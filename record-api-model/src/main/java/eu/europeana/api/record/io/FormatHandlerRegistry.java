package eu.europeana.api.record.io;

import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.format.RdfFormat;
import eu.europeana.api.record.io.json.JsonLdWriter;
import eu.europeana.api.record.io.xml.XmlRecordWriter;
import eu.europeana.api.record.model.ProvidedCHO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;

import static eu.europeana.api.config.AppConfigConstants.*;

/**
 * @author Srishti Singh
 * @since 13 Oct 2023
 * @refractored 22 December 2023
 */
@Configuration
@Import(JsonLdWriter.class)
public class FormatHandlerRegistry extends HashMap<RdfFormat, FormatWriter<ProvidedCHO>> {

    public FormatHandlerRegistry(@Qualifier(BEAN_FORMAT_WRITER_JSONLD) JsonLdWriter jsonLdWriter
                               , @Qualifier(BEAN_FORMAT_WRITER_XML) XmlRecordWriter xmlRecordWriter
                               , @Qualifier(BEAN_FORMAT_WRITER_TURTLE) JenaBasedFormatWriter jenaBasedTurtleWriter
                               , @Qualifier(BEAN_FORMAT_WRITER_N3) JenaBasedFormatWriter jenaBasedN3Writer
                               , @Qualifier(BEAN_FORMAT_WRITER_NT) JenaBasedFormatWriter jenaBasedNTWriter) {
        put(RdfFormat.JSONLD, jsonLdWriter);
        put(RdfFormat.JSON  , jsonLdWriter);
        put(RdfFormat.XML   , xmlRecordWriter);
        put(RdfFormat.TURTLE, jenaBasedTurtleWriter);
        put(RdfFormat.N3    , jenaBasedN3Writer);
        put(RdfFormat.NT    , jenaBasedNTWriter);
    }
}

