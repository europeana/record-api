package eu.europeana.api.record.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.model.TechMetadata;
import eu.europeana.api.record.model.WebResource;
import eu.europeana.api.record.serialization.LiteralStringConverter;

public class WebResourceImpl extends EdmEntityImpl implements WebResource {

    @JsonDeserialize(converter = LiteralStringConverter.class)
    private Literal<String> type;

    @JsonDeserialize(as = TechMetadataImpl.class)
    private TechMetadata techMetadata;

    public WebResourceImpl() {
    }

    @Override
    public Literal<String> getType() {
        return type;
    }

    public void setType(Literal<String> type) {
        this.type = type;
    }

    public String getAbout() {
        return super.getAbout();
    }

    @Override
    public TechMetadata getTechMetadata() {
        return techMetadata;
    }

    @Override
    public void setTechMetadata(TechMetadata techMetadata) {
        this.techMetadata = techMetadata;
    }
}