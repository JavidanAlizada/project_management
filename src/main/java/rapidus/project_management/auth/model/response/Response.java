package rapidus.project_management.auth.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rapidus.project_management.auth.exception.BaseException;

@Data
@NoArgsConstructor
public class Response<T> {

    private Boolean success;
    private T body;
    private BaseException error;

    public Response(T body) {
        this.success = true;
        this.body = body;
        this.error = null;
    }

    public Response(BaseException error) {
        this.success = false;
        this.body = null;
        this.error = error;
    }
}
