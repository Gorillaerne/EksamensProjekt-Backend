package gustavo.com.eksamenprojektbackend.Exceptions.WarehouseExceptions;

public class WareHouseCreationException extends WareHouseException {
    public WareHouseCreationException(String message) {
        super(message);
    }

    public WareHouseCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
