package gustavo.com.eksamenprojektbackend.Exceptions.WarehouseProductExceptions;

public class WarehouseProductNotFoundException extends WarehouseProductException {
    public WarehouseProductNotFoundException(String message) {
        super(message);
    }

    public WarehouseProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
