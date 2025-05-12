#!/bin/bash
# Number Verification API Demo Script

# Colors for better readability
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# API Base URL (update this with your deployed Railway URL)
API_BASE_URL="https://number-verification-service-production.up.railway.app"
# For local testing, use:
# API_BASE_URL="http://localhost:8080"

# For demo purposes, we'll use basic auth
USERNAME="demo"
PASSWORD="demo123"

# Function to make API requests with basic auth
function make_request() {
    local method="$1"
    local endpoint="$2"
    local data="$3"
    
    echo -e "${YELLOW}Making ${method} request to ${endpoint}${NC}"
    
    if [ "$method" == "GET" ]; then
        curl -s -X GET \
             -u "${USERNAME}:${PASSWORD}" \
             -H "Content-Type: application/json" \
             "${API_BASE_URL}${endpoint}"
    else
        curl -s -X ${method} \
             -u "${USERNAME}:${PASSWORD}" \
             -H "Content-Type: application/json" \
             -d "${data}" \
             "${API_BASE_URL}${endpoint}"
    fi
    
    echo -e "\n"
}

# Function to display section headers
function display_header() {
    echo -e "\n${BLUE}===============================================${NC}"
    echo -e "${BLUE}  $1${NC}"
    echo -e "${BLUE}===============================================${NC}\n"
}

# Clear the terminal
clear

# Intro
display_header "Number Verification API Demo"
echo -e "This script demonstrates the functionality of the Number Verification API."
echo -e "API Base URL: ${GREEN}${API_BASE_URL}${NC}"
echo -e "Press Enter to continue..."
read

# 1. Verify a phone number (Match case)
display_header "1. Verify Phone Number - Match Case"
echo -e "Sending a verification request with phone number: ${GREEN}+34698765432${NC}"
echo -e "This should match the device's phone number in our demo environment."

VERIFICATION_REQUEST='{
  "phoneNumber": "+34698765432",
  "correlationId": "demo-123-456"
}'

echo -e "${YELLOW}Request:${NC}"
echo "$VERIFICATION_REQUEST"

VERIFICATION_RESPONSE=$(make_request "POST" "/api/v1/verify" "$VERIFICATION_REQUEST")
echo -e "${GREEN}Response:${NC}"
echo "$VERIFICATION_RESPONSE" | jq .

echo -e "Press Enter to continue..."
read

# 2. Verify a phone number (Mismatch case)
display_header "2. Verify Phone Number - Mismatch Case"
echo -e "Sending a verification request with phone number: ${RED}+34611111111${NC}"
echo -e "This should NOT match the device's phone number in our demo environment."

VERIFICATION_REQUEST_MISMATCH='{
  "phoneNumber": "+34611111111",
  "correlationId": "demo-789-012"
}'

echo -e "${YELLOW}Request:${NC}"
echo "$VERIFICATION_REQUEST_MISMATCH"

VERIFICATION_RESPONSE_MISMATCH=$(make_request "POST" "/api/v1/verify" "$VERIFICATION_REQUEST_MISMATCH")
echo -e "${GREEN}Response:${NC}"
echo "$VERIFICATION_RESPONSE_MISMATCH" | jq .

echo -e "Press Enter to continue..."
read

# 3. Get device phone number
display_header "3. Get Device Phone Number"
echo -e "Retrieving the phone number associated with the user's device."

PHONE_NUMBER_RESPONSE=$(make_request "GET" "/api/v1/device-phone-number" "")
echo -e "${GREEN}Response:${NC}"
echo "$PHONE_NUMBER_RESPONSE" | jq .

echo -e "Press Enter to continue..."
read

# 4. Invalid phone number format
display_header "4. Invalid Phone Number Format"
echo -e "Sending a verification request with an invalid phone number format: ${RED}1234${NC}"

INVALID_REQUEST='{
  "phoneNumber": "1234",
  "correlationId": "demo-invalid-123"
}'

echo -e "${YELLOW}Request:${NC}"
echo "$INVALID_REQUEST"

INVALID_RESPONSE=$(make_request "POST" "/api/v1/verify" "$INVALID_REQUEST")
echo -e "${GREEN}Response:${NC}"
echo "$INVALID_RESPONSE" | jq .

echo -e "Press Enter to continue..."
read

# 5. Rate limiting demonstration
display_header "5. Rate Limiting Demonstration"
echo -e "Making multiple requests in quick succession to demonstrate rate limiting."
echo -e "Our API is configured to allow 10 requests per minute per IP address."

for i in {1..12}; do
    echo -e "\n${YELLOW}Request ${i}:${NC}"
    RATE_LIMIT_RESPONSE=$(make_request "GET" "/api/v1/device-phone-number" "")
    echo -e "${GREEN}Response:${NC}"
    echo "$RATE_LIMIT_RESPONSE" | jq . || echo "$RATE_LIMIT_RESPONSE"
    
    # Small delay to see the responses
    sleep 1
done

echo -e "Press Enter to continue..."
read

# 6. Check API health
display_header "6. API Health Check"
echo -e "Checking the health of the API."

HEALTH_RESPONSE=$(curl -s "${API_BASE_URL}/actuator/health")
echo -e "${GREEN}Response:${NC}"
echo "$HEALTH_RESPONSE" | jq .

echo -e "\n${BLUE}===============================================${NC}"
echo -e "${BLUE}  Demo Complete!${NC}"
echo -e "${BLUE}===============================================${NC}\n"

echo -e "This concludes the demo of the Number Verification API."
echo -e "You can explore more using the Swagger UI at: ${GREEN}${API_BASE_URL}/swagger-ui.html${NC}"