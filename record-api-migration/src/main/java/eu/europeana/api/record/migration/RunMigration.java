package eu.europeana.api.record.migration;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import eu.europeana.api.record.migration.FTPSupport.FTPContext;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * @author Hugo
 * @since 17 Oct 2023
 */
@Component("migrationRunner")
public class RunMigration {

    private PrintStream progressLog;
    private Transformer transformer = null;
    private Collection<String> processed = new HashSet<String>();

    @Autowired(required = true)
    private MigrationHandler handler;

    public void run(MigrationConfig config) throws Exception {
        File logDir = config.getLoggingDir();
        config.out = new PrintStream(new File(logDir, "run.log"));
        config.err = new PrintStream(new File(logDir, "error.log"));
        try {

            // file and logger setting\\
            File progressFile = new File(logDir, "progress.log");
            loadProcessed(progressFile);
            progressLog = new PrintStream(new FileOutputStream(progressFile, true));
            transformer = TransformerFactory.newInstance().newTransformer();

            // process
            process(config);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            config.out.flush();
            config.err.flush();
            IOUtils.closeQuietly(config.out);
            IOUtils.closeQuietly(config.err);
        }
    }

    private void process(MigrationConfig config) {
        try {
            String url = config.getSource();
            handler.start(config);
            if ( url.startsWith("ftp://") ) {
                processFtp(new FTPSupport(url));
                return;
            }
            if ( url.startsWith("file://") ) {
                processInt(new File(url.replace("file:///", "")));
                return;
            }
            processInt(new File(url));            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            handler.end();
        }
    }


    private void processInt(File file) {
        if ( file.isDirectory() ) {
            for (File f : file.listFiles()) {
                processInt(f);
            }
            return;
        }

        if (processed.contains(file.getPath())) {
            return;
        }

        String name = file.getName();
        if (name.endsWith(".xml")) {
            processXML(file);
        } else if (name.endsWith(".zip")) {
            processZip(file);
        }
    }

    private void loadProcessed(File file) throws IOException {
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String path = reader.readLine().trim();
                if (!StringUtils.isEmpty(path)) {
                    processed.add(path);
                }
            }
        }
    }

    // Handling of Zip resources

    private void processZip(File file) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
            processZip(zis);
            progressLog.println(file.getPath());
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void processZip(ZipInputStream zis) throws IOException {
        ZipEntry ze;
        InputStream is = new FilterInputStream(zis) {
            @Override
            public void close() {
            }
        };
        while ((ze = zis.getNextEntry()) != null) {
            String name = ze.getName();
            if (name.endsWith(".xml") ) {
                processXML(is);
            }
            zis.closeEntry();
        }
    }

    // Handling of XML resources

    private void processXML(File file) {
        try (InputStream is = new FileInputStream(file)) {
            processXML(is);
            progressLog.println(file.getPath());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void processXML(InputStream is) {
        try {
            DOMResult domResult = new DOMResult();
            transformer.transform(new StreamSource(is), domResult);
            handler.runTask((Document) domResult.getNode());
        } catch (TransformerException e) {
        }
    }

    // Handling of FTP listing and resources

    private void processFtp(FTPSupport ftp) throws IOException {
        try {
            processFtp(ftp.getRoot());
        }
        finally {
            ftp.close();
        }
    }

    private void processFtp(FTPContext context) throws IOException {

        if ( context.isDirectory() ) {
            for (FTPFile nfile : context.listFiles() ) { 
                processFtp(context.newContext(nfile));
            }
            return;
        }

        String url  = context.getURL();
        if ( processed.contains(url) ) { return; }

        String name = context.getFilename();
        if ( name.endsWith(".zip") ) {
            try ( ZipInputStream zis = new ZipInputStream(context.openStream()) ) {
                processZip(zis);
                progressLog.println(url);
            }
        }
    }

}
