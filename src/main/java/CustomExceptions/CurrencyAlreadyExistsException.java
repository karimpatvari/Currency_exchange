package CustomExceptions;

public class CurrencyAlreadyExistsException extends Exception{
    public CurrencyAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
