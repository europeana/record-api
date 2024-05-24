package eu.europeana.api.record.io;

import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.format.RdfFormat;
import eu.europeana.api.record.io.json.JsonLdWriter;
import eu.europeana.api.record.io.json.v2.JsonV2Writer;
import eu.europeana.api.record.io.xml.XmlRecordWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import static eu.europeana.api.config.AppConfigConstants.*;

import java.util.HashMap;

/**
 * @author Srishti Singh
 * @since 7 march 2024
 */
@Configuration
public class FormatHandlerRegistryV2 extends HashMap<RdfFormat, FormatWriter> {

    public FormatHandlerRegistryV2(@Qualifier(BEAN_FORMAT_WRITER_V2_JSON) JsonV2Writer jsonV2Writer
                                 , @Qualifier(BEAN_FORMAT_WRITER_JSONLD) JsonLdWriter jsonLdWriter
                                 , @Qualifier(BEAN_FORMAT_WRITER_XML) XmlRecordWriter xmlRecordWriter
                                 , @Qualifier(BEAN_FORMAT_WRITER_TURTLE) JenaBasedFormatWriter jenaBasedTurtleWriter) {
        put(RdfFormat.JSONLD, jsonLdWriter);
        put(RdfFormat.JSON  , jsonV2Writer);
        put(RdfFormat.XML   , xmlRecordWriter);
        put(RdfFormat.TURTLE, jenaBasedTurtleWriter);

    }
}
