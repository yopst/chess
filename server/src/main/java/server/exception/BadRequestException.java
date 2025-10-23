package server.exception;

public class BadRequestException extends EndpointException {
    public BadRequestException(String message) {
        super(message, 400); // Bad request error code
    }
}
