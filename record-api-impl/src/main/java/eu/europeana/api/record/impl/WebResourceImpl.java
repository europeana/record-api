package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.model.TechMetadata;
import eu.europeana.api.record.model.WebResource;
import eu.europeana.api.record.deserialization.LiteralStringConverter;
import eu.europeana.api.record.vocabulary.FieldTypes;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@Entity(useDiscriminator = false)
@JsonPropertyOrder({ID, TYPE})
public class WebResourceImpl extends EdmEntityImpl implements WebResource {

    private Literal<String> type = new LiteralImpl<>(FieldTypes.WebResource.getFieldType());;

    @JsonDeserialize(as = TechMetadataImpl.class)
    private TechMetadataImpl techMetadata;

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
        this.techMetadata = (TechMetadataImpl) techMetadata;
    }
}
