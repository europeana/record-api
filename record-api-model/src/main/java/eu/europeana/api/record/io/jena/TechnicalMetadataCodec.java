/**
 * 
 */
package eu.europeana.api.record.io.jena;

import eu.europeana.api.edm.EBUCORE;
import eu.europeana.api.model.MediaType;
import eu.europeana.api.model.MediaTypes;
import eu.europeana.api.record.model.data.EdmType;
import eu.europeana.api.record.model.media.*;
import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import eu.europeana.jena.encoder.codec.JenaCodec;
import eu.europeana.jena.encoder.library.ClassTemplate;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;

import java.util.Optional;

import static org.apache.jena.rdf.model.ResourceFactory.createProperty;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class TechnicalMetadataCodec implements JenaCodec<TechnicalMetadata>
{
    private static Property hasMimetype = createProperty(EBUCORE.NS + EBUCORE.hasMimeType);

    private MediaTypes mediaTypes;

    public TechnicalMetadataCodec(MediaTypes types) { this.mediaTypes = types; }

    @Override
    public Class<TechnicalMetadata> getSupportedClass() { return TechnicalMetadata.class; }

    @Override
    public void encode(Model m, TechnicalMetadata tm, EncoderContext context) {}

    @Override
    public TechnicalMetadata decode(RDFNode node, DecoderContext context) {
        Statement stmt = context.getResource().getProperty(hasMimetype);
        if( stmt == null ) { return null; }

        Optional<MediaType> mediaType = mediaTypes.getMediaType(stmt.getString());
        if ( mediaType.isEmpty() ) { 
            ClassTemplate template = context.getLibrary().getTemplateByClass(TechnicalMetadata.class);
            if ( template == null ) { return null; }
            return context.decodeByTemplate(template); 
        }

        EdmType type = EdmType.valueOf(mediaType.get().getType().toUpperCase());
        if ( type == null ) { return null; }

        switch (type) {
            //should be decode by Template using a class
            case IMAGE: return context.decodeByClass(node, ImageMetadata.class, false);
            case TEXT : return context.decodeByClass(node, TextMetadata.class, false);
            case VIDEO: return context.decodeByClass(node, VideoMetadata.class, false);
            case SOUND: return context.decodeByClass(node, AudioMetadata.class, false);
            case _3D  : return context.decodeByClass(node, M3DMetadata.class, false);
        }
        return null;
    }
}
