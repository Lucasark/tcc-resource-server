package tcc.uff.resource.server.exceptions;

public class GenericException extends RuntimeException {

    public GenericException() {
        super();
    }

    public GenericException(String message) {
        super(message);
    }

    public GenericException(Throwable cause) {
        super(cause.getMessage());
    }
}
