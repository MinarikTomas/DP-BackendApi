package sk.stuba.fei.uim.dp.attendanceapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Role;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;

import java.security.KeyPair;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class JWTGenerator {

    private static final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

        public String generateToken(User user){
        String email = user.getEmail();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + Constants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("id", user.getId())
                .claim("fullName", user.getFullName())
                .claim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .claim("hasCard", !user.getCards().isEmpty())
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
        return token;
    }

    public String generateRefreshToken(User user){
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + Constants.REFRESH_EXPIRATION);

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    public String getEmailFromJWT(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keyPair.getPrivate())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public List<String> getRolesFromJWT(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Object rolesObject = claims.get("roles");
        if(rolesObject != null){
            String rolesString = rolesObject.toString();
            return Arrays.stream(rolesString.substring(1, rolesString.length()-1).split(",")).map(
                    String::trim
            ).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public boolean isTokenValid(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(keyPair.getPublic())
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
