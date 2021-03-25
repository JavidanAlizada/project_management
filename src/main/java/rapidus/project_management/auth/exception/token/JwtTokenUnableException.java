package rapidus.project_management.auth.exception.token;

public class JwtTokenUnableException extends RuntimeException {

    public JwtTokenUnableException(String message) {
        super(message);
    }
}
