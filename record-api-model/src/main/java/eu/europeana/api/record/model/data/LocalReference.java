package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.*;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.PostLoad;
import dev.morphia.annotations.PostPersist;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.ModelConstants;
import org.bson.Document;

import static eu.europeana.api.record.model.ModelConstants.*;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@Entity(discriminator = Local, discriminatorKey = RDF.type)
public class LocalReference implements ObjectReference
{
    @JsonProperty(ModelConstants.id)
    @Property(ModelConstants.id)
    protected String           id;

    @JsonUnwrapped
    @Property(ModelConstants.object)
    protected EDMClass object;

    public LocalReference() {}

    public LocalReference(String id)
    {
        this.id     = id;
        this.object = null;
    }

    public LocalReference(EDMClass object)
    {
        this.id     = object.getID();
        this.object = object;
    }


    public String getID() { return this.id; }

    public boolean isLocal() { return true; }

    public boolean isDereferenced() { return this.object != null; }

    public EDMClass getDereferencedObject() { return this.object; }

    public String toString() { return ("<" + id + ">"); }

    @PostPersist
    public void postPersist(Document doc) {
        if ( this.object != null ) { doc.remove("id"); }
    }

    @PostLoad
    public void postLoad(Document doc) {
        if ( this.object != null ) { this.id = this.object.getID(); }
    }
}
