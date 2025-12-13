# Authentication System Architecture & Flow Diagrams

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    NOMAD AUTH SYSTEM                             │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────┐
│   Angular App    │ (localhost:4200)
│  (Browser)       │
└────────┬─────────┘
         │ HTTP Request
         │
         ▼
┌──────────────────────────────────────────────────────────────────┐
│                    SPRING BOOT BACKEND                            │
│                    (localhost:8090)                               │
│                                                                    │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │              JWT FILTER (OncePerRequestFilter)            │   │
│  │  1. Extract "Authorization: Bearer <token>"              │   │
│  │  2. Validate JWT signature                               │   │
│  │  3. Load UserDetails from database                       │   │
│  │  4. Set Authentication in SecurityContext                │   │
│  └──────────────────────────────────────────────────────────┘   │
│           │                                                       │
│           ▼                                                       │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │           SPRING SECURITY FILTER CHAIN                    │   │
│  │  - CORS Filter (allow localhost:4200)                    │   │
│  │  - Authentication Filter                                 │   │
│  │  - Authorization Filter (@PreAuthorize)                  │   │
│  └──────────────────────────────────────────────────────────┘   │
│           │                                                       │
│           ▼                                                       │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │            CONTROLLER LAYER                               │   │
│  │  ┌────────────────────────────────────────────────────┐  │   │
│  │  │  AuthController                                    │  │   │
│  │  │  - /api/auth/register/freelancer (POST)           │  │   │
│  │  │  - /api/auth/register/recruiter (POST)            │  │   │
│  │  │  - /api/auth/login (POST)                         │  │   │
│  │  │  - /api/auth/validate (GET)                       │  │   │
│  │  └────────────────────────────────────────────────────┘  │   │
│  │                                                             │   │
│  │  Other Controllers (protected with @Require*)             │   │
│  │  - MissionController                                      │   │
│  │  - ApplicationController                                  │   │
│  │  - ProfileController                                      │   │
│  └──────────────────────────────────────────────────────────┘   │
│           │                                                       │
│           ▼                                                       │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │           SERVICE LAYER                                   │   │
│  │  - AuthenticationManager (validates credentials)         │   │
│  │  - JwtUtil (generates/validates tokens)                  │   │
│  │  - PasswordEncoder (BCrypt)                              │   │
│  │  - UserDetailsService (loads user from DB)              │   │
│  └──────────────────────────────────────────────────────────┘   │
│           │                                                       │
│           ▼                                                       │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │          DATA ACCESS LAYER                                │   │
│  │  - UserRepository (find/save users)                      │   │
│  │  - FreelancerRepository                                  │   │
│  │  - RecruiterRepository                                   │   │
│  └──────────────────────────────────────────────────────────┘   │
│           │                                                       │
│           ▼                                                       │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │        MYSQL DATABASE                                     │   │
│  │  - users table (base entity)                             │   │
│  │  - freelancers table (inherited)                         │   │
│  │  - recruiters table (inherited)                          │   │
│  └──────────────────────────────────────────────────────────┘   │
│                                                                    │
└──────────────────────────────────────────────────────────────────┘
         ▲
         │ HTTP Response
         │ { token, role, userId, ... }
         │
         ▼
┌──────────────────┐
│   Angular App    │ Store token in localStorage
└──────────────────┘ Use in Authorization header
```

---

## Registration Flow (Freelancer Example)

```
┌──────────────────────┐
│  Freelancer Client   │
│  (Angular Form)      │
└──────────┬───────────┘
           │
           │ POST /api/auth/register/freelancer
           │ {firstName, lastName, email, username, password, ...}
           │
           ▼
┌──────────────────────────────────────────┐
│    AuthController.registerFreelancer()   │
│  1. Validate email not exists            │
│  2. Validate username not exists         │
│  3. Encode password (BCrypt)             │
│  4. Create Freelancer entity             │
│  5. Set role = FREELANCER                │
└──────────┬───────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────┐
│  UserRepository.save(freelancer)         │
│  INSERT into users table                 │
│  INSERT into freelancers table           │
└──────────┬───────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────┐
│     Return Success Response              │
│  {                                       │
│    "message": "Registered successfully",  │
│    "data": 1,                            │
│    "timestamp": 1702406400000            │
│  }                                       │
└──────────┬───────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────┐
│   Store user info in Angular             │
│   Navigate to login page                 │
└──────────────────────────────────────────┘
```

---

## Login & Token Generation Flow

```
┌──────────────────────┐
│   User Login Form    │
│  (email, password)   │
└──────────┬───────────┘
           │
           │ POST /api/auth/login
           │ {email, password}
           │
           ▼
┌──────────────────────────────────────────┐
│     AuthController.login()               │
└──────────┬───────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────┐
│  AuthenticationManager.authenticate()    │
│  1. Load UserDetails by email            │
│  2. Compare password with hash           │
│  ✓ if passwords match                    │
└──────────┬───────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────┐
│   JwtUtil.generateToken(userDetails)    │
│                                          │
│  Header: {typ: "JWT", alg: "HS256"}     │
│                                          │
│  Payload: {                              │
│    sub: "user@email.com"                │
│    roles: ["ROLE_FREELANCER"]            │
│    iat: 1702406400                       │
│    exp: 1702442400 (10 hours later)     │
│  }                                       │
│                                          │
│  Signature: HMAC-SHA256(secret)          │
│                                          │
│  Result: "eyJhbGc..." (JWT Token)       │
└──────────┬───────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────┐
│     Return AuthResponse                  │
│  {                                       │
│    "token": "eyJhbGc...",                │
│    "role": "ROLE_FREELANCER",            │
│    "userId": 1,                          │
│    "email": "user@email.com",            │
│    "firstName": "Ahmed",                 │
│    "lastName": "Ali"                     │
│  }                                       │
└──────────┬───────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────┐
│   Angular Client                         │
│  1. Save token in localStorage           │
│  2. Save user info in service            │
│  3. Navigate to dashboard                │
│  4. Add Authorization header to requests │
└──────────────────────────────────────────┘
```

---

## Protected Request Flow

```
┌─────────────────────────┐
│   Angular Component     │
│  GET /api/missions      │
└────────┬────────────────┘
         │
         │ Add Authorization Header
         │ Authorization: Bearer eyJhbGc...
         │
         ▼
┌──────────────────────────────────────────┐
│     HTTP Request reaches server          │
└────────┬─────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────┐
│    JwtFilter.doFilterInternal()          │
│  1. Extract "Authorization: Bearer..."   │
│  2. Get token: "eyJhbGc..."             │
│  3. Extract username from token          │
└────────┬─────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────┐
│   JwtUtil.validateToken()                │
│  1. Verify signature (HMAC-SHA256)       │
│  2. Check expiration (not expired)       │
│  3. ✓ Valid                              │
└────────┬─────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────┐
│  UserDetailsService.loadUserByUsername() │
│  Load user from database by email        │
└────────┬─────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────┐
│  SecurityContext.setAuthentication()     │
│  Set UsernamePasswordAuthenticationToken │
│  with authorities: [ROLE_FREELANCER]    │
└────────┬─────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────┐
│    Controller Method                     │
│  @GetMapping("/api/missions")            │
│  public List<Mission> getMissions()      │
│  [No @Require annotation = PUBLIC]       │
│  ✓ Execute method                        │
└────────┬─────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────┐
│    Return Missions List (200 OK)         │
└────────┬─────────────────────────────────┘
         │
         ▼
┌─────────────────────────┐
│   Angular Client        │
│   Process response      │
│   Display missions      │
└─────────────────────────┘
```

---

## Protected Endpoint with Role Check

```
┌─────────────────────────┐
│   Angular Component     │
│  POST /api/missions     │ (Create mission)
│  + Authorization header │
└────────┬────────────────┘
         │
         │ Request authenticated
         │ (see Protected Request Flow above)
         │
         ▼
┌──────────────────────────────────────────┐
│    Controller Method                     │
│  @PostMapping("/api/missions")           │
│  @RequireRecruiter                       │
│  public Mission createMission()          │
└────────┬─────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────┐
│  Spring Security @PreAuthorize           │
│  Check: hasRole('RECRUITER')             │
└────────┬─────────────────────────────────┘
         │
         ├─► IF user has ROLE_RECRUITER
         │   │
         │   ▼
         │   ✓ Execute method
         │   Return 200 with created mission
         │
         └─► IF user has ROLE_FREELANCER
             │
             ▼
             ✗ 403 Forbidden
             {
               "timestamp": "...",
               "status": 403,
               "error": "Forbidden",
               "message": "Access Denied"
             }
```

---

## JWT Token Anatomy

```
┌────────────────────────────────────────────────────────────────┐
│          eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.               │
│          eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwicm9s...        │
│          SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c      │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │ HEADER (Base64 encoded JSON)                            │  │
│  │                                                          │  │
│  │ {                                                        │  │
│  │   "alg": "HS256",                                       │  │
│  │   "typ": "JWT"                                          │  │
│  │ }                                                        │  │
│  └─────────────────────────────────────────────────────────┘  │
│                          │                                      │
│                          ▼                                      │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │ PAYLOAD (Base64 encoded JSON)                           │  │
│  │                                                          │  │
│  │ {                                                        │  │
│  │   "sub": "john@example.com",                           │  │
│  │   "roles": ["ROLE_FREELANCER"],                         │  │
│  │   "iat": 1702406400,                                    │  │
│  │   "exp": 1702442400                                     │  │
│  │ }                                                        │  │
│  └─────────────────────────────────────────────────────────┘  │
│                          │                                      │
│                          ▼                                      │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │ SIGNATURE (HMAC-SHA256)                                 │  │
│  │                                                          │  │
│  │ HMACSHA256(                                             │  │
│  │   base64UrlEncode(header) + "." +                      │  │
│  │   base64UrlEncode(payload),                            │  │
│  │   "nomadplatformSecretKey..."                           │  │
│  │ )                                                        │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ✓ Only server knows the secret key                           │
│  ✓ Token cannot be modified without invalidating signature    │
│  ✓ Token can be read by client (not encrypted)                │
└────────────────────────────────────────────────────────────────┘
```

---

## Authorization Levels

```
┌──────────────────────────────────────────────────────────────┐
│                    PUBLIC ENDPOINTS                           │
│         (No authentication required)                          │
│                                                               │
│  GET /api/missions                                           │
│  GET /api/missions/{id}                                      │
│  GET /api/skills                                             │
│  GET /api/profiles/{id}                                      │
│  POST /api/auth/register/freelancer                          │
│  POST /api/auth/register/recruiter                           │
│  POST /api/auth/login                                        │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│              AUTHENTICATED ENDPOINTS                          │
│         (Valid JWT token required)                           │
│                                                               │
│  GET /api/auth/validate                                      │
│  [other protected endpoints]                                 │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│           FREELANCER-ONLY ENDPOINTS                           │
│    (@RequireFreelancer + valid JWT with ROLE_FREELANCER)    │
│                                                               │
│  POST /api/applications/apply                                │
│  GET /api/applications/freelancer/{id}                       │
│  PUT /api/profiles/{id}                                      │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│            RECRUITER-ONLY ENDPOINTS                           │
│    (@RequireRecruiter + valid JWT with ROLE_RECRUITER)      │
│                                                               │
│  POST /api/missions                                          │
│  GET /api/missions/recruiter/{id}                            │
│  GET /api/applications/mission/{id}                          │
│  PUT /api/applications/{id}/status                           │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│              ADMIN-ONLY ENDPOINTS                             │
│    (@RequireAdmin + valid JWT with ROLE_ADMIN)              │
│                                                               │
│  GET /api/admin/users                                        │
│  DELETE /api/admin/users/{id}                                │
│  DELETE /api/admin/missions/{id}                             │
└──────────────────────────────────────────────────────────────┘
```

---

## Key Classes & Relationships

```
┌─────────────────────┐
│   User (Abstract)   │
│  ────────────────   │
│  - id               │
│  - firstName        │
│  - lastName         │
│  - email            │
│  - username         │
│  - password         │
│  - role             │
│  - createdAt        │
└──────────┬──────────┘
           │
           ├─ inherits ─────────┬──────────────────┐
           │                    │                   │
           ▼                    ▼                   ▼
    ┌─────────────┐      ┌─────────────┐    ┌──────────────┐
    │ Freelancer  │      │ Recruiter   │    │    Admin     │
    │ ─────────── │      │ ─────────── │    │ ──────────── │
    │ - title     │      │ - company   │    │ (no extras)  │
    │ - summary   │      │ - website   │    │              │
    │ - hourlyRate│      │ - missions  │    │              │
    │ - skills    │      │             │    │              │
    │ - profile   │      │             │    │              │
    │ - apps      │      │             │    │              │
    └─────────────┘      └─────────────┘    └──────────────┘
          │                    │
          ├─ applies to ───────┤
          │                    │
          │            ┌───────▼──────────┐
          │            │     Mission      │
          │            │  ─────────────  │
          │            │ - title          │
          │            │ - description    │
          │            │ - budget         │
          │            │ - deadline       │
          │            │ - status         │
          │            │ - recruiter ─────┼─→ Recruiter
          │            │ - applications ──┼─→ Application[]
          │            │ - skills ────────┼─→ Skill[]
          │            └──────────────────┘
          │                    ▲
          │                    │
          └───────────────────────────┘

┌──────────────────────────────────┐
│      Application                 │
│  ────────────────────────────────│
│  - id                            │
│  - freelancer ───────────────────┼─→ Freelancer
│  - mission ──────────────────────┼─→ Mission
│  - coverLetter                   │
│  - proposedBudget                │
│  - status (PENDING/ACCEPTED/...) │
│  - appliedDate                   │
│  - estimatedDays                 │
└──────────────────────────────────┘

┌──────────────────────────┐
│      Skill               │
│  ────────────────────────│
│  - id                    │
│  - name                  │
│  - category              │
│  - freelancers ──────────┼─→ Freelancer[]
│  - missions ─────────────┼─→ Mission[]
└──────────────────────────┘
```

---

**All diagrams created to help visualize the complete authentication flow and system architecture!**
