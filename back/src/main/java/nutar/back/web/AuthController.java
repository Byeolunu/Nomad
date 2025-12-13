package nutar.back.web;

import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.entites.User;
import nutar.back.dao.enums.Role;
import nutar.back.dao.repositories.UserRepository;
import nutar.back.dto.AuthRequest;
import nutar.back.dto.AuthResponse;
import nutar.back.dto.RegisterFreelancerRequest;
import nutar.back.dto.RegisterRecruiterRequest;
import nutar.back.security.JwtUtil;
import nutar.back.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtils;

    /**
     * Register a new freelancer
     */
    @PostMapping("/register/freelancer")
    public ResponseEntity<?> registerFreelancer(@RequestBody RegisterFreelancerRequest request) {
        // Check if email or username already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Email already exists"));
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username already exists"));
        }

        try {
            // Create freelancer entity
            Freelancer freelancer = new Freelancer();
            freelancer.setFirstName(request.getFirstName());
            freelancer.setLastName(request.getLastName());
            freelancer.setEmail(request.getEmail());
            freelancer.setUsername(request.getUsername());
            freelancer.setPassword(passwordEncoder.encode(request.getPassword()));
            freelancer.setPhoneNumber(request.getPhoneNumber());
            freelancer.setRole(Role.FREELANCER);
            freelancer.setTitle(request.getTitle());
            freelancer.setSummary(request.getSummary());
            freelancer.setHourlyRate(request.getHourlyRate());

            Freelancer savedFreelancer = userRepository.save(freelancer);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new SuccessResponse("Freelancer registered successfully", savedFreelancer.getId())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Registration failed: " + e.getMessage()));
        }
    }

    /**
     * Register a new recruiter
     */
    @PostMapping("/register/recruiter")
    public ResponseEntity<?> registerRecruiter(@RequestBody RegisterRecruiterRequest request) {
        // Check if email or username already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Email already exists"));
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username already exists"));
        }

        try {
            // Create recruiter entity
            Recruiter recruiter = new Recruiter();
            recruiter.setFirstName(request.getFirstName());
            recruiter.setLastName(request.getLastName());
            recruiter.setEmail(request.getEmail());
            recruiter.setUsername(request.getUsername());
            recruiter.setPassword(passwordEncoder.encode(request.getPassword()));
            recruiter.setPhoneNumber(request.getPhoneNumber());
            recruiter.setRole(Role.RECRUITER);
            recruiter.setCompanyName(request.getCompanyName());
            recruiter.setCompanyWebsite(request.getCompanyWebsite());

            Recruiter savedRecruiter = userRepository.save(recruiter);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new SuccessResponse("Recruiter registered successfully", savedRecruiter.getId())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Registration failed: " + e.getMessage()));
        }
    }

    /**
     * Login endpoint - returns JWT token and user details
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Load user details and generate JWT
            var userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtUtils.generateToken(userDetails);

            // Get role
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(auth -> auth.getAuthority())
                    .orElse("FREELANCER");

            // Get user from DB to retrieve additional info
            User user = userRepository.findByEmail(request.getEmail()).orElse(null);

            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setRole(role);
            if (user != null) {
                response.setUserId(user.getId());
                response.setEmail(user.getEmail());
                response.setFirstName(user.getFirstName());
                response.setLastName(user.getLastName());
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid email or password"));
        }
    }

    /**
     * Validate token endpoint
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Missing or invalid Authorization header"));
            }

            String token = authHeader.substring(7);
            boolean isValid = jwtUtils.validateToken(token);

            if (isValid) {
                String email = jwtUtils.extractUsername(token);
                return ResponseEntity.ok(new HashMap<String, String>() {{
                    put("valid", "true");
                    put("email", email);
                }});
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Token expired or invalid"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid token"));
        }
    }

    // Helper classes
    public static class ErrorResponse {
        public String message;
        public long timestamp;

        public ErrorResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMessage() {
            return message;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class SuccessResponse {
        public String message;
        public Object data;
        public long timestamp;

        public SuccessResponse(String message, Object data) {
            this.message = message;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
