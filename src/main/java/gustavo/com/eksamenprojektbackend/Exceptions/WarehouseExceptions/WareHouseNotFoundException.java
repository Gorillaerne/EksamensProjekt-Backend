package gustavo.com.eksamenprojektbackend.Exceptions.WarehouseExceptions;

public class WareHouseNotFoundException extends WareHouseException {
    public WareHouseNotFoundException(String message) {
        super(message);
    }

    public WareHouseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
