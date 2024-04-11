package eu.europeana.api.record.model;

import eu.europeana.api.format.RdfFormat;

public class RecordRequest {

    private String datasetId;

    // clean localId without extension
    private String localId;

    // complete record Id
    private String about;

    private RdfFormat rdfFormat;

    // if the localID had extension like .json or .rdf
    private boolean hasExtension;

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public RdfFormat getRdfFormat() {
        return rdfFormat;
    }

    public void setRdfFormat(RdfFormat rdfFormat) {
        this.rdfFormat = rdfFormat;
    }

    public boolean hasExtension() {
        return hasExtension;
    }

    public void setHasExtension(boolean hasExtension) {
        this.hasExtension = hasExtension;
    }
}
