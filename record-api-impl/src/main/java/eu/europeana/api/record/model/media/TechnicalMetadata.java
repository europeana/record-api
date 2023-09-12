package eu.europeana.api.record.model.media;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.model.data.Literal;

import static eu.europeana.api.record.vocabulary.RecordFields.FILE_BYTE_SIZE;
import static eu.europeana.api.record.vocabulary.RecordFields.MIME_TYPE;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@Entity(discriminatorKey = "type")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public abstract class TechnicalMetadata
{
    private Literal<String> mimetype;

    private Literal<Long>   fileByteSize;

    public TechnicalMetadata() {}

    @JsonGetter(MIME_TYPE)
    public Literal<String> getMimetype()
    {
        return this.mimetype;
    }

    @JsonGetter(FILE_BYTE_SIZE)
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
