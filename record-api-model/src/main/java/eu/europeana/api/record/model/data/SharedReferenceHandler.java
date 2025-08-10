/**
 * 
 */
package eu.europeana.api.record.model.data;

/**
 * @author Hugo
 * @since 12 Jun 2025
 */
public interface SharedReferenceHandler {

    public void saveShared(SharedObject obj);

    public void loadShared(SharedReference ref);

}
