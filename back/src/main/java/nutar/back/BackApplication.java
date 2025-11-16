package nutar.back;

import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Mission;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.enums.Role;
import nutar.back.dao.repositories.MissionRepository;
import nutar.back.dao.repositories.RecruiterRepository;
import nutar.back.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class BackApplication {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(BackApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository , MissionRepository missionRepository, RecruiterRepository recruiterRepository) {
        return args -> {
            // Only add users if database is empty
            if (userRepository.count() == 0) {

                // ==================== RECRUITERS ====================
                Recruiter recruiter1 = new Recruiter();
                recruiter1.setFirstName("John");
                recruiter1.setUsername("john");
                recruiter1.setLastName("Smith");
                recruiter1.setEmail("recruiter@company.com");
                recruiter1.setPassword(passwordEncoder.encode("password123"));
                recruiter1.setPhoneNumber("+1234567890");
                recruiter1.setCompanyName("Tech Solutions Inc");
                recruiter1.setCompanyWebsite("https://techsolutions.com");
                recruiter1.setRole(Role.RECRUITER);
                userRepository.save(recruiter1);

                Recruiter recruiter2 = new Recruiter();
                recruiter2.setFirstName("Sarah");
                recruiter2.setUsername("sRAAAHH");
                recruiter2.setLastName("Johnson");
                recruiter2.setEmail("sarah@startup.com");
                recruiter2.setPassword(passwordEncoder.encode("password123"));
                recruiter2.setPhoneNumber("+0987654321");
                recruiter2.setCompanyName("Innovate Startup");
                recruiter2.setCompanyWebsite("https://innovatestartup.com");
                recruiter2.setRole(Role.RECRUITER);
                userRepository.save(recruiter2);


                // ==================== FREELANCERS ====================
                Freelancer freelancer1 = new Freelancer();
                freelancer1.setFirstName("Alice");
                freelancer1.setUsername("alicieee");
                freelancer1.setLastName("Williams");
                freelancer1.setEmail("alice@dev.com");
                freelancer1.setPassword(passwordEncoder.encode("password123"));
                freelancer1.setPhoneNumber("+1112223333");
                freelancer1.setTitle("Senior Full Stack Developer");
                freelancer1.setSummary("Experienced developer with 5+ years in web development");
                freelancer1.setHourlyRate(65.0);
                freelancer1.setRole(Role.FREELANCER);
                userRepository.save(freelancer1);

                Freelancer freelancer2 = new Freelancer();
                freelancer2.setFirstName("Bob");
                freelancer2.setUsername("bobie");
                freelancer2.setLastName("Brown");
                freelancer2.setEmail("bob@design.com");
                freelancer2.setPassword(passwordEncoder.encode("password123"));
                freelancer2.setPhoneNumber("+4445556666");
                freelancer2.setTitle("UI/UX Designer");
                freelancer2.setSummary("Creative designer specializing in user-centered design");
                freelancer2.setHourlyRate(45.0);
                freelancer2.setRole(Role.FREELANCER);
                userRepository.save(freelancer2);

                Freelancer freelancer3 = new Freelancer();
                freelancer3.setFirstName("Emma");
                freelancer3.setUsername("emmaaaa");
                freelancer3.setLastName("Davis");
                freelancer3.setEmail("emma@mobile.com");
                freelancer3.setPassword(passwordEncoder.encode("password123"));
                freelancer3.setPhoneNumber("+7778889999");
                freelancer3.setTitle("Mobile App Developer");
                freelancer3.setSummary("Android and iOS developer with 3 years experience");
                freelancer3.setHourlyRate(55.0);
                freelancer3.setRole(Role.FREELANCER);
                userRepository.save(freelancer3);

                Mission mission1 = new Mission();
                mission1.setTitle("Build E-commerce Website");
                mission1.setDescription("We need a full-stack developer to build an e-commerce platform with React frontend and Spring Boot backend.");
                mission1.setBudget(2500.0);
                mission1.setDeadline(LocalDateTime.now().plusDays(30));
                mission1.setRecruiter(recruiter1);
                missionRepository.save(mission1);

                Mission mission2 = new Mission();
                mission2.setTitle("Mobile App UI Design");
                mission2.setDescription("Looking for a UI/UX designer to create wireframes and design for a fitness tracking mobile app.");
                mission2.setBudget(1200.0);
                mission2.setDeadline(LocalDateTime.now().plusDays(21));
                mission2.setRecruiter(recruiter2);
                missionRepository.save(mission2);

            }
        };
    }
}
