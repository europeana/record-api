/**
 * 
 */
package eu.europeana.jena.encoder;

/**
 * @author Hugo
 * @since 17 Oct 2023
 */
public class JenaDecoderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JenaDecoderException() {
        super();
    }

    public JenaDecoderException(String message) {
        super(message);
    }

    public JenaDecoderException(Throwable cause) {
        super(cause);
    }

    public JenaDecoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
