package com.example.urlshortener.dto;

/**
 * DTO for URL mapping representation
 */
public class UrlMapping {
    
    private String shortUrl;
    private String longUrl;
    private boolean isEnabled;
    
    public UrlMapping() {}
    
    public UrlMapping(String shortUrl, String longUrl, boolean isEnabled) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.isEnabled = isEnabled;
    }
    
    public String getShortUrl() {
        return shortUrl;
    }
    
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
    
    public String getLongUrl() {
        return longUrl;
    }
    
    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
} 