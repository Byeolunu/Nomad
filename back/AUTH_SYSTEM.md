# Nomad Auth System - Complete Implementation

## Overview
The Nomad platform now has a complete JWT-based authentication system with role-based access control (RBAC).

## Architecture

### Components

1. **JwtUtil** (`security/JwtUtil.java`)
   - Generates JWT tokens with 10-hour expiration
   - Validates tokens and extracts claims
   - Supports role extraction from token

2. **JwtFilter** (`security/JwtFilter.java`)
   - Intercepts every request
   - Extracts JWT from Authorization header
   - Sets user authentication in SecurityContext

3. **CustomUserDetailsService** (`security/CustomUserDetailsService.java`)
   - Loads user details from database by email
   - Builds GrantedAuthorities based on user role

4. **ProjectSecurityConfig** (`config/ProjectSecurityConfig.java`)
   - Configures password encoding (BCrypt)
   - Sets up AuthenticationManager
   - Enables method-level security with @PreAuthorize
   - Configures CORS for Angular frontend

5. **AuthController** (`web/AuthController.java`)
   - POST /api/auth/register/freelancer - Register freelancer
   - POST /api/auth/register/recruiter - Register recruiter
   - POST /api/auth/login - Login and get JWT token
   - GET /api/auth/validate - Validate token

## DTOs

- **AuthRequest** - { email, password }
- **AuthResponse** - { token, role, userId, email, firstName, lastName }
- **RegisterFreelancerRequest** - Freelancer registration fields
- **RegisterRecruiterRequest** - Recruiter registration fields

## Role-Based Access Control

### Annotations

Use these annotations to protect controller methods:

```java
@RequireFreelancer  // Only freelancers can access
@RequireRecruiter   // Only recruiters can access
@RequireAdmin       // Only admins can access
```

### Example Usage

```java
@PostMapping("/apply")
@RequireFreelancer
public ResponseEntity<?> applyToMission(@RequestBody Application app) {
    // Only freelancers can call this
}

@PostMapping("/create")
@RequireRecruiter
public ResponseEntity<?> createMission(@RequestBody Mission mission) {
    // Only recruiters can call this
}
```

## API Endpoints

### Authentication

#### 1. Register Freelancer
```
POST /api/auth/register/freelancer
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "username": "johndoe",
  "password": "secure123",
  "phoneNumber": "+212612345678",
  "title": "Full Stack Developer",
  "summary": "5 years of experience",
  "hourlyRate": 25.50
}

Response: 201 Created
{
  "message": "Freelancer registered successfully",
  "data": 1,
  "timestamp": 1702406400000
}
```

#### 2. Register Recruiter
```
POST /api/auth/register/recruiter
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane@company.com",
  "username": "janesmith",
  "password": "secure123",
  "phoneNumber": "+212612345678",
  "companyName": "Tech Company",
  "companyWebsite": "https://company.com"
}

Response: 201 Created
{
  "message": "Recruiter registered successfully",
  "data": 2,
  "timestamp": 1702406400000
}
```

#### 3. Login
```
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "secure123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "role": "ROLE_FREELANCER",
  "userId": 1,
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### 4. Validate Token
```
GET /api/auth/validate
Authorization: Bearer <token>

Response: 200 OK
{
  "valid": "true",
  "email": "john@example.com"
}
```

## How It Works

### Registration Flow
1. User sends registration request with required fields
2. AuthController validates email/username uniqueness
3. Password is encoded with BCrypt
4. User is saved to database with appropriate role
5. Response returns success message and user ID

### Login Flow
1. User sends email and password
2. AuthenticationManager validates credentials
3. UserDetailsService loads user from database
4. JwtUtil generates JWT token with roles
5. Token is returned to client

### Authenticated Request Flow
1. Client sends request with `Authorization: Bearer <token>` header
2. JwtFilter intercepts request
3. Token is extracted and validated
4. User details are loaded and set in SecurityContext
5. Request proceeds to controller

## JWT Token Structure

Tokens include:
- Subject: user email
- Roles: list of user roles
- Issued at: token creation time
- Expiration: 10 hours from creation
- Signature: HMAC-SHA256 with secret key

## Configuration

In `application.properties`:
```properties
jwt.secret=nomadplatformSecretKey123456789012345678901234567890
jwt.expiration=36000000  # 10 hours in milliseconds
```

## Security Best Practices

1. ✅ Passwords are hashed with BCrypt
2. ✅ JWT tokens are signed with HMAC-SHA256
3. ✅ Tokens expire after 10 hours
4. ✅ CORS is configured for specific origin (localhost:4200)
5. ✅ CSRF is disabled (appropriate for JWT auth)
6. ✅ Authorization header is exposed in CORS config

## Testing the Auth System

### 1. Register as Freelancer
```bash
curl -X POST http://localhost:8090/api/auth/register/freelancer \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ahmed",
    "lastName": "Ali",
    "email": "ahmed@example.com",
    "username": "ahmed123",
    "password": "password123",
    "phoneNumber": "+212612345678",
    "title": "Web Developer",
    "summary": "3 years experience",
    "hourlyRate": 20.0
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "ahmed@example.com",
    "password": "password123"
  }'
```

### 3. Use Token in Protected Endpoint
```bash
curl -X GET http://localhost:8090/api/protected \
  -H "Authorization: Bearer <token_from_login>"
```

## Next Steps

1. Protect endpoints with @RequireFreelancer, @RequireRecruiter, @RequireAdmin
2. Implement messaging system
3. Add ratings system
4. Create admin endpoints
5. Build Angular authentication components

---

**Status**: ✅ Complete and Ready for Integration
