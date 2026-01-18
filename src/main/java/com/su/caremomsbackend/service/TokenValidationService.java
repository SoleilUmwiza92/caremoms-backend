package com.su.caremomsbackend.service;

import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenValidationService {

    private final UserRepository userRepository;


    @Value("${supabase.jwt.secret}")
    private String jwtSecret;

    @jakarta.annotation.PostConstruct
    public void init() {
        log.info("JwtService initialized with secret: {}", (jwtSecret != null ? "LOADED" : "NULL"));
    }

    public String extractName(String token){
        return extractClaim(token, claims -> {
            HashMap<?, ?> userMetadata = claims.get("user_metadata", HashMap.class);
            if (userMetadata != null) {
                return userMetadata.get("name").toString();
            }
            return null;
        });
    }

    public String extractRole(String token){
        return extractClaim(token, claims -> {
            HashMap<?, ?> userMetadata = claims.get("user_metadata", HashMap.class);
            if (userMetadata != null && userMetadata.get("role") != null) {
                return userMetadata.get("role").toString();
            }
            return "Regular";
        });
    }
    public String extractEmail(String token) {
        return extractClaim(token, claims -> {
            HashMap<?, ?> userMetadata = claims.get("user_metadata", HashMap.class);
            if (userMetadata != null) {
                return userMetadata.get("email").toString();
            }
            return null;
        });
    }

    public String extractPersonId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(HttpServletRequest request) {
        try {
            if(isNotBlank(request.getHeader("Authorization"))) {

                String token = request.getHeader("Authorization").substring(7);
                return !isTokenExpired(token);
            }
            return isNotBlank(request.getHeader("Admin")) && getAdminUser(request.getHeader("Admin"))!= null;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public User getAdminUser(String adminUser){
       // String adminUser= request.getHeader("Admin");
        if(!adminUser.isBlank()) {
            log.info("Admin use is {}", adminUser);
            User user =userRepository.findByEmail(adminUser).orElse(null);
            log.info("user returned {}", user);
            return user;
        }
        return null;
    }
}
