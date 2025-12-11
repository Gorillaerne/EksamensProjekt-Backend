package gustavo.com.eksamenprojektbackend.Exceptions.WarehouseExceptions;

public class WareHouseUpdateException extends WareHouseException  {
    public WareHouseUpdateException(String message) {
        super(message);
    }

    public WareHouseUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
