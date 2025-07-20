package com.example.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for URL mapping enable/disable request
 */
public class EnableUrlRequest {
    @JsonProperty("isEnabled")
    private boolean isEnabled;
    
    public EnableUrlRequest() {}
    
    public EnableUrlRequest(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public boolean isEnabled() {
        return isEnabled;
    }
    
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
} 