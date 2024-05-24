/**
 * 
 */
package eu.europeana.api.record.migration;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * @author Hugo
 * @since 22 May 2024
 */
public class FTPSupport
{
    private FTPClient client;
    private String    baseURL;
    private URI       uri;

    public FTPSupport(String url) throws SocketException, IOException {
        uri = URI.create(url);
        client = new FTPClient();
        client.connect(uri.getHost(), uri.getPort());
        boolean isSuccess = client.login(uri.getUserInfo(), "");
        if ( !isSuccess ) { throw new IOException("Cannot connnect"); }
        client.enterLocalPassiveMode();

        client.changeWorkingDirectory(uri.getPath());
        baseURL = url.replace(uri.getPath(), "");
    }

    public FTPContext getRoot() throws IOException {
        String path = uri.getPath();

        FTPFile[] files = client.listFiles(path);
        if ( files.length == 1 ) {
            return new FTPContext(path, files[0]);
        }
        return new FTPRoot(path, files);
    }

    public void close() throws IOException {
        try {
            client.logout();
        }
        finally {
            client.disconnect();
        }
    }

    public class FTPContext {

        private String  path;
        private FTPFile file;

        public FTPContext(String path, FTPFile file) { 
            this.path = path;
            this.file = file;
        }

        public FTPContext newContext(FTPFile file) {
            return new FTPContext(this.path + "/" + file.getName(), file);
        }

        public FTPFile[] listFiles() throws IOException {
            return client.listFiles(path);
        }

        public boolean isDirectory() {
            return file.isDirectory();
        }

        public String getURL() {
            return baseURL + path;
        }

        public FTPFile getFile() {
            return file;
        }

        public String getFilename() {
            return file.getName();
        }

        public InputStream openStream() throws IOException {
            return client.retrieveFileStream(this.path);
        }
    }

    public class FTPRoot extends FTPContext {

        private FTPFile[] files;

        public FTPRoot(String path, FTPFile[] files) { 
            super(path, null);
            this.files = files;
        }

        public FTPFile[] listFiles() throws IOException {
            return files;
        }

        public boolean isDirectory() {
            return true;
        }

        public FTPFile getFile() {
            return null;
        }

        public String getFilename() {
            return null;
        }
    }
}
