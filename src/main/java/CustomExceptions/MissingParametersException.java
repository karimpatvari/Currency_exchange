package CustomExceptions;

public class MissingParametersException extends Exception {
    public MissingParametersException() {
        super();
    }

    public MissingParametersException(String message) {
        super(message);
    }
}
