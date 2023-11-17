package eu.europeana.api.record.io;

import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.format.RdfFormat;
import eu.europeana.api.record.io.json.JsonLdWriter;
import eu.europeana.api.record.io.xml.XmlRecordWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author Hugo
 * @since 13 Oct 2023
 * @refractored Srishti Singh 17 November 2023
 */
@Service
public class FormatHandlerRegistry extends HashMap<RdfFormat, FormatWriter> {

    @Autowired
    JsonLdWriter jsonLdWriter;

    @Autowired
    XmlRecordWriter xmlRecordWriter;

    @Resource(name = AppConfigConstants.BEAN_JENA_FORAMAT_WRITER_TURTLE)
    JenaBasedFormatWriter jenaBasedTurtleWriter;

    @Resource(name = AppConfigConstants.BEAN_JENA_FORAMAT_WRITER_N3)
    JenaBasedFormatWriter JenaBasedN3Writer;

    @Resource(name = AppConfigConstants.BEAN_JENA_FORAMAT_WRITER_NT)
    JenaBasedFormatWriter JenaBasedNTWriter;

    public FormatHandlerRegistry() {
        put(RdfFormat.JSONLD, jsonLdWriter);
        put(RdfFormat.XML, xmlRecordWriter);
        put(RdfFormat.TURTLE, jenaBasedTurtleWriter);
        put(RdfFormat.N3, JenaBasedN3Writer);
        put(RdfFormat.NT, JenaBasedNTWriter);
    }
}

