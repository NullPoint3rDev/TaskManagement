//package com.practice.task_management.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//
//import java.util.Date;
//
//@Component
//public class JwtTokenUtil {
//
//    private static final String SECRET_KEY = "mysecretkey";
//    private static final long EXPIRATION_TIME = 86400000L; // 1 day in ms
//
//    public String generateToken(String username) {
//        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
//    }
//
//    public String getUsernameFromToken(String token) {
//        return getClaimsFromToken(token).getSubject();
//    }
//
//    public boolean isTokenExpired(String token) {
//        return getExpirationDateFromToken(token).before(new Date());
//    }
//
//    public Date getExpirationDateFromToken(String token) {
//        return getClaimsFromToken(token).getExpiration();
//    }
//
//    public boolean validateToken(String token, String username) {
//        return (username.equals(getUsernameFromToken(token)) && !isTokenExpired(token));
//    }
//
//    private Claims getClaimsFromToken(String token) {
//        SecretKey key = Keys.hmacShaKeyFor("mysecretkey".getBytes());
//
//        return Jwts.parser()
//                .verifyWith(key) // Установка ключа для проверки
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//}

package com.practice.task_management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey"; // Должен быть длиной 256 бит (32 символа)
    private static final long EXPIRATION_TIME = 86400000L; // 1 день в миллисекундах

    private final Key key;

    public JwtTokenUtil() {
        // Создаем ключ один раз при инициализации компонента
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS512) // Используем ключ
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(getUsernameFromToken(token)) && !isTokenExpired(token));
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // Используем ключ из Jakarta API
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

