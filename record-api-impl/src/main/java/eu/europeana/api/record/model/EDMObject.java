package eu.europeana.api.record.model;

import dev.morphia.annotations.*;
import org.bson.types.ObjectId;

@Entity(discriminator = "EDMObject", discriminatorKey = "type")
public class EDMObject {

    @Id
    protected ObjectId objID;

    @Indexed(options = @IndexOptions(name="idx_id", unique = true))
    @Property("id")
    protected String id;

    public ObjectId getObjectID()
    {
        return objID;
    }

    public String getID()
    {
        return id;
    }

}
