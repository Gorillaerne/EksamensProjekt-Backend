package gustavo.com.eksamenprojektbackend.Exceptions.DeliveryException;

public class DeliveryRegistrationException extends RuntimeException {
    public DeliveryRegistrationException(String message) {
        super(message);
    }

    public DeliveryRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
