package gustavo.com.eksamenprojektbackend.Exceptions.WarehouseProductExceptions;

public class WarehouseProductException extends RuntimeException {
    public WarehouseProductException(String message) {
        super(message);
    }

    public WarehouseProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
