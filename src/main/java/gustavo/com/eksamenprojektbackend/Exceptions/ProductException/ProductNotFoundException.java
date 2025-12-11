package gustavo.com.eksamenprojektbackend.Exceptions.ProductException;

public class ProductNotFoundException extends ProductException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
