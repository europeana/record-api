/**
 * 
 */
package eu.europeana.api.record.migration;

/**
 * @author Hugo
 * @since 7 Jun 2024
 */
public class TestMigration
{

    public static final void main(String[] args) {
        String[] args2 = 
            { 
//              "-source", "ftp://anonymous@download.europeana.eu:21/dataset/XML/9200467.zip",
              "-source", "file:///C:/Work/incoming/Record v3/source/",
              "-logDir", "C:/Work/incoming/Record v3/target/" 
            };
        MigrationCommand.main(args2);
    }
}
