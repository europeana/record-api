/**
 * 
 */
package eu.europeana.jena.encoder;

/**
 * @author Hugo
 * @since 17 Oct 2023
 */
public class JenaEncoderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JenaEncoderException() {
        super();
    }

    public JenaEncoderException(String message) {
        super(message);
    }

    public JenaEncoderException(Throwable cause) {
        super(cause);
    }

    public JenaEncoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
