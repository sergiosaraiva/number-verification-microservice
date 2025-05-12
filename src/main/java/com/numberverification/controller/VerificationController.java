package com.numberverification.controller;

import com.numberverification.model.PhoneNumberResponse;
import com.numberverification.model.VerificationRequest;
import com.numberverification.model.VerificationResponse;
import com.numberverification.service.VerificationService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Number Verification", description = "Number Verification CAMARA API")
public class VerificationController {
    private static final Logger log = LoggerFactory.getLogger(VerificationController.class);
    
    private final VerificationService verificationService;
    
    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }
    
    @PostMapping("/verify")
    @Operation(
        summary = "Verify phone number",
        description = "Determines if the provided phone number matches the one currently in use by the user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Verification processed", 
                         content = @Content(schema = @Schema(implementation = VerificationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid phone number format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "429", description = "Too many requests")
        }
    )
    @Timed(value = "verification.time", description = "Time taken to process verification")
    public ResponseEntity<VerificationResponse> verifyPhoneNumber(@Valid @RequestBody VerificationRequest request) {
        log.info("Received verification request");
        VerificationResponse response = verificationService.verifyPhoneNumber(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/device-phone-number")
    @Operation(
        summary = "Get device phone number",
        description = "Identifies the phone number currently associated with the user's device",
        responses = {
            @ApiResponse(responseCode = "200", description = "Phone number retrieved", 
                         content = @Content(schema = @Schema(implementation = PhoneNumberResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "429", description = "Too many requests")
        }
    )
    @Timed(value = "phone.number.retrieval.time", description = "Time taken to retrieve phone number")
    public ResponseEntity<PhoneNumberResponse> getDevicePhoneNumber() {
        log.info("Received device phone number request");
        PhoneNumberResponse response = verificationService.getDevicePhoneNumber();
        return ResponseEntity.ok(response);
    }
}