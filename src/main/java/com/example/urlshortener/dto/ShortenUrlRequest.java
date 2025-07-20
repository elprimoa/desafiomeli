package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO for URL shortening request
 */
public class ShortenUrlRequest {
    
    @NotBlank(message = "URL cannot be null or empty")
    @Pattern(
        regexp = "^https?://[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-.,@?^=%&:/~+#]*[\\w\\-@?^=%&/~+#])?$",
        message = "Invalid URL format. Must be a valid HTTP or HTTPS URL"
    )
    private String longUrl;
    
    public ShortenUrlRequest() {}
    
    public ShortenUrlRequest(String longUrl) {
        this.longUrl = longUrl;
    }
    
    public String getLongUrl() {
        return longUrl;
    }
    
    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
} 