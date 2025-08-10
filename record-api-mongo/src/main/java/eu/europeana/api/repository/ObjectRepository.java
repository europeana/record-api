/**
 * 
 */
package eu.europeana.api.repository;

import java.util.Collection;

import dev.morphia.query.FindOptions;
import dev.morphia.query.MorphiaCursor;
import dev.morphia.query.filters.Filter;

/**
 * @author Hugo
 * @since 16 Jun 2024
 */
public interface ObjectRepository<O> {

    public long size();
    
    public boolean exists(String id);


    public O findById(String id);

    public O findById(String id, FindOptions opts);

    public MorphiaCursor<O> findByIds(Collection<String> ids);

    public MorphiaCursor<O> findByIds(Collection<String> ids, FindOptions opts);

    public MorphiaCursor<O> findByFilter(Filter filter);

    public MorphiaCursor<O> findByFilter(Filter filter, FindOptions opts);

    public O save(O obj);
}
