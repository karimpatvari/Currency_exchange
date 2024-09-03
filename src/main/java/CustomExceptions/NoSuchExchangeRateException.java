package CustomExceptions;

public class NoSuchExchangeRateException extends Exception {
    public NoSuchExchangeRateException(String message) {
        super(message);
    }

    public NoSuchExchangeRateException() {
        super();
    }
}
