package eu.europeana.api.format;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public interface FormatWriter<T> {
    public void write(T value, OutputStream out) throws IOException;

    public void write(Iterator<T> value, int size, OutputStream out) throws IOException;

}
