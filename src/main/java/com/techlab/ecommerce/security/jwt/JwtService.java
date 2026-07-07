package com.techlab.ecommerce.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Service
public class JwtService {

    private static final String SECRET_KEY = "bXk5bW1hX3NlY3JldF9mb3JfZGV2ZWxvcG1lbnRfcHVycG9zZXNfa2V5XzIwMjY=";

    public String extraerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }   

    public String generarToken(UserDetails usuario) {
        return generarToken(new HashMap<>(), usuario);
    }

    public String generarToken(Map<String, Object> claimsExtra, UserDetails usuario) {
        return Jwts.builder()
                .claims(claimsExtra)
                .subject(usuario.getUsername()) 
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 5)) 
                .signWith(getKey()) 
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean esTokenValido(String token, UserDetails usuario) {
        final String username = extraerUsername(token);
        return (username.equals(usuario.getUsername())) && !estaExpirado(token);
    }

    private boolean estaExpirado(String token) {
        return obtenerExpiracion(token).before(new Date());
    }

    private Date obtenerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey()) 
                .build()
                .parseSignedClaims(token) 
                .getPayload(); 
    }
     
    public String obtenerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }
}
