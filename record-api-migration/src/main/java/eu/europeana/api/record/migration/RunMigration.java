package eu.europeana.api.record.migration;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
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
@SpringBootApplication(
        scanBasePackages = {"eu.europeana.api.record.migration"},
        exclude = {
                // Remove these exclusions to re-enable security
                SecurityAutoConfiguration.class,
                // DataSources are manually configured (for EM and batch DBs)
                DataSourceAutoConfiguration.class
        })
@PropertySource(
        value = {"classpath:migration.properties", "classpath:migration.user.properties"},
        ignoreResourceNotFound = true)
public class RunMigration implements CommandLineRunner {

    @Value("${source.url}")
    private String sourceURL;

    @Value("${target.file}")
    private String targetDirectory;

    private PrintStream progressLog;
    private Transformer transformer = null;
    private Collection<String> processed = new HashSet<String>();

    @Autowired
    private MigrationHandler handler;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RunMigration.class, args);
        System.exit(SpringApplication.exit(context));
    }

    @Override
    public void run(String... args) throws Exception {
        File logDir = new File(targetDirectory);
        PrintStream out   = new PrintStream(new File(logDir, "run.log"));
        PrintStream error = new PrintStream(new File(logDir, "error.log"));
        try {
            // handler settings
            handler.setLoggingDir(logDir);
            handler.setThreads(20);
            handler.setSaveCopy(true);
            handler.setValidateDB(true);

            // file and logger setting
            File progressFile = new File(logDir, "progress.log");
            loadProcessed(progressFile);
            progressLog = new PrintStream(new FileOutputStream(progressFile, true));
            transformer = TransformerFactory.newInstance().newTransformer();

            // process
            process(sourceURL);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            out.flush();
            error.flush();
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(error);
        }
    }

    private void process(String url) {
        try {
            handler.start();
            if ( url.startsWith("ftp://") ) {
                processFtp(new FTPSupport(url));
                return;
            }
            if ( url.startsWith("file://") ) {
                processInt(new File(url.replace("file:///", "")));
                return;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            handler.end();
        }
    }


    private void processInt(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                processInt(file);
                continue;
            }

            if (processed.contains(file.getPath())) {
                continue;
            }

            String name = file.getName();
            if (name.endsWith(".xml")) {
                processXML(file);
            } else if (name.endsWith(".zip")) {
                processZip(file);
            }
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
