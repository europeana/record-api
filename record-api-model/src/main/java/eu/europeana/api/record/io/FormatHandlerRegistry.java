/**
 * 
 */
package eu.europeana.api.record.io;

import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.format.RdfFormat;
import eu.europeana.api.record.io.json.JsonLdSerializer;
import eu.europeana.api.record.io.json.JsonLdWriter;
import eu.europeana.api.record.io.xml.XmlRecordWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author Hugo
 * @since 13 Oct 2023
 */
@Service
public class FormatHandlerRegistry extends HashMap<RdfFormat, FormatWriter> {

    @Autowired
    JsonLdSerializer jsonLdSerializer;

//    @Autowired
//    XmlRecordWriter xmlRecordWriter;
//
//    @Resource(name = "ttl")
//    JenaBasedFormatWriter Turtle_jenaBasedFormatWriter;
//
//    @Resource(name = "N3")
//    JenaBasedFormatWriter N3_jenaBasedFormatWriter;
//
//    @Resource(name = "NT")
//    JenaBasedFormatWriter NT_jenaBasedFormatWriter;



    public FormatHandlerRegistry() {
        put(RdfFormat.JSONLD, jsonLdSerializer);
        put(RdfFormat.XML   , new XmlRecordWriter());
        put(RdfFormat.TURTLE, new JenaBasedFormatWriter("TURTLE"));
        put(RdfFormat.N3    , new JenaBasedFormatWriter("N3"));
        put(RdfFormat.NT    , new JenaBasedFormatWriter("NT"));
    }
}
