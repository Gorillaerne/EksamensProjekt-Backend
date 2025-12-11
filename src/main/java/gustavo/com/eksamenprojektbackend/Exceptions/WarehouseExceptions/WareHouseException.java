package gustavo.com.eksamenprojektbackend.Exceptions.WarehouseExceptions;

public class WareHouseException extends RuntimeException {
    public WareHouseException(String message) {
        super(message);
    }

    public WareHouseException(String message, Throwable cause) {
        super(message, cause);
    }
}
