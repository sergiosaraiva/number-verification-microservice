package com.numberverification.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.numberverification.model.VerificationStatus;

import java.time.Instant;

@Document(collection = "verification_logs")
public class VerificationLog {
    @Id
    private String id;
    private String correlationId;
    private String hashedPhoneNumber; // We hash phone numbers for privacy
    private VerificationStatus status;
    private String clientIp;
    private Instant timestamp;
    private String errorMessage;
    
    // Constructors, getters and setters
    public VerificationLog() {
    }
    
    // All args constructor
    public VerificationLog(String id, String correlationId, String hashedPhoneNumber, 
                           VerificationStatus status, String clientIp, Instant timestamp, String errorMessage) {
        this.id = id;
        this.correlationId = correlationId;
        this.hashedPhoneNumber = hashedPhoneNumber;
        this.status = status;
        this.clientIp = clientIp;
        this.timestamp = timestamp;
        this.errorMessage = errorMessage;
    }
    
    // Builder method for convenience
    public static VerificationLogBuilder builder() {
        return new VerificationLogBuilder();
    }
    
    // Builder class
    public static class VerificationLogBuilder {
        private String id;
        private String correlationId;
        private String hashedPhoneNumber;
        private VerificationStatus status;
        private String clientIp;
        private Instant timestamp;
        private String errorMessage;
        
        public VerificationLogBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        public VerificationLogBuilder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }
        
        public VerificationLogBuilder hashedPhoneNumber(String hashedPhoneNumber) {
            this.hashedPhoneNumber = hashedPhoneNumber;
            return this;
        }
        
        public VerificationLogBuilder status(VerificationStatus status) {
            this.status = status;
            return this;
        }
        
        public VerificationLogBuilder clientIp(String clientIp) {
            this.clientIp = clientIp;
            return this;
        }
        
        public VerificationLogBuilder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public VerificationLogBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }
        
        public VerificationLog build() {
            return new VerificationLog(id, correlationId, hashedPhoneNumber, status, clientIp, timestamp, errorMessage);
        }
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCorrelationId() {
        return correlationId;
    }
    
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
    
    public String getHashedPhoneNumber() {
        return hashedPhoneNumber;
    }
    
    public void setHashedPhoneNumber(String hashedPhoneNumber) {
        this.hashedPhoneNumber = hashedPhoneNumber;
    }
    
    public VerificationStatus getStatus() {
        return status;
    }
    
    public void setStatus(VerificationStatus status) {
        this.status = status;
    }
    
    public String getClientIp() {
        return clientIp;
    }
    
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}