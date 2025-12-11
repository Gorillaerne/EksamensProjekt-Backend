package gustavo.com.eksamenprojektbackend.Exceptions.WarehouseExceptions;

public class WareHouseFetchException extends WareHouseException {
    public WareHouseFetchException(String message) {
        super(message);
    }

    public WareHouseFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
