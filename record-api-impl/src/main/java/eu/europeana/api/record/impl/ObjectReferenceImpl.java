package eu.europeana.api.record.impl;

import eu.europeana.api.record.datatypes.ObjectReference;

public class ObjectReferenceImpl implements ObjectReference {

    private String id;
    private Object referencedObject;

    public ObjectReferenceImpl() {
    }

    public ObjectReferenceImpl(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object getReferencedObject() {
        return referencedObject;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setReferencedObject(Object referencedObject) {
        this.referencedObject = referencedObject;
    }

    @Override
    public String toString() {
        return "ObjectReferenceImpl{" +
                "id='" + id + '\'' +
                ", referencedObject=" + referencedObject +
                '}';
    }
}
