/**
 * 
 */
package eu.europeana.api.record.migration;

import java.io.File;
import java.io.PrintStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Hugo
 * @since 6 Jun 2024
 */
@Component("migrationConfig")
@PropertySource(
        value = {"classpath:migration.properties"},
        ignoreResourceNotFound = true)
public class MigrationConfig
{

    @Value("${source.url}")
    private String sourceURL;

    @Value("${log.dir}")
    private String logDir;

    @Value("${validate}")
    private boolean validate;

    @Value("${validateDB}")
    private boolean validateDB;
    
    @Value("${saveCopy}")
    private boolean saveCopy;
    
    @Value("${external}")
    private boolean genExternal;

    @Value("${threads}")
    private int threads;

    public PrintStream err;

    public PrintStream out;


    public void setSource(String url) {
        this.sourceURL = url;
    }

    public String getSource() {
        return this.sourceURL;
    }

    public void setLoggingDir(String logDir) {
        this.logDir = logDir;
    }

    public File getLoggingDir() {
        return new File(this.logDir);
    }

    public boolean isToLog() {
        return this.logDir != null;
    }

    public void setThreads(Integer threads) {
        if ( threads != null ) { this.threads = threads; }
    }

    public int getThreads() {
        return this.threads;
    }

    public void setValidate(Boolean validate) {
        if ( validate != null ) { this.validate = validate; }
    }

    public boolean isToValidate() {
        return this.validate;
    }

    public void setValidateDB(Boolean validate) {
        if ( validate != null ) { this.validateDB = validate; }
    }

    public boolean isToValidateDB() {
        return this.validateDB;
    }

    public void setSaveCopy(Boolean saveCopy) {
        if ( saveCopy != null ) { this.saveCopy = saveCopy; }
    }

    public boolean isToSaveCopy() {
        return this.saveCopy;
    }

    public void setGenerateExternal(Boolean genExternal) { 
        if ( genExternal != null ) { this.genExternal = genExternal; }
    }

    public boolean isToGenerateExternal() { 
        return this.genExternal;
    }
}
