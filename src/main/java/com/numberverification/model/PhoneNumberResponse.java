package com.numberverification.model;

import java.time.Instant;

public class PhoneNumberResponse {
    private String phoneNumber;
    private Instant retrievalTime;
    
    // Constructors, getters and setters
    public PhoneNumberResponse() {
    }
    
    public PhoneNumberResponse(String phoneNumber, Instant retrievalTime) {
        this.phoneNumber = phoneNumber;
        this.retrievalTime = retrievalTime;
    }
    
    // Getters and setters
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Instant getRetrievalTime() {
        return retrievalTime;
    }
    
    public void setRetrievalTime(Instant retrievalTime) {
        this.retrievalTime = retrievalTime;
    }
}
