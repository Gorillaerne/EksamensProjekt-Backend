package gustavo.com.eksamenprojektbackend.Exceptions.LogExceptions;

public class LogCreationException extends LogException {
    public LogCreationException(String message, Throwable cause) {
        super(message, cause);
    }
    public LogCreationException(String message) {
        super(message);
    }
}
