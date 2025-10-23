package server.exception;

public class EndpointException extends Exception {
    private final int errorCode;

    // Constructor that takes a message and an error code
    public EndpointException(String message, int errorCode) {
        super(message); // Pass the message to the superclass (Exception)
        this.errorCode = errorCode; // Set the error code
    }

    // Default constructor with a default error code of 500
    public EndpointException(String message) {
        super(message);
        this.errorCode = 500; // Set default error code to 500
    }

    // Getter for the error code
    public int getErrorCode() {
        return errorCode;
    }
}

