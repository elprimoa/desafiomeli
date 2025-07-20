package com.example.urlshortener.service;

import com.example.urlshortener.dto.ShortenUrlResponse;
import com.example.urlshortener.dto.UrlMapping;
import com.example.urlshortener.entity.UrlMappingEntity;
import com.example.urlshortener.repository.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service layer for URL shortening operations
 */
@Service
public class UrlShortenerService {
    
    private final UrlMappingRepository urlMappingRepository;
    private final Random random;
    
    // Character set for generating short URLs (alphanumeric)
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    
    // URL validation pattern
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^https?://[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-.,@?^=%&:/~+#]*[\\w\\-@?^=%&/~+#])?$"
    );
    
    @Autowired
    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
        this.random = new Random();
    }
    
    /**
     * Shortens a long URL or returns existing short URL if already shortened
     * 
     * @param longUrl The long URL to shorten
     * @return Response containing short URL and whether it's new
     */
    public ShortenUrlResponse shortenUrl(String longUrl) {
        if (longUrl == null || longUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        
        // Normalize the URL
        longUrl = longUrl.trim();
        
        // Validate URL format
        if (!isValidURL(longUrl)) {
            throw new IllegalArgumentException("Invalid URL format: " + longUrl);
        }
        
        // Check if URL is already shortened
        Optional<UrlMappingEntity> existingMapping = urlMappingRepository.findByLongUrl(longUrl);
        boolean isNew = existingMapping.isEmpty();
        
        String shortUrl;
        if (isNew) {
            // Generate a unique short URL
            shortUrl = generateUniqueShortURL();
            
            // Save to database
            UrlMappingEntity newMapping = new UrlMappingEntity(shortUrl, longUrl, true);
            urlMappingRepository.save(newMapping);
        } else {
            shortUrl = existingMapping.get().getShortUrl();
        }
        
        return new ShortenUrlResponse(shortUrl, longUrl, isNew);
    }
    
    /**
     * Retrieves the original long URL from a short URL
     * 
     * @param shortUrl The short URL
     * @return The original long URL, or null if not found/disabled
     */
    public String getOriginalUrl(String shortUrl) {
        if (shortUrl == null || shortUrl.trim().isEmpty()) {
            return null;
        }
        
        Optional<UrlMappingEntity> mapping = urlMappingRepository.findByShortUrl(shortUrl.trim());
        if (mapping.isEmpty()) {
            return null;
        }
        
        UrlMappingEntity urlMapping = mapping.get();
        if (!urlMapping.isEnabled()) {
            return null;
        }
        
        return urlMapping.getLongUrl();
    }
    
    /**
     * Gets all URL mappings
     * 
     * @return List of URL mappings
     */
    public List<UrlMapping> getAllMappings() {
        List<UrlMappingEntity> mappings = urlMappingRepository.findAll();
        
        return mappings.stream()
                .map(mapping -> new UrlMapping(
                    mapping.getShortUrl(), 
                    mapping.getLongUrl(), 
                    mapping.isEnabled()
                ))
                .collect(Collectors.toList());
    }
    
    /**
     * Updates the long URL mapping for an existing short URL
     * 
     * @param shortUrl The existing short URL
     * @param newLongUrl The new long URL to map
     * @return true if update was successful, false if short URL doesn't exist
     */
    public boolean updateMapping(String shortUrl, String newLongUrl) {
        if (shortUrl == null || shortUrl.trim().isEmpty()) {
            return false;
        }
        
        if (newLongUrl == null || newLongUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("New URL cannot be null or empty");
        }
        
        // Normalize the new URL
        newLongUrl = newLongUrl.trim();
        
        // Validate the new URL format
        if (!isValidURL(newLongUrl)) {
            throw new IllegalArgumentException("Invalid URL format: " + newLongUrl);
        }
        
        Optional<UrlMappingEntity> existingMapping = urlMappingRepository.findByShortUrl(shortUrl.trim());
        if (existingMapping.isEmpty()) {
            return false;
        }
        
        UrlMappingEntity mapping = existingMapping.get();
        mapping.setLongUrl(newLongUrl);
        urlMappingRepository.save(mapping);
        
        return true;
    }

    /**
     * Enables/disables the long URL mapping for an existing short URL
     * 
     * @param shortUrl The existing short URL
     * @param isEnabled The new enabled status
     * @return true if update was successful, false if short URL doesn't exist
     */
    public boolean updateMappingEnabled(String shortUrl, boolean isEnabled) {
        if (shortUrl == null || shortUrl.trim().isEmpty()) {
            return false;
        }

        Optional<UrlMappingEntity> existingMapping = urlMappingRepository.findByShortUrl(shortUrl.trim());
        if (existingMapping.isEmpty()) {
            return false;
        }
        
        UrlMappingEntity mapping = existingMapping.get();
        mapping.setEnabled(isEnabled);
        urlMappingRepository.save(mapping);
        
        return true;
    }
    
    /**
     * Gets the total number of URL mappings
     * 
     * @return The total count
     */
    public long getTotalMappings() {
        return urlMappingRepository.count();
    }
    
    /**
     * Checks if a short URL exists
     * 
     * @param shortUrl The short URL to check
     * @return true if it exists, false otherwise
     */
    public boolean hasShortUrl(String shortUrl) {
        return shortUrl != null && urlMappingRepository.existsByShortUrl(shortUrl.trim());
    }
    
    /**
     * Generates a unique short URL that doesn't already exist in the database
     * 
     * @return A unique short URL
     */
    private String generateUniqueShortURL() {
        String shortUrl;
        int attempts = 0;
        final int MAX_ATTEMPTS = 100; // Prevent infinite loops
        
        do {
            shortUrl = generateRandomShortURL();
            attempts++;
            
            // If we've tried too many times, increase the length
            if (attempts > MAX_ATTEMPTS) {
                shortUrl = generateRandomShortURL(SHORT_URL_LENGTH + 1);
                attempts = 0;
            }
        } while (urlMappingRepository.existsByShortUrl(shortUrl));
        
        return shortUrl;
    }
    
    /**
     * Generates a random short URL of the default length
     * 
     * @return A random short URL
     */
    private String generateRandomShortURL() {
        return generateRandomShortURL(SHORT_URL_LENGTH);
    }
    
    /**
     * Generates a random short URL of specified length
     * 
     * @param length The length of the short URL to generate
     * @return A random short URL
     */
    private String generateRandomShortURL(int length) {
        StringBuilder shortUrl = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            shortUrl.append(CHARACTERS.charAt(randomIndex));
        }
        
        return shortUrl.toString();
    }
    
    /**
     * Validates if a URL has a proper format
     * 
     * @param url The URL to validate
     * @return true if the URL is valid, false otherwise
     */
    private boolean isValidURL(String url) {
        return URL_PATTERN.matcher(url).matches();
    }
} 