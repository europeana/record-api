package eu.europeana.api.record.io;

import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.format.RdfFormat;
import eu.europeana.api.record.io.json.JsonLdWriter;
import eu.europeana.api.record.io.json.v2.JsonV2Writer;
import eu.europeana.api.record.io.xml.XmlRecordWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author Srishti Singh
 * @since 7 march 2024
 */
@Configuration
public class FormatHandlerRegistryV2 extends HashMap<RdfFormat, FormatWriter> {

    private final JsonV2Writer jsonV2Writer;

    private final JsonLdWriter jsonLdWriter;

    private final XmlRecordWriter xmlRecordWriter;

    private final JenaBasedFormatWriter jenaBasedTurtleWriter;


    public FormatHandlerRegistryV2(JsonV2Writer jsonV2Writer,
                                   JsonLdWriter jsonLdWriter,
                                   XmlRecordWriter xmlRecordWriter,
                                   @Qualifier("jenaFormatWriterTurtle") JenaBasedFormatWriter jenaBasedTurtleWriter) {
        this.jsonV2Writer = jsonV2Writer;
        this.jsonLdWriter = jsonLdWriter;
        this.xmlRecordWriter = xmlRecordWriter;
        this.jenaBasedTurtleWriter = jenaBasedTurtleWriter;

        put(RdfFormat.JSON, this.jsonV2Writer);
        put(RdfFormat.JSONLD, this.jsonLdWriter);
        put(RdfFormat.XML, this.xmlRecordWriter);
        put(RdfFormat.TURTLE, this.jenaBasedTurtleWriter);

    }
}
