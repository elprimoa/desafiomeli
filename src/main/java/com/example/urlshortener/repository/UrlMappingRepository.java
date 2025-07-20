package com.example.urlshortener.repository;

import com.example.urlshortener.entity.UrlMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMappingEntity, Long> {
    
    /**
     * Find URL mapping by short URL
     */
    Optional<UrlMappingEntity> findByShortUrl(String shortUrl);
    
    /**
     * Find URL mapping by long URL
     */
    Optional<UrlMappingEntity> findByLongUrl(String longUrl);
    
    /**
     * Check if a short URL exists
     */
    boolean existsByShortUrl(String shortUrl);
    
    /**
     * Check if a long URL exists
     */
    boolean existsByLongUrl(String longUrl);
    
    /**
     * Find all enabled URL mappings
     */
    List<UrlMappingEntity> findByIsEnabledTrue();
    
    /**
     * Find all disabled URL mappings
     */
    List<UrlMappingEntity> findByIsEnabledFalse();
    
    /**
     * Count total URL mappings
     */
    long count();
    
    /**
     * Count enabled URL mappings
     */
    long countByIsEnabledTrue();
    
    /**
     * Count disabled URL mappings
     */
    long countByIsEnabledFalse();
    
    /**
     * Find URL mappings by partial short URL match
     */
    @Query("SELECT u FROM UrlMappingEntity u WHERE u.shortUrl LIKE %:partialShortUrl%")
    List<UrlMappingEntity> findByPartialShortUrl(@Param("partialShortUrl") String partialShortUrl);
    
    /**
     * Find URL mappings by partial long URL match
     */
    @Query("SELECT u FROM UrlMappingEntity u WHERE u.longUrl LIKE %:partialLongUrl%")
    List<UrlMappingEntity> findByPartialLongUrl(@Param("partialLongUrl") String partialLongUrl);
} 