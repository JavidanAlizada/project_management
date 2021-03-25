package rapidus.project_management.auth.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rapidus.project_management.auth.exception.token.JwtTokenExpiredException;
import rapidus.project_management.auth.exception.token.JwtTokenUnableException;
import rapidus.project_management.auth.model.UserDTO;
import rapidus.project_management.auth.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${server.address}")
    private String ip;

    @Value("${server.port}")
    private String port;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        StringBuffer endpointUrl = request.getRequestURL();
        String body = null;
        String jwtToken = null;
        String[] parsedTokenBodyParts = null;
        if (!endpointUrl.toString().contains("api")) {
            chain.doFilter(request, response);
            return;
        }

        if (!(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer "))) {
            throw new AccessDeniedException("Jwt Token is not given");
        }
        
        jwtToken = requestTokenHeader.substring(7);
        try {
            body = jwtTokenUtil.getBodyFromToken(jwtToken);
            parsedTokenBodyParts = body.split(" ");
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                chain.doFilter(request, response);
                return;
            }
            UserDTO userDetails = this.userService.loadUserById(Integer.valueOf(parsedTokenBodyParts[0]));
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null);
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                throw new AccessDeniedException("Jwt Token is invalid.");
            }
        } catch (IllegalArgumentException e) {
            throw new JwtTokenUnableException("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException("JWT Token has expired");
        }
        chain.doFilter(request, response);
    }
}