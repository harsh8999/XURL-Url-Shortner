package com.harsh.urlshortner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "url_mapping")
@NoArgsConstructor
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UrlMapping {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String originalUrl;
    
    String shortUrl;

}
