package rapidus.project_management.auth.service.helper;

import org.springframework.stereotype.Component;
import rapidus.project_management.auth.security.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserAuthorizationHelper {

    public boolean isUserNonAuthorized(HttpServletRequest request, JwtTokenUtil tokenUtil, Integer id) {
        String token = request.getHeader("Authorization").substring(7);
        String body = tokenUtil.getBodyFromToken(token);
        int tokenId = Integer.parseInt(body.split(" ")[0]);
        return tokenId != id;
    }
}
