package eu.europeana.api.record.model.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.json.LiteralSerializer;
import eu.europeana.api.record.model.data.Literal;

import static eu.europeana.api.record.vocabulary.RecordFields.FILE_BYTE_SIZE;
import static eu.europeana.api.record.vocabulary.RecordFields.MIME_TYPE;


@Entity(discriminatorKey = "type")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public abstract class TechnicalMetadata {

    @JsonSerialize(using = LiteralSerializer.class)
    @JsonProperty(MIME_TYPE)
    private Literal<String> mimetype;

    @JsonSerialize(using = LiteralSerializer.class)
    @JsonProperty(FILE_BYTE_SIZE)
    private Literal<Long>   fileByteSize;

    public TechnicalMetadata() {}

    public Literal<String> getMimetype()
    {
        return this.mimetype;
    }

    public Literal<Long> getFileByteSize()
    {
        return this.fileByteSize;
    }

    public void setMimetype(Literal<String> mimetype)
    {
        this.mimetype = mimetype;
    }

    public void setFileByteSize(Literal<Long> fileByteSize)
    {
        this.fileByteSize = fileByteSize;
    }
}
