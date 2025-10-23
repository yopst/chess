package server.exception;

public class UserAlreadyExistsException extends EndpointException {
    public UserAlreadyExistsException(String message) {
        super(message, 403); // Forbidden error code
    }
}
