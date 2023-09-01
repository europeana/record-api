package eu.europeana.api.record.datatypes;

public interface ObjectReference extends DataValue {

    String getId();

    Object getReferencedObject();

    void setId(String id);

    void setReferencedObject(Object referencedObject);

}
