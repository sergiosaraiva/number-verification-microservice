# Number Verification Microservice - Technical Design Document

## 1. Introduction

This document outlines the detailed technical design for a Java-based microservice implementing the Number Verification CAMARA API. The microservice will provide two primary endpoints as specified in the requirements: POST /verify and GET /device-phone-number.

## 2. Architecture Overview

### 2.1 System Context

```
┌─────────────────┐      ┌─────────────────────┐      ┌───────────────────┐
│                 │      │                     │      │                   │
│  Client         │ ──── │  Number             │ ──── │  Telecom Network  │
│  Applications   │      │  Verification       │      │  Provider APIs    │
│                 │      │  Microservice       │      │                   │
└─────────────────┘      └─────────────────────┘      └───────────────────┘
                                   │
                                   │
                          ┌────────┴────────┐
                          │                 │
                          │  Data Store     │
                          │  (MongoDB)      │
                          │                 │
                          └─────────────────┘
```

### 2.2 Component Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    Number Verification Microservice              │
│                                                                 │
│  ┌───────────────┐    ┌───────────────┐    ┌───────────────┐    │
│  │               │    │               │    │               │    │
│  │  API Layer    │───>│ Service Layer │───>│Integration    │    │
│  │  Controllers  │    │ Business Logic│    │Layer          │    │
│  │               │    │               │    │               │    │
│  └───────────────┘    └───────────────┘    └───────┬───────┘    │
│          ▲                    │                    │             │
│          │                    │                    ▼             │
│  ┌───────┴───────┐    ┌───────┴───────┐    ┌───────────────┐    │
│  │               │    │               │    │               │    │
│  │ Security      │    │ Persistence   │<───│ External API  │    │
│  │ Layer         │    │ Layer         │    │ Clients       │    │
│  │               │    │               │    │               │    │
│  └───────────────┘    └───────────────┘    └───────────────┘    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## 3. Technology Stack

### 3.1 Core Technologies

| Component | Technology | Version | Rationale |
|-----------|------------|---------|-----------|
| Programming Language | Java | 17 LTS | Long-term support, modern features |
| Framework | Spring Boot | 3.2.x | Industry standard, comprehensive ecosystem |
| Build Tool | Gradle | 8.4 | Flexible, performance-focused |
| API Documentation | SpringDoc (OpenAPI) | 2.3.0 | Auto-generated API docs |
| Database | MongoDB | 6.0 | Flexible schema, good for audit logs |
| Containerization | Docker | - | Industry standard, portable |

### 3.2 Supporting Libraries

| Library | Purpose |
|---------|---------|
| Spring WebFlux | Reactive web framework |
| Spring Security | Authentication and authorization |
| Spring Data MongoDB | Database access |
| Resilience4j | Circuit breaking, rate limiting |
| Micrometer | Metrics collection |
| Logback + ELK | Logging infrastructure |
| Mockito & JUnit 5 | Testing |
| MapStruct | Object mapping |

## 4. API Specification

### 4.1 POST /verify

Validates if the provided phone number matches the one associated with the user's device.

#### Request

```json
{
  "phoneNumber": "+34698765432",
  "correlationId": "abc-123-def-456"
}
```

#### Response

```json
{
  "verificationId": "ver-12345",
  "status": "MATCH",
  "verificationTime": "2023-06-30T12:34:56Z"
}
```

#### Status Codes

- 200 OK: Verification processed
- 400 Bad Request: Invalid phone number format
- 401 Unauthorized: Missing or invalid authentication
- 403 Forbidden: Insufficient permissions
- 429 Too Many Requests: Rate limit exceeded
- 500 Internal Server Error: Server-side issue

### 4.2 GET /device-phone-number

Retrieves the phone number associated with the user's device.

#### Response

```json
{
  "phoneNumber": "+34698765432",
  "retrievalTime": "2023-06-30T12:34:56Z"
}
```

#### Status Codes

- 200 OK: Phone number retrieved successfully
- 401 Unauthorized: Missing or invalid authentication
- 403 Forbidden: Insufficient permissions
- 429 Too Many Requests: Rate limit exceeded
- 500 Internal Server Error: Server-side issue

## 5. Data Model

### 5.1 Core Domain Models

#### VerificationRequest
```java
public class VerificationRequest {
    private String phoneNumber;
    private String correlationId;
}
```

#### VerificationResponse
```java
public class VerificationResponse {
    private String verificationId;
    private VerificationStatus status; // Enum: MATCH, MISMATCH, ERROR
    private Instant verificationTime;
}
```

#### PhoneNumberResponse
```java
public class PhoneNumberResponse {
    private String phoneNumber;
    private Instant retrievalTime;
}
```

### 5.2 Persistence Models

#### VerificationLog
```java
@Document(collection = "verification_logs")
public class VerificationLog {
    @Id
    private String id;
    private String correlationId;
    private String hashedPhoneNumber; // Never store raw phone numbers
    private VerificationStatus status;
    private String clientIp;
    private Instant timestamp;
    private String errorMessage;
}
```

## 6. Component Design

### 6.1 API Layer (Controllers)

```java
@RestController
@RequestMapping("/api/v1")
public class VerificationController {
    
    private final VerificationService verificationService;
    
    @PostMapping("/verify")
    public ResponseEntity<VerificationResponse> verifyPhoneNumber(@RequestBody VerificationRequest request) {
        // Implementation
    }
    
    @GetMapping("/device-phone-number")
    public ResponseEntity<PhoneNumberResponse> getDevicePhoneNumber() {
        // Implementation
    }
}
```

### 6.2 Service Layer

```java
@Service
public class VerificationService {
    
    private final TelecomProviderClient telecomProviderClient;
    private final VerificationLogRepository verificationLogRepository;
    
    public VerificationResponse verifyPhoneNumber(VerificationRequest request) {
        // Business logic implementation
    }
    
    public PhoneNumberResponse getDevicePhoneNumber() {
        // Business logic implementation
    }
}
```

### 6.3 Integration Layer

```java
@Component
public class TelecomProviderClient {
    
    private final WebClient webClient;
    
    public Mono<Boolean> verifyPhoneNumberMatch(String phoneNumber) {
        // Implementation to call telecom provider API
    }
    
    public Mono<String> getDevicePhoneNumber() {
        // Implementation to call telecom provider API
    }
}
```

### 6.4 Security Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/v1/**").authenticated()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        
        return http.build();
    }
    
    // Additional security beans
}
```

## 7. Observability & Monitoring

### 7.1 Logging Strategy

- Use structured JSON logging format
- Include correlation IDs in all logs
- Implement PII redaction for sensitive data
- Log levels:
  - DEBUG: Detailed information for debugging
  - INFO: Normal operation events
  - WARN: Potentially harmful situations
  - ERROR: Error events that might still allow the application to continue

```java
@Slf4j
public class LoggingAspect {
    
    @Around("execution(* com.numberverification.controller.*.*(..))")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        // Implementation
    }
}
```

### 7.2 Metrics Collection

Collect the following metrics using Micrometer:

- Request count by endpoint
- Request duration
- Error count by type
- Success/failure ratio for verifications
- System resource utilization

```java
@Configuration
public class MetricsConfig {
    
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "number-verification");
    }
}
```

### 7.3 Health Checks

```java
@Component
public class TelecomProviderHealthIndicator implements HealthIndicator {
    
    private final TelecomProviderClient client;
    
    @Override
    public Health health() {
        // Implementation
    }
}
```

## 8. Deployment & Infrastructure

### 8.1 Containerization

- Dockerfile for microservice
- Multi-stage build for optimized image size
- Port configuration: 8080
- Health check endpoints

### 8.2 Railway Deployment

- Railway project configuration
- Environment variables for configuration
- CI/CD integration via GitHub Actions

### 8.3 Scaling Strategy

- Horizontal pod autoscaling based on CPU/memory
- Consider Redis for caching and rate limiting at scale
- Use of circuit breakers for external APIs

## 9. Testing Strategy

### 9.1 Test Types

- Unit tests for business logic (80%+ coverage)
- Integration tests for database and external APIs
- Contract tests for API endpoints
- Performance tests for load handling

### 9.2 Test Implementation

```java
@SpringBootTest
public class VerificationServiceIntegrationTest {
    
    @MockBean
    private TelecomProviderClient telecomProviderClient;
    
    @Autowired
    private VerificationService verificationService;
    
    @Test
    public void testVerifyPhoneNumber_whenMatches_thenReturnsMatchStatus() {
        // Test implementation
    }
}
```

## 10. Implementation Plan

### 10.1 Phase 1: Core API Implementation
- Set up project structure
- Implement basic controllers and services
- Configure MongoDB integration
- Create mock telecom provider client

### 10.2 Phase 2: Security & Observability
- Implement security configuration
- Set up logging and metrics collection
- Add error handling and resilience patterns

### 10.3 Phase 3: Testing & Deployment
- Write comprehensive tests
- Set up CI/CD pipeline
- Configure Railway deployment
- Document API and operations

## 11. Appendix

### 11.1 Security Considerations
- API throttling to prevent abuse
- IP-based rate limiting
- Phone number tokenization 
- Audit logging for all verification attempts
- Secret management for API keys and credentials

### 11.2 Performance Targets
- API response time: < 300ms (p95)
- Throughput: 100+ requests/second
- Availability: 99.9%

### 11.3 Compliance Requirements
- GDPR compliance for European deployments
- Telecom regulatory compliance
- Data retention policies (log data maximum 30 days)
