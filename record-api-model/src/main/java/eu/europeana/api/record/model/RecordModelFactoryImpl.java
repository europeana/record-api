package eu.europeana.api.record.model;

import eu.europeana.api.record.model.data.Datatype;
import eu.europeana.api.record.model.data.DatatypeLiteral;
import eu.europeana.api.record.model.data.DatatypeUtils;
import eu.europeana.api.record.model.data.LanguageLiteral;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.LocalReference;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.data.SharedReference;
import eu.europeana.api.record.model.entity.ContextualEntity;
import eu.europeana.api.record.model.entity.License;

/**
 * @author Hugo
 * @since 28 Nov 2023
 */
public class RecordModelFactoryImpl implements RecordModelFactory {

    public static final RecordModelFactory INSTANCE = new RecordModelFactoryImpl();

    @Override
    public <T> Literal<T> newLiteral(T value) {
        return new Literal<T>(value);
    }

    @Override
    public LanguageLiteral newLiteral(String value, String lang) {
        return new LanguageLiteral(value, lang);
    }

    @Override
    public <T> DatatypeLiteral<T> newLiteral(T value, Datatype datatype) {
        return new DatatypeLiteral<T>(value, datatype);
    }

    @Override
    public <T> DatatypeLiteral<T> newLiteral(T value, String datatype) {
        return new DatatypeLiteral<T>(value, DatatypeUtils.resolveDatatype(datatype));
    }

    @Override
    public ObjectReference newReference(String uri) {
        return (isLocal(uri) ? new LocalReference(uri) : new SharedReference(uri));
    }

    /*
     * For the time being, references to non-contextual entities will always be 
     * created only with the reference and no object attached, with the exception
     * of cc:License because there isnt any fixed location for it unless edm:rights
     * is not longer a ObjectReference and points directly to a cc:License.
     * 
     * This is mainly because of the storage in MongoDB since we dont have yet a
     * way to re-attach the object without creating a duplicate.
     */
    @Override
    public ObjectReference newReference(EDMClass obj) {
        if ( obj instanceof License ) { return new LocalReference(obj); }
        if ( !(obj instanceof ContextualEntity) ) { return new LocalReference(obj.getID()); }
        return (isLocal(obj.getID()) ? new LocalReference(obj) : new SharedReference(obj));
    }

    private boolean isLocal(String uri) {
        return ( uri.startsWith("/") || uri.startsWith("#") );
    }

    private boolean isLocal(EDMClass obj) {
        return ( !(obj instanceof ContextualEntity) || isLocal(obj.getID()) );
    }
}