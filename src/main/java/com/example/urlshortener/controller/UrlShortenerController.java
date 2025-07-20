package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortenUrlRequest;
import com.example.urlshortener.dto.ShortenUrlResponse;
import com.example.urlshortener.dto.UpdateUrlRequest;
import com.example.urlshortener.dto.EnableUrlRequest;
import com.example.urlshortener.dto.UrlMapping;
import com.example.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for URL shortening operations
 */
@RestController
@RequestMapping("")
@CrossOrigin(origins = "*")
public class UrlShortenerController {
    
    private final UrlShortenerService urlShortenerService;
    
    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }
    
    /**
     * POST /shorten
     * Shortens a long URL or returns existing short URL
     */
    @PostMapping("/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        try {
            ShortenUrlResponse response = urlShortenerService.shortenUrl(request.getLongUrl());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * GET /{shortUrl}
     * Retrieves the original long URL from a short URL
     */
    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortUrl) {
        String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        
        if (originalUrl != null) {
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", originalUrl).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /
     * Lists all URL mappings
     */
    @GetMapping
    public ResponseEntity<List<UrlMapping>> getAllMappings() {
        List<UrlMapping> mappings = urlShortenerService.getAllMappings();
        return ResponseEntity.ok(mappings);
    }
    
    /**
     * PUT /{shortUrl}
     * Updates the long URL mapping for an existing short URL
     */
    @PutMapping("/{shortUrl}")
    public ResponseEntity<Void> updateMapping(
            @PathVariable String shortUrl,
            @Valid @RequestBody UpdateUrlRequest request) {
        
        try {
            boolean updated = urlShortenerService.updateMapping(shortUrl, request.getNewLongUrl());
            
            if (updated) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /{shortUrl}/enable
     * Enables/disables the long URL mapping for an existing short URL
     */
    @PutMapping("/{shortUrl}/enable")
    public ResponseEntity<Void> updateMappingEnabled(
            @PathVariable String shortUrl,
            @Valid @RequestBody EnableUrlRequest request) {
        
        try {
            boolean updated = urlShortenerService.updateMappingEnabled(shortUrl, request.isEnabled());
            
            if (updated) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * GET /stats
     * Gets statistics about the URL shortener
     */
    @GetMapping("/stats")
    public ResponseEntity<Object> getStats() {
        return ResponseEntity.ok(Map.of(
            "totalMappings", urlShortenerService.getTotalMappings()
        ));
    }
    
    /**
     * GET /{shortUrl}/exists
     * Checks if a short URL exists
     */
    @GetMapping("/{shortUrl}/exists")
    public ResponseEntity<Object> checkShortUrlExists(@PathVariable String shortUrl) {
        boolean exists = urlShortenerService.hasShortUrl(shortUrl);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
    
    /**
     * Exception handler for validation errors
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
} 