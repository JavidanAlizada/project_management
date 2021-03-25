package rapidus.project_management.auth.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import rapidus.project_management.auth.model.UserDTO;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${jwt.expires}")
    public String JWT_TOKEN_VALIDITY;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.salt}")
    private String salt;

    public String getBodyFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDTO userDTO) {
        Map<String, Object> claims = new HashMap<>();
        final String generatedTokenBody = String.format("%s %s %s", userDTO.getId(), userDTO.getEmail(), salt);
        return doGenerateToken(claims, generatedTokenBody);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Integer.parseInt(JWT_TOKEN_VALIDITY)))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token, UserDTO userDTO) {
        final String body = getBodyFromToken(token);
        String[] tokenBodyParts = body.split(" ");
        boolean isTokenBodyValid = tokenBodyParts[0].equals(String.valueOf(userDTO.getId()))
                && tokenBodyParts[1].equals(userDTO.getEmail())
                && tokenBodyParts[2].equals(salt);
        return  isTokenBodyValid && !isTokenExpired(token);
    }

}