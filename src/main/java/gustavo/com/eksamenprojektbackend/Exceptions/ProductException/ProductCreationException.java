package gustavo.com.eksamenprojektbackend.Exceptions.ProductException;

public class ProductCreationException extends ProductException {
    public ProductCreationException(String message,Throwable cause) {
        super(message, cause);
    }

    public ProductCreationException(String message) {
        super(message);
    }


}
