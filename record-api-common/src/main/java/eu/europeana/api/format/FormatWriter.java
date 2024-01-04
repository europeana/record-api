/**
 * 
 */
package eu.europeana.api.format;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public interface FormatWriter<T>
{
    public void write(T value, OutputStream out) throws IOException;
}
