package gustavo.com.eksamenprojektbackend.Exceptions.WarehouseProductExceptions;

public class WarehouseProductFetchException extends WarehouseProductException {
    public WarehouseProductFetchException(String message) {
        super(message);
    }

    public WarehouseProductFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
