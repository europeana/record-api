package eu.europeana.api.format;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public interface FormatWriter<T>
{
    /**
     * Method for serialising T value
     * @param value value to be formatted
     * @param out output stream
     * @throws IOException
     */
    void write(T value, OutputStream out) throws IOException;

    /**
     * Method for serialising list of values
     * @param value list of value to be formatted
     * @param out output stream
     * @throws IOException
     */
    void write(List<T> value, OutputStream out) throws IOException;

}
