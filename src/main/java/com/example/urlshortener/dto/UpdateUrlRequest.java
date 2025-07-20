package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO for URL mapping update request
 */
public class UpdateUrlRequest {
    
    @NotBlank(message = "New URL cannot be null or empty")
    @Pattern(
        regexp = "^https?://[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-.,@?^=%&:/~+#]*[\\w\\-@?^=%&/~+#])?$",
        message = "Invalid URL format. Must be a valid HTTP or HTTPS URL"
    )
    private String newLongUrl;
    
    public UpdateUrlRequest() {}
    
    public UpdateUrlRequest(String newLongUrl) {
        this.newLongUrl = newLongUrl;
    }
    
    public String getNewLongUrl() {
        return newLongUrl;
    }
    
    public void setNewLongUrl(String newLongUrl) {
        this.newLongUrl = newLongUrl;
    }
} 