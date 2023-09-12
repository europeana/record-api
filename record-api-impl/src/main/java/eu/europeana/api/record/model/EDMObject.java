package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.morphia.annotations.*;
import org.bson.types.ObjectId;

import static eu.europeana.api.record.vocabulary.RecordFields.ID;

@Entity(discriminator = "EDMObject", discriminatorKey = "type")
public class EDMObject {

    @Id
    @JsonIgnore
    protected ObjectId objID;

    @Indexed(options = @IndexOptions(name="idx_id", unique = true))
    @Property("id")
    protected String id;

    @JsonIgnore
    public ObjectId getObjectID()
    {
        return objID;
    }

    @JsonIgnore
    public String getID()
    {
        return id;
    }

}
