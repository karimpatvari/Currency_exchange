package CustomExceptions;

public class NoSuchCurrencyException extends Exception{
    public NoSuchCurrencyException() {
        super();
    }

    public NoSuchCurrencyException(String message) {
        super(message);
    }
}
