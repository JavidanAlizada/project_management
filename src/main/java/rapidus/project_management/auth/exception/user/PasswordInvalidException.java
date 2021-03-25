package rapidus.project_management.auth.exception.user;

public class PasswordInvalidException extends RuntimeException{

    public PasswordInvalidException(String message) {
        super(message);
    }
}
