package com.harsh.urlshortner.service;

import java.util.List;

import com.harsh.urlshortner.entity.UrlMapping;

public interface UrlMappingService {

    UrlMapping createShortUrl(String originalUrl);
    UrlMapping getShortUrlFromOriginalUrl(String originalUrl);
    UrlMapping getOriginalUrlFromShortUrl(String shortUrl);
    List<UrlMapping> getAll();

}
