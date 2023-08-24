package eu.europeana.api.record.datatypes;

public interface ObjectReference {

    String getId();

    Object getReferencedObject();

    void setId(String id);

    void setReferencedObject(Object referencedObject);

}
