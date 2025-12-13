# Quick Reference - Nomad Auth System

## 🚀 Quick Start Commands

### Build & Run Backend
```bash
cd Nomad/back
mvn clean install
mvn spring-boot:run
# Backend runs on http://localhost:8090
```

---

## 📝 Quick API Reference

### 1. Register Freelancer
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

### 2. Register Recruiter
```bash
curl -X POST http://localhost:8090/api/auth/register/recruiter \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane@company.com",
    "username": "janesmith",
    "password": "password123",
    "phoneNumber": "+212612345678",
    "companyName": "Tech Company",
    "companyWebsite": "https://company.com"
  }'
```

### 3. Login
```bash
curl -X POST http://localhost:8090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "ahmed@example.com",
    "password": "password123"
  }'

# Response:
# {
#   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#   "role": "ROLE_FREELANCER",
#   "userId": 1,
#   "email": "ahmed@example.com",
#   "firstName": "Ahmed",
#   "lastName": "Ali"
# }
```

### 4. Validate Token
```bash
curl -X GET http://localhost:8090/api/auth/validate \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# Response: 200 OK
# {
#   "valid": "true",
#   "email": "ahmed@example.com"
# }
```

---

## 🔒 Using Token in Protected Endpoints

### Set token in bash variable
```bash
TOKEN=$(curl -X POST http://localhost:8090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ahmed@example.com","password":"password123"}' \
  | grep -o '"token":"[^"]*' | cut -d'"' -f4)

echo "Token: $TOKEN"
```

### Use token in request
```bash
curl -X GET http://localhost:8090/api/protected \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🛡️ Protecting Endpoints in Code

### Freelancer-Only Endpoint
```java
@PostMapping("/apply")
@RequireFreelancer
public ResponseEntity<Application> applyToMission(@RequestBody Application app) {
    // Only freelancers can call this
}
```

### Recruiter-Only Endpoint
```java
@PostMapping("/create")
@RequireRecruiter
public ResponseEntity<Mission> createMission(@RequestBody Mission mission) {
    // Only recruiters can call this
}
```

### Admin-Only Endpoint
```java
@DeleteMapping("/users/{id}")
@RequireAdmin
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    // Only admins can call this
}
```

### Public Endpoint
```java
@GetMapping("/missions")
public ResponseEntity<List<Mission>> getMissions() {
    // No annotation = public access
}
```

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `AUTH_SYSTEM.md` | Technical documentation & API specs |
| `IMPLEMENTATION_SUMMARY.md` | What was built and why |
| `ENDPOINT_PROTECTION_GUIDE.md` | How to protect endpoints |
| `AUTH_FLOW_DIAGRAMS.md` | Visual flow diagrams |
| `AUTH_SYSTEM_COMPLETE.md` | Final summary |
| `COMPLETION_CHECKLIST.md` | What's been completed |
| `AUTH_QUICK_REFERENCE.md` | This file |

---

## 🔑 Key Configuration

### JWT Settings (application.properties)
```properties
jwt.secret=nomadplatformSecretKey123456789012345678901234567890
jwt.expiration=36000000  # 10 hours in milliseconds
```

### CORS Settings (ProjectSecurityConfig)
```java
configuration.setAllowedOrigins(List.of("http://localhost:4200"));
configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
```

---

## 🧪 Postman Collection

### Step 1: Register
```
POST http://localhost:8090/api/auth/register/freelancer
Body: {
  "firstName": "Test",
  "lastName": "User",
  "email": "test@example.com",
  "username": "testuser",
  "password": "test123",
  "phoneNumber": "+212612345678",
  "title": "Developer",
  "summary": "Test",
  "hourlyRate": 25
}
```

### Step 2: Login
```
POST http://localhost:8090/api/auth/login
Body: {
  "email": "test@example.com",
  "password": "test123"
}
```

### Step 3: Save Token
Click `Tests` tab and add:
```javascript
var jsonData = pm.response.json();
pm.environment.set("token", jsonData.token);
```

### Step 4: Use Token
In any protected endpoint, add Header:
```
Authorization: Bearer {{token}}
```

---

## ❌ Common Errors & Solutions

### Error: "Cannot find module '@angular/router'"
**Solution**: Run `npm install` in the front directory
```bash
cd Nomad/front
npm install
```

### Error: "Invalid email or password"
**Solution**: 
- Check email exists in database
- Check password matches (passwords are hashed)
- Use the exact email from registration

### Error: "Token expired or invalid"
**Solution**:
- Token expires after 10 hours
- Login again to get a new token
- Include `Authorization: Bearer <token>` header

### Error: "Access Denied" (403 Forbidden)
**Solution**:
- Check you have correct role
- Freelancers can't access @RequireRecruiter endpoints
- Recruiters can't access @RequireFreelancer endpoints
- Admin role may not exist yet (create through DB)

### Error: "CORS policy: No 'Access-Control-Allow-Origin' header"
**Solution**:
- Angular must run on http://localhost:4200
- Check CORS configuration in ProjectSecurityConfig
- Restart backend after changing CORS

---

## 🔄 Token Flow Summary

```
Browser (Angular)
    ↓
    ├─→ POST /api/auth/register (public)
    │
    ├─→ POST /api/auth/login (public)
    │   ↓
    │   Store: { token, userId, role, ... }
    │
    └─→ GET /api/protected (protected)
        ├─ Add header: "Authorization: Bearer token"
        ↓
        Server
        ├─ JwtFilter extracts and validates token
        ├─ Loads user from database
        ├─ Sets SecurityContext
        ├─ Checks @RequireFreelancer/@RequireRecruiter
        ↓
        ├─ If authorized: Execute controller
        └─ If denied: Return 403 Forbidden
```

---

## 🚦 Access Control Summary

| Role | Can Do |
|------|--------|
| **Public** | View missions, profiles, register, login |
| **Freelancer** | View missions, apply, view own applications, edit profile |
| **Recruiter** | Create missions, view applications, update application status |
| **Admin** | Delete users, delete missions, view all users |

---

## ✅ Testing Checklist (Quick)

- [ ] Register freelancer - Success
- [ ] Register recruiter - Success
- [ ] Login - Get token
- [ ] Validate token - Token valid
- [ ] Access public endpoint without token - Success
- [ ] Access protected endpoint with token - Success
- [ ] Access protected endpoint without token - 401 Unauthorized
- [ ] Freelancer access recruiter endpoint - 403 Forbidden
- [ ] Recruiter access freelancer endpoint - 403 Forbidden

---

## 📞 Getting Help

1. **Check the docs**: Read AUTH_SYSTEM.md first
2. **Review examples**: See ENDPOINT_PROTECTION_GUIDE.md
3. **Understand flow**: Study AUTH_FLOW_DIAGRAMS.md
4. **See what's done**: Check COMPLETION_CHECKLIST.md

---

## 🎯 Next Steps

1. ✅ Auth system complete
2. → Test all endpoints
3. → Build Angular login component
4. → Protect other controllers
5. → Next feature: Messaging System

---

## 💡 Pro Tips

**Tip 1**: Use Postman environments to save `{{token}}` variable

**Tip 2**: Add this to your Angular HTTP interceptor:
```typescript
headers: headers.set('Authorization', `Bearer ${token}`)
```

**Tip 3**: Store token in localStorage:
```typescript
localStorage.setItem('nomad_token', response.token);
```

**Tip 4**: Always include 10-hour expiration in UI:
```typescript
const expiresAt = new Date().getTime() + 36000000;
localStorage.setItem('nomad_expires_at', expiresAt);
```

**Tip 5**: Clean up on logout:
```typescript
localStorage.removeItem('nomad_token');
localStorage.removeItem('nomad_expires_at');
```

---

**Everything you need to use the Nomad auth system in one quick reference! 🚀**
