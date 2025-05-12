package com.numberverification.util;

import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class PhoneNumberUtils {
    
    /**
     * Hash a phone number for secure storage
     * @param phoneNumber The phone number to hash
     * @return A Base64 encoded SHA-256 hash of the phone number
     */
    public String hashPhoneNumber(String phoneNumber) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(phoneNumber.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash phone number", e);
        }
    }
    
    /**
     * Normalize a phone number to E.164 format
     * @param phoneNumber The phone number to normalize
     * @return The normalized phone number
     */
    public String normalizePhoneNumber(String phoneNumber) {
        // Remove all non-digit characters except the leading +
        return phoneNumber.replaceAll("[^\\d+]", "");
    }
}