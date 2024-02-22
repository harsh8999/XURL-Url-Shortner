package com.harsh.urlshortner.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class UrlShortnerServiceImplementation implements UrlShortnerService {

    private static final String BASE_URL = "http://shorturl.com/";
    private static final String ALGORITHM = "SHA-256";

    @Override
    public String generateShortUrl(String originalUrl) {
        try {
            // Hash the original URL using SHA-256
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hashBytes = digest.digest(originalUrl.getBytes(StandardCharsets.UTF_8));
            // Convert the hash bytes to a base64-encoded string
            String hashString = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
            // Take the first 8 characters of the base64-encoded string
            String shortHash = hashString.substring(0, 8);
            // Combine with base URL
            return BASE_URL + shortHash;
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // Handle error
            return null;
        }
    }
    
}
