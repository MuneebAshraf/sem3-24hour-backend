package errorhandling;

/**
 *
 * @author cph-ma670
 */
public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super("Requested item could not be found");
    }
}
