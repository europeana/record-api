package eu.europeana.api.record.impl;

public class ProvidedCHO extends EdmEntity {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getAbout() {
        return super.getAbout();
    }

    @Override
    public void setAbout(String about) {
        super.setAbout(about);
    }
}
