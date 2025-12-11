package gustavo.com.eksamenprojektbackend.Exceptions.WarehouseExceptions;

public class WarehouseDeletionException extends WareHouseException {
    public WarehouseDeletionException(String message) {
        super(message);
    }

    public WarehouseDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
