package eu.europeana.api.record.migration;

import dev.morphia.query.filters.Filters;
import eu.europeana.api.edm.EDM;
import eu.europeana.api.format.RdfFormat;
import eu.europeana.api.record.db.repository.RecordRepository;
import eu.europeana.api.record.io.FormatHandlerRegistry;
import eu.europeana.api.record.migration.RecordDomProcessor.Result;
import eu.europeana.api.record.model.*;
import eu.europeana.api.record.model.internal.ProxyComparator;
import eu.europeana.jena.encoder.JenaObjectDecoder;
import eu.europeana.jena.encoder.JenaObjectEncoder;
import eu.europeana.jena.encoder.library.TemplateLibrary;
import eu.europeana.jena.encoder.utils.JenaUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfxml.xmlinput.DOM2Model;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.jena.rdf.model.ResourceFactory.createResource;

/**
 * @author Hugo
 * @since 3 Nov 2023
 */
@Service
public class MigrationHandler {

    private static final int DEFAULT_THREADS = 20;
    private static final Resource ProvidedCHO = createResource(EDM.NS + EDM.ProvidedCHO);
    private static ThreadLocal<String> recordId = new ThreadLocal();
    private RdfFormat format = RdfFormat.JSONLD;
    private boolean validate = true;
    protected boolean validateDB = false;
    protected boolean saveCopy = false;
    private File logDir = null;
    private int threads = DEFAULT_THREADS;
    private ExecutorService executor = null;

    private final MigrationSettings settings;

    private RecordJenaProcessor jenaProcessor;

    private RecordDomProcessor domProcessor;

    private final FormatHandlerRegistry registry;

    private final MigrationRepository migrationRepository;

    private final TemplateLibrary library;

    @Autowired
    public MigrationHandler(MigrationSettings settings, FormatHandlerRegistry registry, MigrationRepository migrationRepository, TemplateLibrary library) {
        this.settings = settings;
        this.registry = registry;
        this.migrationRepository = migrationRepository;
        this.library = library;
        this.jenaProcessor = new RecordJenaProcessor(this.settings.getMediaTypes());
        this.domProcessor = new RecordDomProcessor();
        JenaUtils.disableRiotValidation();
        executor = null;
    }

    public void setLoggingDir(File logDir) {
        this.logDir = logDir;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public void setSaveCopy(boolean saveCopy) {
        this.saveCopy = saveCopy;
    }

    public void setValidateDB(boolean validate) {
        this.validateDB = validate;
    }


    public void start() {
        executor = getExecutor();
    }

    public void runTask(Document doc) {
        if (executor != null) {
            executor.submit(new MigrationTask(doc));
        }
    }

    public void end() {
        if (executor == null) {
            return;
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ie) {
        }
    }

    public class MigrationTask implements Runnable {

        private Document document;
        private Resource cho;

        public MigrationTask(Document document) {
            this.document = document;
        }

        @Override
        public void run() {
            String recordId = domProcessor.getRecordId(document);
            if (recordId == null) {
                return;
            }

            MigrationHandler.recordId.set(recordId);
            try {
                storeInDB(parse(domProcessor.process(document)));
            } catch (Throwable t) {
                synchronized (System.err) {
                    logErr("Exception in cho: " + recordId);
                    t.printStackTrace(System.err);
                }
            } finally {
                MigrationHandler.recordId.remove();
            }
        }

        private ProvidedCHO parse(Result res) throws IOException, SAXParseException {

            Model m = ModelFactory.createDefaultModel();
            DOM2Model dom2Model = DOM2Model.createD2M(res.uri, m);
            dom2Model.setProperty(JenaUtils.allowBadURIs, "true");
            dom2Model.load(res.doc);

            ResIterator iter = m.listResourcesWithProperty(RDF.type, ProvidedCHO);
            if (!iter.hasNext()) {
                return null;
            }

            Resource cho = jenaProcessor.upgrade(iter.next());
            if (cho == null) {
                return null;
            }

            this.cho = cho;
            ProvidedCHO pcho = (ProvidedCHO) new JenaObjectDecoder(library, RecordModelFactoryImpl.INSTANCE).decode(cho);

            //Sort proxies from Europeana to Provider Proxy
            Collections.sort(pcho.getProxies(), ProxyComparator.INSTANCE);

            //Sort views based on the original order in the XML
            Aggregation aggr = pcho.getProviderProxy().getProxyIn();
            if (aggr != null && aggr.hasViews()) {
                ViewComparator.sort(aggr.getViews(), res.views);
            }

            if (validate) {
                validate(m, pcho);
            }
            return pcho;
        }

        private ProvidedCHO validate(Model m1, ProvidedCHO cho) throws IOException {
            String uri = cho.getID();
            Model m2 = ModelFactory.createDefaultModel();
            new JenaObjectEncoder(library).encode(cho, m2, uri);
            Model diff = m1.difference(m2);
            long differences = diff.size();
            if (differences > 0) {
                logErr("Jena differences <" + uri + ">" + differences + "\n"
                        + diff.toString());
                System.err.flush();
            }

            if (logDir != null && (saveCopy || differences > 0)) {
                storeInFile(cho, newFile(uri));
            }
            return cho;
        }

        private void storeInDB(ProvidedCHO cho) throws IOException {
            if (migrationRepository == null) {
                return;
            }

            migrationRepository.save(cho);

            if (!validateDB) {
                return;
            }

            String uri = cho.getID();
            EDMClass o2 = migrationRepository.getDatastore().find(cho.getClass())
                    .filter(Filters.eq(ModelConstants.id, uri))
                    .first();

            Model m2 = ModelFactory.createDefaultModel();
            new JenaObjectEncoder(library).encode(o2, m2, uri);

            Model diff = this.cho.getModel().difference(m2);
            long differences = diff.size();
            if (differences > 0) {
                logErr("db differences " + this.cho.getModel().size() + " - " + m2.size() + " = " + differences + "\n"
                        + diff.toString());
                System.err.flush();
            }

            if (logDir != null && (saveCopy || differences > 0)) {
                storeInFile(cho, newFile(uri));
            }
        }

        private void storeInFile(ProvidedCHO cho, File out) throws IOException {
            FileOutputStream fos = new FileOutputStream(out);
            try {
                registry.get(format).write(cho, fos);
                fos.flush();
            } finally {
                IOUtils.closeQuietly(fos);
            }
        }
    }

    private File newFile(String uri) {
        String name = uri.replace(ModelConstants.dataItemUri, "")
                + "." + format.getExtension();
        File file = new File(logDir, name);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return file;
    }

    private ExecutorService getExecutor() {
        return new ThreadPoolExecutor(
                threads, threads, 0L, TimeUnit.MILLISECONDS
                , new ArrayBlockingQueue<Runnable>(threads * 10)
                , new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static void log(String str) {
        System.out.println(recordId.get() + " :> " + str);
    }

    public static void logErr(String str) {
        System.err.println(recordId.get() + " :> " + str);
    }
}
