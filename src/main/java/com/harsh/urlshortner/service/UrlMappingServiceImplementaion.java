package com.harsh.urlshortner.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.harsh.urlshortner.entity.UrlMapping;
import com.harsh.urlshortner.repository.UrlMappingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UrlMappingServiceImplementaion implements UrlMappingService {

    private UrlMappingRepository urlMappingRepository;
    private UrlShortnerService urlShortnerService;

    @Override
    public UrlMapping createShortUrl(String originalUrl) {

        // Check if the URL already exists in the database
        Optional<UrlMapping> existingMapping = urlMappingRepository.findByOriginalUrl(originalUrl);
        if (existingMapping.isPresent()) {
            return existingMapping.get();
        }
        
        UrlMapping newUrl = new UrlMapping();
        newUrl.setOriginalUrl(originalUrl);
        // create short URL
        String shortUrl = urlShortnerService.generateShortUrl(originalUrl);
        newUrl.setShortUrl(shortUrl);
        return urlMappingRepository.save(newUrl);
    }

    @Override
    public UrlMapping getShortUrlFromOriginalUrl(String originalUrl) {
        return urlMappingRepository.findByOriginalUrl(originalUrl).orElseThrow(() -> new RuntimeException("Url Not found!!!"));
    }

    @Override
    public UrlMapping getOriginalUrlFromShortUrl(String shortUrl) {
        return urlMappingRepository.findByShortUrl(shortUrl).orElseThrow(() -> new RuntimeException("Url not found!!!"));
    }

    @Override
    public List<UrlMapping> getAll() {
        return urlMappingRepository.findAll();
    }
    
}
