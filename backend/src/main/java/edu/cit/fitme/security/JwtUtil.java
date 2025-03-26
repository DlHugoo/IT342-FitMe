package edu.cit.fitme.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Valid Base64-encoded 64-byte key
    private static final String SECRET = "nGlGdKfXQdK2pjGZoxQ3sM2UuPYvY2jzr9OqpxflZHcZ9gfYXRX8lUEY8x7NzA6nU9VZ7FzOnF4WZKxjFtKEXQ==";
    private static final long EXPIRATION = 86400000; // 1 day

    // Decode Base64 secret to Key
    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

    // ✅ Accept role and email when generating token
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role) // ✅ role now correctly passed
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ Optional: Get role from token
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
