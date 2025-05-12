package com.numberverification.service;

import com.numberverification.client.TelecomProviderClient;
import com.numberverification.model.PhoneNumberResponse;
import com.numberverification.model.VerificationRequest;
import com.numberverification.model.VerificationResponse;
import com.numberverification.model.VerificationStatus;
import com.numberverification.repository.VerificationLogRepository;
import com.numberverification.repository.entity.VerificationLog;
import com.numberverification.util.PhoneNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.UUID;

@Service
public class VerificationService {
    private static final Logger log = LoggerFactory.getLogger(VerificationService.class);
    
    private final TelecomProviderClient telecomProviderClient;
    private final VerificationLogRepository verificationLogRepository;
    private final PhoneNumberUtils phoneNumberUtils;
    
    public VerificationService(TelecomProviderClient telecomProviderClient, 
                              VerificationLogRepository verificationLogRepository,
                              PhoneNumberUtils phoneNumberUtils) {
        this.telecomProviderClient = telecomProviderClient;
        this.verificationLogRepository = verificationLogRepository;
        this.phoneNumberUtils = phoneNumberUtils;
    }
    
    public VerificationResponse verifyPhoneNumber(VerificationRequest request) {
        log.info("Processing verification request with correlationId: {}", request.getCorrelationId());
        
        String verificationId = UUID.randomUUID().toString();
        Instant verificationTime = Instant.now();
        
        // Call telecom provider to verify phone number
        boolean matches = telecomProviderClient.verifyPhoneNumberMatch(request.getPhoneNumber()).block();
        VerificationStatus status = matches ? VerificationStatus.MATCH : VerificationStatus.MISMATCH;
        
        // Log the verification attempt
        String clientIp = extractClientIp();
        String hashedPhoneNumber = phoneNumberUtils.hashPhoneNumber(request.getPhoneNumber());
        
        VerificationLog verificationLog = VerificationLog.builder()
            .correlationId(request.getCorrelationId())
            .hashedPhoneNumber(hashedPhoneNumber)
            .status(status)
            .clientIp(clientIp)
            .timestamp(verificationTime)
            .build();
        
        verificationLogRepository.save(verificationLog);
        
        // Return response
        return new VerificationResponse(verificationId, status, verificationTime);
    }
    
    public PhoneNumberResponse getDevicePhoneNumber() {
        log.info("Retrieving device phone number");
        
        // Get phone number from telecom provider
        String phoneNumber = telecomProviderClient.getDevicePhoneNumber().block();
        Instant retrievalTime = Instant.now();
        
        // Log the retrieval attempt (optional, for audit purposes)
        String clientIp = extractClientIp();
        String hashedPhoneNumber = phoneNumberUtils.hashPhoneNumber(phoneNumber);
        
        VerificationLog verificationLog = VerificationLog.builder()
            .correlationId(UUID.randomUUID().toString())
            .hashedPhoneNumber(hashedPhoneNumber)
            .status(VerificationStatus.MATCH) // Always MATCH for direct retrieval
            .clientIp(clientIp)
            .timestamp(retrievalTime)
            .build();
        
        verificationLogRepository.save(verificationLog);
        
        // Return response
        return new PhoneNumberResponse(phoneNumber, retrievalTime);
    }
    
    private String extractClientIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest().getRemoteAddr();
            }
        } catch (Exception e) {
            log.warn("Could not extract client IP", e);
        }
        return "unknown";
    }
}