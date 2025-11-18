package client.exceptions;

public class ResponseException extends Exception {
    final private int statusCode;
    public ResponseException(int status, String message) {
        super(message);
        statusCode = status;
    }
    public int getStatusCode() {
        return statusCode;
    }
}
