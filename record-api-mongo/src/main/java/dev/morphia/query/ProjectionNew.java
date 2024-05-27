/**
 * 
 */
package dev.morphia.query;

import org.bson.Document;

import com.mongodb.lang.Nullable;

import dev.morphia.mapping.Mapper;

/**
 * @author Hugo
 * @since 17 Apr 2024
 */
public class ProjectionNew extends dev.morphia.query.Projection {

    private Document projection;


    /**
     * @param options
     */
    public ProjectionNew(FindOptions options) {
        super(options);
    }

    /**
     * Adds a field to the projection clause. The _id field is always included unless explicitly suppressed.
     *
     * @param fields the fields to include
     * @return this
     * @see <a href="https://docs.mongodb.com/manual/tutorial/project-fields-from-query-results/">Project Fields to Return from Query</a>
     */
    @Override
    public FindOptions include(String... fields) {
        if (projection == null) { projection = new Document(); }
        for ( String field : fields ) {
            projection.put(field, 1);
        }
        return super.include(fields);
    }
    @Override
    @Nullable
    public Document map(Mapper mapper, Class<?> type) {
        return projection;
    }
}