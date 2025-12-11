package gustavo.com.eksamenprojektbackend.Exceptions.LogExceptions;

public class LogNotFoundException extends LogException {
    public LogNotFoundException(String message) {
        super(message);
    }
    public LogNotFoundException(String message,Throwable cause) {
        super(message, cause);
    }
}
