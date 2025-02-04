package com.example.demo.service;

import com.example.demo.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

import javax.crypto.SecretKey;


@Service("jwtService")
public class JwtService {

    @Autowired
    @Qualifier("usuarioService")
    private UserService userService;

    private static final long HORAS_EXPIRACION= 48; // 48 horas
    private static final long EXPIRACION = HORAS_EXPIRACION * 60 * 60 * 1000;

    // Generar clave segura para HS512
    private final SecretKey secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);

    public String generateToken(UserDetails user){
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRACION))
                .signWith(secretKey)
                .compact();
    }
    private Claims getClaims(String token) {
        return Jwts.parser()
        		.verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public boolean isAdmin(String token){
        boolean respuesta = false;

        token = token.replace("Bearer ","");
        String email = extractUsername(token);
        Usuario usuario = userService.findByEmail(email);

        if (usuario.getRol().equals("ROLE_ADMIN"))
            respuesta = true;

        return respuesta;
    }
}
