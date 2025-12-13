# How to Protect Endpoints with Role-Based Access Control

This guide shows how to add role-based security to your existing controllers.

## Quick Reference

```java
import nutar.back.security.RequireFreelancer;
import nutar.back.security.RequireRecruiter;
import nutar.back.security.RequireAdmin;
```

## Examples for Each Controller

### MissionController

```java
package nutar.back.web;

import nutar.back.dao.entites.Mission;
import nutar.back.dao.entites.Recruiter;
import nutar.back.service.MissionService;
import nutar.back.service.RecruiterService;
import nutar.back.security.RequireRecruiter;
import nutar.back.security.RequireFreelancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@CrossOrigin(origins = "http://localhost:4200")
public class MissionController {

    @Autowired
    private MissionService missionService;

    @Autowired
    private RecruiterService recruiterService;

    @GetMapping
    public ResponseEntity<List<Mission>> getAllMissions() {
        // PUBLIC - Anyone can view missions
        List<Mission> missions = missionService.getAllOpenMissions();
        return ResponseEntity.ok(missions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mission> getMissionById(@PathVariable Long id) {
        // PUBLIC - Anyone can view mission details
        Mission mission = missionService.getMissionById(id);
        return ResponseEntity.ok(mission);
    }

    @PostMapping
    @RequireRecruiter  // Only recruiters can create missions
    public ResponseEntity<Mission> createMission(@RequestBody Mission mission) {
        Mission createdMission = missionService.createMission(mission, getCurrentRecruiterId());
        return ResponseEntity.ok(createdMission);
    }

    @GetMapping("/recruiter/{recruiterId}")
    @RequireRecruiter  // Only recruiters can view their own missions
    public ResponseEntity<List<Mission>> getMissionsByRecruiter(@PathVariable Long recruiterId) {
        List<Mission> missions = missionService.getMissionsByRecruiter(recruiterId);
        return ResponseEntity.ok(missions);
    }

    // Helper method to get current authenticated recruiter ID
    private Long getCurrentRecruiterId() {
        // Get from security context or principal
        return 1L; // TODO: Extract from authentication
    }
}
```

### ApplicationController

```java
package nutar.back.web;

import nutar.back.dao.entites.Application;
import nutar.back.service.ApplicationService;
import nutar.back.security.RequireFreelancer;
import nutar.back.security.RequireRecruiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply")
    @RequireFreelancer  // Only freelancers can apply
    public ResponseEntity<Application> applyToMission(@RequestBody Application application) {
        Application savedApplication = applicationService.applyToMission(application);
        return ResponseEntity.ok(savedApplication);
    }

    @GetMapping("/mission/{missionId}")
    @RequireRecruiter  // Only recruiters can view applications on their missions
    public ResponseEntity<List<Application>> getApplicationsForMission(@PathVariable Long missionId) {
        List<Application> applications = applicationService.getApplicationsForMission(missionId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/freelancer/{freelancerId}")
    @RequireFreelancer  // Only freelancers can view their own applications
    public ResponseEntity<List<Application>> getFreelancerApplications(@PathVariable Long freelancerId) {
        List<Application> applications = applicationService.getFreelancerApplications(freelancerId);
        return ResponseEntity.ok(applications);
    }

    @PutMapping("/{applicationId}/status")
    @RequireRecruiter  // Only recruiters can change application status
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable Long applicationId, 
            @RequestBody String status) {
        Application updatedApplication = applicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(updatedApplication);
    }
}
```

### ProfileController (Example)

```java
package nutar.back.web;

import nutar.back.dao.entites.Profile;
import nutar.back.security.RequireFreelancer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileController {

    @GetMapping("/{id}")
    public Profile getProfile(@PathVariable Long id) {
        // PUBLIC - Anyone can view profiles
        return new Profile();
    }

    @PutMapping("/{id}")
    @RequireFreelancer  // Only freelancers can update their profile
    public Profile updateProfile(@PathVariable Long id, @RequestBody Profile profile) {
        // Update logic
        return profile;
    }
}
```

### Future Admin Controller (Example)

```java
package nutar.back.web;

import nutar.back.dao.entites.User;
import nutar.back.security.RequireAdmin;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @GetMapping("/users")
    @RequireAdmin  // Only admins can view all users
    public java.util.List<User> getAllUsers() {
        // Return all users
        return new java.util.ArrayList<>();
    }

    @DeleteMapping("/users/{userId}")
    @RequireAdmin  // Only admins can delete users
    public void deleteUser(@PathVariable Long userId) {
        // Delete user logic
    }

    @DeleteMapping("/missions/{missionId}")
    @RequireAdmin  // Only admins can delete missions
    public void deleteMission(@PathVariable Long missionId) {
        // Delete mission logic
    }
}
```

## Access Control Matrix

| Endpoint | Public | Freelancer | Recruiter | Admin |
|----------|--------|-----------|-----------|-------|
| GET /api/missions | ✅ | ✅ | ✅ | ✅ |
| POST /api/missions | ❌ | ❌ | ✅ | ✅ |
| POST /api/applications/apply | ❌ | ✅ | ❌ | ❌ |
| GET /api/applications/freelancer/{id} | ❌ | ✅ | ❌ | ❌ |
| PUT /api/applications/{id}/status | ❌ | ❌ | ✅ | ❌ |
| GET /api/profiles/{id} | ✅ | ✅ | ✅ | ✅ |
| PUT /api/profiles/{id} | ❌ | ✅ | ❌ | ❌ |
| GET /api/admin/users | ❌ | ❌ | ❌ | ✅ |
| DELETE /api/admin/users/{id} | ❌ | ❌ | ❌ | ✅ |

## How @PreAuthorize Works

When you add `@RequireFreelancer` to a method:

1. Spring intercepts the request
2. Checks if user has `ROLE_FREELANCER` authority
3. If yes → method executes
4. If no → returns 403 Forbidden

### Error Response for Unauthorized Access
```json
{
  "timestamp": "2024-12-12T10:30:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/admin/users"
}
```

## Getting Current User Information

To get current user data within a controller method:

```java
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@PostMapping("/apply")
@RequireFreelancer
public ResponseEntity<Application> applyToMission(@RequestBody Application application) {
    // Get current authenticated user
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = auth.getName(); // email is used as username
    
    // Find user by email and get their ID
    // User user = userRepository.findByEmail(userEmail).orElse(null);
    // application.setFreelancer(...);
    
    Application savedApplication = applicationService.applyToMission(application);
    return ResponseEntity.ok(savedApplication);
}
```

## Testing Protected Endpoints

### Test with curl (include token)
```bash
# Get token from login
TOKEN=$(curl -X POST http://localhost:8090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ahmed@example.com","password":"password123"}' \
  | grep -o '"token":"[^"]*' | cut -d'"' -f4)

# Use token in protected endpoint
curl -X GET http://localhost:8090/api/missions \
  -H "Authorization: Bearer $TOKEN"
```

### Test with Postman
1. Login to get token
2. Copy token from response
3. In request headers, add: `Authorization: Bearer <token>`
4. Send request

---

## Summary

✅ Use `@RequireFreelancer` for freelancer-only endpoints
✅ Use `@RequireRecruiter` for recruiter-only endpoints  
✅ Use `@RequireAdmin` for admin-only endpoints
✅ Leave endpoints without annotation for public access
✅ Test with Authorization header containing JWT token
