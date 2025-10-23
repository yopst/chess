package server.exception;

public class UnauthorizedException extends EndpointException {
    public UnauthorizedException(String message) {
        super(message, 401); // Unauthorized error code
    }
}
