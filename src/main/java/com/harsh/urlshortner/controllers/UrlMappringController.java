package com.harsh.urlshortner.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harsh.urlshortner.entity.UrlMapping;
import com.harsh.urlshortner.models.RequestUrlBody;
import com.harsh.urlshortner.models.ResponseUrlBody;
import com.harsh.urlshortner.service.UrlMappingService;

@RestController
@RequestMapping("/api/v1/url")
public class UrlMappringController {
    
    @Autowired
    private UrlMappingService urlMappingService;

    @PostMapping
    public ResponseEntity<ResponseUrlBody> shortner(@RequestBody RequestUrlBody requestUrlBody) {
        UrlMapping urlMapping = urlMappingService.createShortUrl(requestUrlBody.getUrl());
        ResponseUrlBody url = new ResponseUrlBody();
        url.setOriginalUrl(urlMapping.getOriginalUrl());
        url.setShortUrl(urlMapping.getShortUrl());
        return new ResponseEntity<>(url, HttpStatus.CREATED);
    }

    // Request: short url 
    // Response: Original url
    @GetMapping("/short")
    public ResponseEntity<ResponseUrlBody> getOriginalUrl(@RequestBody RequestUrlBody requestUrlBody) {
        UrlMapping urlMapping = urlMappingService.getOriginalUrlFromShortUrl(requestUrlBody.getUrl());
        ResponseUrlBody url = new ResponseUrlBody();
        url.setOriginalUrl(urlMapping.getOriginalUrl());
        url.setShortUrl(urlMapping.getShortUrl());
        return new ResponseEntity<>(url, HttpStatus.CREATED);
    }

    
    // Request: Original url
    // Respose: short url 
    @GetMapping("/original")
    public ResponseEntity<ResponseUrlBody> getShortUrl(@RequestBody RequestUrlBody requestUrlBody) {
        UrlMapping urlMapping = urlMappingService.getShortUrlFromOriginalUrl(requestUrlBody.getUrl());
        ResponseUrlBody url = new ResponseUrlBody();
        url.setOriginalUrl(urlMapping.getOriginalUrl());
        url.setShortUrl(urlMapping.getShortUrl());
        return new ResponseEntity<>(url, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ResponseUrlBody>> getAll() {
        List<UrlMapping> urlMapping = urlMappingService.getAll();

        List<ResponseUrlBody> url = new ArrayList<>();
        urlMapping.stream().map(e -> url.add(new ResponseUrlBody(e.getOriginalUrl(), e.getShortUrl()))).collect(Collectors.toList());
        
        return new ResponseEntity<>(url, HttpStatus.CREATED);
    }
}
