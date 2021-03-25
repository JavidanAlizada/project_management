package rapidus.project_management.auth.exception.user;

import rapidus.project_management.auth.exception.BaseException;

import java.time.LocalDateTime;

public class UserNotFoundException extends RuntimeException implements BaseException {

    private String message;

    public UserNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return null;
    }

    @Override
    public Integer getCode() {
        return null;
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getFullPath() {
        return null;
    }
}
