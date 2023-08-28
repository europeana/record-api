package eu.europeana.api.record.utils;

import static eu.europeana.api.record.utils.RecordConstants.BASE_URL;
public class RecordUtils {

    public static String buildRecordId(String collectionId, String recordId) {
        return  BASE_URL + collectionId + "/" + recordId;

    }
}
