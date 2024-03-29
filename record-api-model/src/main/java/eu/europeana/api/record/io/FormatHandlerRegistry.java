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

    private final JsonLdWriter jsonLdWriter;

    private final XmlRecordWriter xmlRecordWriter;

    private final JenaBasedFormatWriter jenaBasedTurtleWriter;

    private final JenaBasedFormatWriter jenaBasedN3Writer;

    private final JenaBasedFormatWriter jenaBasedNTWriter;


    public FormatHandlerRegistry(JsonLdWriter jsonLdWriter, XmlRecordWriter xmlRecordWriter,
                                 @Qualifier("jenaFormatWriterTurtle") JenaBasedFormatWriter jenaBasedTurtleWriter,
                                 @Qualifier("jenaFormatWriterN3") JenaBasedFormatWriter jenaBasedN3Writer,
                                 @Qualifier("jenaFormatWriterNt") JenaBasedFormatWriter jenaBasedNTWriter) {
        this.jsonLdWriter = jsonLdWriter;
        this.xmlRecordWriter = xmlRecordWriter;
        this.jenaBasedTurtleWriter = jenaBasedTurtleWriter;
        this.jenaBasedN3Writer = jenaBasedN3Writer;
        this.jenaBasedNTWriter = jenaBasedNTWriter;

        put(RdfFormat.JSONLD, this.jsonLdWriter);
        put(RdfFormat.JSON, this.jsonLdWriter);
        put(RdfFormat.XML, this.xmlRecordWriter);
        put(RdfFormat.TURTLE, this.jenaBasedTurtleWriter);
        put(RdfFormat.N3, this.jenaBasedN3Writer);
        put(RdfFormat.NT, this.jenaBasedNTWriter);
    }
}

