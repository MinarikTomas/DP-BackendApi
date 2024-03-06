package sk.stuba.fei.uim.dp.attendanceapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Role;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class JWTGenerator {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    @Bean
    public MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }


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
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        System.out.println("New token : ");
        System.out.println(token);
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
            return Arrays.stream(rolesString.substring(1, rolesString.length()-1).split(",")).map(
                    String::trim
            ).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public boolean isTokenValid(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
