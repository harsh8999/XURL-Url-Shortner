package com.harsh.urlshortner.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUrlBody {
    private String originalUrl;
    private String shortUrl;
}
