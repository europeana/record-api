/**
 * 
 */
package eu.europeana.jena.encoder.library;

/**
 * @author Hugo
 * @since 17 Oct 2023
 */
public class JenaTemplateCompilerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JenaTemplateCompilerException() {
        super();
    }

    public JenaTemplateCompilerException(String message) {
        super(message);
    }

    public JenaTemplateCompilerException(Throwable cause) {
        super(cause);
    }

    public JenaTemplateCompilerException(String message, Throwable cause) {
        super(message, cause);
    }
}
