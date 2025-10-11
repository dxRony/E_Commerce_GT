/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author ronyrojas
 */
@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    // obteniendo username del token
    public String obtenerUsername(String token) {
        return obtenerClaim(token, Claims::getSubject);
    }

    // obteniendo claim x del token
    public <T> T obtenerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = obtenerClaimsToken(token);
        return claimsResolver.apply(claims);
    }

    // generando token user -> UserDetails
    public String generarToken(UserDetails userDetails) {
        return generarToken(new HashMap<>(), userDetails);
    }

    // generando token con claims
    public String generarToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return crearToken(extraClaims, userDetails, jwtExpiration);
    }

    // construyend el token jwt
    private String crearToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(obtenerFirmaToken(), SignatureAlgorithm.HS256)
                .compact();
    }

    // validar token
    public boolean isTokenValido(String token, UserDetails userDetails) {
        final String username = obtenerUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpirado(token);
    }

    // valida token expirado
    private boolean isTokenExpirado(String token) {
        return obtenerExpiracionToken(token).before(new Date());
    }

    // obtiene fecha de expiracion token
    private Date obtenerExpiracionToken(String token) {
        return obtenerClaim(token, Claims::getExpiration);
    }

    // obtiene los claims del token
    private Claims obtenerClaimsToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(obtenerFirmaToken())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // obtiene la clave de firma
    private SecretKey obtenerFirmaToken() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // validando de cualquier error al token
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(obtenerFirmaToken())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("token jwt invalido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("token jwt expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("token jwt no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("jwt sin claims: {}", e.getMessage());
        }
        return false;
    }
}
