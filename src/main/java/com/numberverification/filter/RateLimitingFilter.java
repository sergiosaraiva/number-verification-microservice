package com.numberverification.filter;

import com.numberverification.config.RateLimitingConfig;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    
    private final Map<String, Bucket> ipRateLimitBuckets;
    private final RateLimitingConfig rateLimitingConfig;
    
    public RateLimitingFilter(Map<String, Bucket> ipRateLimitBuckets, RateLimitingConfig rateLimitingConfig) {
        this.ipRateLimitBuckets = ipRateLimitBuckets;
        this.rateLimitingConfig = rateLimitingConfig;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Skip rate limiting for non-API requests
        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Get client IP
        String clientIp = request.getRemoteAddr();
        
        // Get or create bucket for this IP
        Bucket bucket = ipRateLimitBuckets.computeIfAbsent(clientIp, k -> rateLimitingConfig.createNewBucket());
        
        // Try to consume a token
        if (bucket.tryConsume(1)) {
            // Token consumed successfully, proceed with the request
            filterChain.doFilter(request, response);
        } else {
            // Rate limit exceeded
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Rate limit exceeded. Please try again later.\"}");
        }
    }
}