/**
 * 
 */
package eu.europeana.api.record.model.data;

import eu.europeana.api.record.model.EDMClass;

/**
 * @author Hugo
 * @since 28 Nov 2023
 */
public interface DataValueFactory {

    public <T> Literal<T> newLiteral(T value);

    public LanguageLiteral newLiteral(String value, String lang);

    public <T> DatatypeLiteral<T> newLiteral(T value, Datatype datatype);

    public <T> DatatypeLiteral<T> newLiteral(T value, String datatype);

    public ObjectReference newReference(String uri);

    public ObjectReference newReference(EDMClass obj);
}
