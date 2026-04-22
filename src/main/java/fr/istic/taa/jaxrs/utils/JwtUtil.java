package fr.istic.taa.jaxrs.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.security.Principal;
import java.util.*;

public class JwtUtil {

    private static final String JWT_ISSUER = "concerts-app";
    private static final long JWT_LIFETIME_MS = 60 * 60 * 1000; // 1h
    /** Cette clé sera renouveller à chaque redemarrage du serveur   */
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /** Permet de savoir quel utilisateur fait des requête sur le site */
    public static class TokenPayload implements Principal {
         private final String username;
         private final Set<String> roles;

        public TokenPayload(String username, Set<String> roles) {
            this.username = username;
            this.roles = roles;
        }

        //retourne le username stocké dans le payload
        public String username() { return username; }
        public Set<String> roles() { return roles; }

        //Représente le "nom" de l'utilisateur authentifié.
        @Override
        public String getName() {
            return username;
        }
    }

    /** Génère un token à partir du nom et du role de l'utilisateur */
    public static String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username) // ex: mail
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