package fr.istic.taa.jaxrs.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JwtUtil {

    private static final String JWT_ISSUER = "concerts-app";
    private static final long JWT_LIFETIME_MS = 60 * 60 * 1000; // 1h

    /**
     * Use JWT_SECRET when available to keep tokens valid across restarts.
     * Fallback to a generated key for local/dev convenience.
     */
    private static final Key key = buildKey();

    private static Key buildKey() {
        String configuredSecret = System.getenv("JWT_SECRET");
        if (configuredSecret != null && !configuredSecret.isBlank()) {
            byte[] raw = configuredSecret.getBytes(StandardCharsets.UTF_8);
            // HS256 expects at least a 256-bit key.
            return Keys.hmacShaKeyFor(Arrays.copyOf(raw, 32));
        }
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public static class TokenPayload implements Principal {
        private final String username;
        private final Set<String> roles;

        public TokenPayload(String username, Set<String> roles) {
            this.username = username;
            this.roles = roles;
        }

        public String username() {
            return username;
        }

        public Set<String> roles() {
            return roles;
        }

        @Override
        public String getName() {
            return username;
        }
    }

    public static String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(JWT_ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_LIFETIME_MS))
                .claim("roles", roles)
                .signWith(key)
                .compact();
    }

    public static TokenPayload validateToken(String token) throws JwtException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<String> rolesList = claims.get("roles", List.class);
        Set<String> roles = rolesList == null ? Set.of() : new HashSet<>(rolesList);

        return new TokenPayload(claims.getSubject(), roles);
    }
}
