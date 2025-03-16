package json.Exceptions;

public final class JSONMappingException extends Exception {
    public JSONMappingException(String message) {
        super(message);
    }

    public JSONMappingException(String message, Exception cause) {
        super(message, cause);
    }
}
