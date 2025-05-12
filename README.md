# Number Verification Microservice

A Java-based microservice implementing the Number Verification CAMARA API. This API validates user identity by confirming ownership of the phone number being registered, matching it with the number identified by the operator through the user's device connection.

## Features

- **POST /verify** - Determines if the provided phone number matches the one currently in use by the user
- **GET /device-phone-number** - Identifies the phone number currently associated with the user's device
- Security measures including authentication, authorization, and rate limiting
- Comprehensive observability with logging, metrics, and health checks
- Ready for deployment on Railway platform
- Fully documented API using OpenAPI/Swagger

## Tech Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.2
- **Build Tool**: Gradle
- **Database**: MongoDB (for logging & auditing)
- **API Documentation**: OpenAPI 3.0 (Swagger)
- **Monitoring**: Micrometer with Prometheus
- **Containerization**: Docker
- **Deployment**: Railway
- **CI/CD**: GitHub Actions

## Prerequisites

- JDK 17 or higher
- Gradle 7.5+
- MongoDB (local or remote instance)
- Docker (for containerization)
- Railway CLI (for deployment)

## Getting Started

### Local Development

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/number-verification-service.git
   cd number-verification-service
   ```

2. Configure the application:
   Edit `src/main/resources/application.yml` to set your MongoDB connection and other properties.

3. Build the application:
   ```bash
   ./gradlew build
   ```

4. Run the application locally:
   ```bash
   ./gradlew bootRun
   ```

5. The service will be available at:
   - API: http://localhost:8080/api/v1
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/v3/api-docs

### Docker Build

1. Build the Docker image:
   ```bash
   ./gradlew jib
   ```

2. Or use traditional Docker build:
   ```bash
   docker build -t number-verification-service .
   ```

3. Run the container:
   ```bash
   docker run -p 8080:8080 number-verification-service
   ```

## Deployment to Railway

### Option 1: Manual Deployment

1. Install the Railway CLI:
   ```bash
   npm install -g @railway/cli
   ```

2. Login to Railway:
   ```bash
   railway login
   ```

3. Link your project:
   ```bash
   railway link
   ```

4. Deploy the application:
   ```bash
   railway up
   ```

### Option 2: GitHub Actions (CI/CD)

1. Set up your repository on GitHub

2. Add the `RAILWAY_TOKEN` secret to your GitHub repository settings

3. Push to the main branch to trigger automatic deployment

## API Documentation

### POST /verify

Verifies if the provided phone number matches the user's device.

**Request:**
```json
{
  "phoneNumber": "+34698765432",
  "correlationId": "abc-123-def-456"
}
```

**Response:**
```json
{
  "verificationId": "ver-12345",
  "status": "MATCH",
  "verificationTime": "2023-06-30T12:34:56Z"
}
```

### GET /device-phone-number

Retrieves the phone number associated with the user's device.

**Response:**
```json
{
  "phoneNumber": "+34698765432",
  "retrievalTime": "2023-06-30T12:34:56Z"
}
```

## Security

- Basic authentication for demo purposes
- OAuth 2.0 / JWT authentication for production
- Rate limiting to prevent abuse
- HTTPS encryption for all communications
- Phone number hashing for privacy
- Input validation to prevent injection attacks

## Observability

### Metrics

Metrics are available at `/actuator/prometheus` and include:
- Request count by endpoint
- Response times
- Error counts
- JVM metrics
- Custom business metrics

### Health Checks

Health information is available at `/actuator/health`

### Logging

- Structured JSON logging
- Correlation IDs for request tracing
- Log levels: DEBUG, INFO, WARN, ERROR
- PII redaction for sensitive data

## Demo

A demo script is provided to showcase the API functionality:

```bash
# Make the script executable
chmod +x demo.sh

# Run the demo
./demo.sh
```

## Testing

Run the tests with:
```bash
./gradlew test
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgements

- [CAMARA API Specifications](https://developers.opengateway.telefonica.com/docs/numberverification)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Railway](https://railway.app/) for deployment