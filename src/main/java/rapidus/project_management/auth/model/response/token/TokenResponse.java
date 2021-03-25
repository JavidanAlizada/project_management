package rapidus.project_management.auth.model.response.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse implements Serializable {

    private String jwttoken;
}