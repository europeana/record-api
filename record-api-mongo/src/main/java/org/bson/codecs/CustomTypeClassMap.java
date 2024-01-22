/**
 * 
 */
package org.bson.codecs;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.bson.BsonType;

/**
 * @author Hugo
 * @since 27 Nov 2023
 */
public class CustomTypeClassMap extends BsonTypeClassMap {

    protected Collection<Class<?>> classes = new HashSet();

    public CustomTypeClassMap() {
        super(Collections.singletonMap(BsonType.DATE_TIME, Instant.class));

        for ( BsonType type : super.keys() ) {
            classes.add(get(type));
        }
    }

    public boolean containsValue(Class<?> clazz) {
        return classes.contains(clazz);
    }
}
