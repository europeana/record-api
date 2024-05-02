package eu.europeana.api.record.io;

import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.format.RdfFormat;
import eu.europeana.api.record.io.json.JsonLdWriter;
import eu.europeana.api.record.io.xml.XmlRecordWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author Srishti Singh
 * @since 13 Oct 2023
 * @refractored 22 December 2023
 */
@Configuration
public class FormatHandlerRegistry extends HashMap<RdfFormat, FormatWriter> {

    public FormatHandlerRegistry(JsonLdWriter jsonLdWriter, XmlRecordWriter xmlRecordWriter,
                                 @Qualifier("jenaFormatWriterTurtle") JenaBasedFormatWriter jenaBasedTurtleWriter,
                                 @Qualifier("jenaFormatWriterN3") JenaBasedFormatWriter jenaBasedN3Writer,
                                 @Qualifier("jenaFormatWriterNt") JenaBasedFormatWriter jenaBasedNTWriter) {
        put(RdfFormat.JSONLD, jsonLdWriter);
        put(RdfFormat.JSON  , jsonLdWriter);
        put(RdfFormat.XML   , xmlRecordWriter);
        put(RdfFormat.TURTLE, jenaBasedTurtleWriter);
        put(RdfFormat.N3    , jenaBasedN3Writer);
        put(RdfFormat.NT    , jenaBasedNTWriter);
    }
}

