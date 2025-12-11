package gustavo.com.eksamenprojektbackend.Exceptions.UserExceptions;

public class LoginFailedException extends UserException {
    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
