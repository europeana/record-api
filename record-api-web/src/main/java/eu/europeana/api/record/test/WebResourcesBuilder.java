package eu.europeana.api.record.test;

import eu.europeana.api.record.model.*;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.media.AudioMetadata;
import eu.europeana.api.record.model.media.VideoMetadata;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class WebResourcesBuilder {

//    private void testWebresources() {
//        int counter = 50;
//        for (int i=0; i<100000; i++) {
//            ProvidedCHO record = new TestDataBuilder().newRecord();
//            LOGGER.info("Adding {} record with  {} web resources ", i, (counter *2));
//            new WebResourcesBuilder().createWebresources(record, counter);
//            try {
//                record = recordService.saveRecord(record);
//                LOGGER.info("saved {}, web resources {} ", record.getID(), record.getIsAggregatedBy().getViews().size());
//                counter = counter + 50;
//
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public ProvidedCHO createWebresources(ProvidedCHO testRecord, Integer counter) {
        List<WebResource> hasViews = new ArrayList<>();
        for(int i=0; i <counter; i++) {
            hasViews.add(newAudio(i));
            hasViews.add(newVideo(i));
        }
        testRecord.getIsAggregatedBy().getViews().addAll(hasViews);

        testRecord.setID(RandomStringUtils.randomAlphanumeric(10));

        return testRecord;
    }


    private WebResource newAudio(int id) {
        WebResource wr = new WebResource("http://testingWebresourcesLimit/" + id);
        AudioMetadata techMeta = new AudioMetadata();
        techMeta.setBitDepth(new Literal((long)16));
        techMeta.setBitRate(new Literal((long)63999));
        techMeta.setChannels(new Literal((long)1));
        techMeta.setDuration(new Literal((long)1695400000));
        techMeta.setFileByteSize(new Literal((long) 1234567));
        techMeta.setMimetype(new Literal<>("audio/mp4"));
        techMeta.setSampleRate(new Literal((long)44100));
        techMeta.setCodec(new Literal<>("mp4"));
        wr.setTechnicalMetadata(techMeta);
        return wr;
    }


    private WebResource newVideo(int id) {
        WebResource wr = new WebResource("http://testingWebresourcesLimit/" + id);
        VideoMetadata techMeta = new VideoMetadata();
        techMeta.setMimetype(new Literal<>("video/x-flv"));
        techMeta.setFileByteSize(new Literal((long) 6654705));
        techMeta.setDuration(new Literal((long)1695400000));
        techMeta.setBitRate(new Literal((long)230860));
        techMeta.setCodec(new Literal("flV1"));
        techMeta.setFrameRate(new Literal((long)25));
        techMeta.setHeight(new Literal((long)240));
        techMeta.setWidth(new Literal((long)32));
        wr.setTechnicalMetadata(techMeta);
        return wr;
    }

}

