package rapidus.project_management.auth.exception;

import java.time.LocalDateTime;

public interface BaseException {


    LocalDateTime getTimeStamp();

    Integer getCode();

    String getStatus();

    String getType();

    String getMessage();

    String getFullPath();

}
