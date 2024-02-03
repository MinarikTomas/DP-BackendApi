package sk.stuba.fei.uim.dp.attendanceapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Role;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class JWTGenerator {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(User user){
        String email = user.getEmail();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("id", user.getId())
                .claim("fullName", user.getFullName())
                .claim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        System.out.println("New token : ");
        System.out.println(token);
        return token;
    }

    public String generateRefreshToken(Authentication authentication){
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.REFRESH_EXPIRATION);

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getEmailFromJWT(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public List<String> getRolesFromJWT(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Object rolesObject = claims.get("roles");
        if(rolesObject != null){
            String rolesString = rolesObject.toString();
            return Arrays.stream(rolesString.split(",")).toList();
        }
        return new ArrayList<>();
    }

    public boolean isTokenValid(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            System.out.println("Token is valid");
            return true;
        }catch (Exception ex){
            System.out.println("Token is invalid");
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
