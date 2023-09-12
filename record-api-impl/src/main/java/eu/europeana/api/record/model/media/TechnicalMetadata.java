/**
 * 
 */
package eu.europeana.api.record.model.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.model.data.Literal;

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