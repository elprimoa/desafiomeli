package com.example.urlshortener.dto;

/**
 * DTO for URL shortening response
 */
public class ShortenUrlResponse {
    
    private String shortUrl;
    private String longUrl;
    private boolean isNew;
    
    public ShortenUrlResponse() {}
    
    public ShortenUrlResponse(String shortUrl, String longUrl, boolean isNew) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.isNew = isNew;
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
    
    public boolean isNew() {
        return isNew;
    }
    
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
} 