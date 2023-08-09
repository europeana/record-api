package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import eu.europeana.api.record.model.TechMetadata;

public class TechMetadataImpl implements TechMetadata {

    private Long fileByteSize;

    public TechMetadataImpl() {
    }

    @Override
    @JsonGetter("fileSize")
    public Long getFileByteSize() {
        return fileByteSize;
    }

    @Override
    @JsonSetter("fileSize")
    public void setFileByteSize(Long fileByteSize) {
        this.fileByteSize = fileByteSize;
    }
}
