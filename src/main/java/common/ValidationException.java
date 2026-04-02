package common;

/**
 * Exception thrown to indicate that a business rule or validation constraint within the system has been violated.
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs a new ValidationException with the specified message.
     *
     * @param message the message explanation of what validation failed
     */
    public ValidationException(String message){
        super(message);
    }
}
