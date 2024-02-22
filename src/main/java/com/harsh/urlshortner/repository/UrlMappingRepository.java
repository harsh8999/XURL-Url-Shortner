package com.harsh.urlshortner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harsh.urlshortner.entity.UrlMapping;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByOriginalUrl(String originalUrl);
    Optional<UrlMapping> findByShortUrl(String shortUrl);
    
}
