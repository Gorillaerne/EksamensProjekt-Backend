package gustavo.com.eksamenprojektbackend.Exceptions.ProductException;

public class ProductFetchException extends ProductException {
    public ProductFetchException(String message) {
        super(message);
    }

    public ProductFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
