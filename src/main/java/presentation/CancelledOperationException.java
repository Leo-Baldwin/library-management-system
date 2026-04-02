package presentation;

/**
 * Exception thrown to indicate that an operation has been cancelled by the user.
 */
public class CancelledOperationException extends RuntimeException {

    /**
     * Constructs a new {@code CancelledOperationException} and provides a default message.
     */
    public CancelledOperationException() {
        super("Operation cancelled, returning to menu...");
    }
}