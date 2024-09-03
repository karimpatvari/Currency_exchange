package CustomExceptions;

public class ExchangeRateAlreadyExistsException extends Exception {
    public ExchangeRateAlreadyExistsException() {
        super();
    }

    public ExchangeRateAlreadyExistsException(String message) {
        super(message);
    }
}
