package nutar.back.web;
import lombok.Getter;
import lombok.Setter;
import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.entites.User;
import nutar.back.dao.enums.Role;
import nutar.back.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import nutar.back.service.JwtService;
import nutar.back.service.CustomUserDetailsService;
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
    private JwtService jwtUtils;
    @PostMapping("/register/freelancer")
    public ResponseEntity<?> registerFreelancer(@RequestBody SignupRequest request)
    {
        if (userRepository.existsByEmail(request.getEmail()))
        {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        String firstName = sanitizeName(request.getFirstName());
        String lastName = sanitizeName(request.getLastName());
        if (firstName.isEmpty() || lastName.isEmpty()) {
            return ResponseEntity.badRequest().body("First name and last name are required");
        }

        Freelancer freelancer = new Freelancer();
        freelancer.setEmail(request.getEmail());
        freelancer.setPassword(passwordEncoder.encode(request.getPassword()));
        freelancer.setUsername(request.getEmail());
        freelancer.setFirstName(firstName);
        freelancer.setLastName(lastName);
        freelancer.setRole(Role.FREELANCER);
        Freelancer savedFreelancer = userRepository.save(freelancer);
        return ResponseEntity.ok(savedFreelancer);
    }
    @PostMapping("/register/recruiter")
    public ResponseEntity<?> registerRecruiter(@RequestBody SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        String firstName = sanitizeName(request.getFirstName());
        String lastName = sanitizeName(request.getLastName());
        if (firstName.isEmpty() || lastName.isEmpty()) {
            return ResponseEntity.badRequest().body("First name and last name are required");
        }

        Recruiter recruiter = new Recruiter();
        recruiter.setEmail(request.getEmail());
        recruiter.setPassword(passwordEncoder.encode(request.getPassword()));
        recruiter.setUsername(request.getEmail());
        recruiter.setFirstName(firstName);
        recruiter.setLastName(lastName);
        recruiter.setRole(Role.RECRUITER);
        Recruiter savedRecruiter = userRepository.save(recruiter);
        return ResponseEntity.ok(savedRecruiter);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request)
    {
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Email: " + request.getEmail());
        try
        {
            System.out.println("Attempting authentication manager...");
            var authToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            var authentication = authenticationManager.authenticate(authToken);
            System.out.println("Authentication SUCCESSFUL!");
            System.out.println("Authenticated: " + authentication.isAuthenticated());
            System.out.println("Principal: " + authentication.getPrincipal());
            final var userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            final String token = jwtUtils.generateToken(userDetails);
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(auth -> auth.getAuthority())
                    .orElse("FREELANCER");
            
            // Get the user to retrieve ID
            var user = userRepository.findByEmail(request.getEmail()).orElse(null);
            Long userId = user != null ? user.getId() : null;
            
            return ResponseEntity.ok(new AuthResponse(token, role, userId, request.getEmail()));
        }
        catch (Exception e)
        {
            System.out.println("=== AUTHENTICATION FAILED ===");
            System.out.println("Exception type: " + e.getClass().getName());
            System.out.println("Exception message: " + e.getMessage());
            e.printStackTrace();
            String debugMsg = "Invalid email or password";
            try {
                var optUser = userRepository.findByEmail(request.getEmail());
                if (optUser.isPresent()) {
                    User u = optUser.get();
                    boolean matches = passwordEncoder.matches(request.getPassword(), u.getPassword());
                    debugMsg = "User found. passwordMatches=" + matches + ", storedPassword='" + u.getPassword() + "'";
                    System.out.println(debugMsg);
                } else {
                    debugMsg = "User not found with email: " + request.getEmail();
                    System.out.println(debugMsg);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(debugMsg);
        }
    }
    @Setter
    @Getter
    public static class AuthRequest {
        private String email;
        private String password;
    }
    @Getter
    public static class AuthResponse {
        private final String token;
        private final String role;
        private final Long userId;
        private final String email;
        
        public AuthResponse(String token, String role, Long userId, String email) { 
            this.token = token; 
            this.role = role; 
            this.userId = userId;
            this.email = email;
        }
    }
    @Setter
    @Getter
    public static class SignupRequest {
        private String email;
        private String password;
        private String role;
        private String firstName;
        private String lastName;
    }

    private String sanitizeName(String name) {
        return name == null ? "" : name.trim();
    }
}
