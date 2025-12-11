package gustavo.com.eksamenprojektbackend.Exceptions.UserExceptions;

public class UserCreationException extends UserException {
    public UserCreationException(String message) {
        super(message);
    }

    public UserCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
