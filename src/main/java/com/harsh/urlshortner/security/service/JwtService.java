package com.harsh.urlshortner.security.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service class for JWT token generation, validation, and extraction.
 */
@Service
public class JwtService {
    
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Extracts the username from the JWT token.
     * 
     * @param token The JWT token
     * @return The username extracted from the token
     */
    public String extractUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token.
     * 
     * @param token           The JWT token
     * @param claimsResolver  Function to resolve the desired claim
     * @param <T>             Type of the claim
     * @return The claim extracted from the token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     * 
     * @param token The JWT token
     * @return All claims extracted from the token
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token for the provided user details with default expiration time.
     * 
     * @param userDetails The user details
     * @return The generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token with extra claims for the provided user details and expiration time.
     * 
     * @param extraClaims Additional claims to include in the token
     * @param userDetails The user details
     * @return The generated JWT token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                    .claims().empty().add(extraClaims).and()
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey(), Jwts.SIG.HS512)
                    .compact();
    }

    /**
     * Retrieves the configured JWT expiration time.
     * 
     * @return The JWT expiration time
     */
    public long getJwtExpiration() {
        return this.jwtExpiration;
    }

    /**
     * Validates whether a JWT token is valid for the provided user details.
     * 
     * @param token       The JWT token
     * @param userDetails The user details
     * @return True if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
}
