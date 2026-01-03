package nutar.back;
import nutar.back.dao.entites.*;
import nutar.back.dao.enums.*;
import nutar.back.dao.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.Arrays;
@SpringBootApplication
public class BackApplication {
    @Autowired
    private PasswordEncoder passwordEncoder;
    public static void main(String[] args) {
        SpringApplication.run(BackApplication.class, args);
    }
    @Bean
    CommandLineRunner init(UserRepository userRepository, 
                          MissionRepository missionRepository, 
                          SkillRepository skillRepository,
                          PortfolioRepository portfolioRepository,
                          ReviewRepository reviewRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                Skill angular = new Skill();
                angular.setName("Angular");
                angular.setDescription("Frontend framework for building web applications");
                angular.setCategory(SkillCategory.PROGRAMMING);
                skillRepository.save(angular);


                Skill react = new Skill();
                react.setName("React");
                react.setDescription("JavaScript library for building user interfaces");
                react.setCategory(SkillCategory.PROGRAMMING);
                skillRepository.save(react);


                Skill springBoot = new Skill();
                springBoot.setName("Spring Boot");
                springBoot.setDescription("Java framework for building backend applications");
                springBoot.setCategory(SkillCategory.PROGRAMMING);
                skillRepository.save(springBoot);


                Skill nodeJs = new Skill();
                nodeJs.setName("Node.js");
                nodeJs.setDescription("JavaScript runtime for server-side development");
                nodeJs.setCategory(SkillCategory.PROGRAMMING);
                skillRepository.save(nodeJs);


                Skill figma = new Skill();
                figma.setName("Figma");
                figma.setDescription("Collaborative design tool for UI/UX");
                figma.setCategory(SkillCategory.DESIGN);
                skillRepository.save(figma);


                Skill uiDesign = new Skill();
                uiDesign.setName("UI Design");
                uiDesign.setDescription("User interface design principles and practices");
                uiDesign.setCategory(SkillCategory.DESIGN);
                skillRepository.save(uiDesign);


                Skill uxResearch = new Skill();
                uxResearch.setName("UX Research");
                uxResearch.setDescription("User experience research and testing");
                uxResearch.setCategory(SkillCategory.DESIGN);
                skillRepository.save(uxResearch);


                Skill reactNative = new Skill();
                reactNative.setName("React Native");
                reactNative.setDescription("Framework for building mobile apps");
                reactNative.setCategory(SkillCategory.MOBILE);
                skillRepository.save(reactNative);


                Skill flutter = new Skill();
                flutter.setName("Flutter");
                flutter.setDescription("Google's UI toolkit for mobile development");
                flutter.setCategory(SkillCategory.MOBILE);
                skillRepository.save(flutter);


                Skill typescript = new Skill();
                typescript.setName("TypeScript");
                typescript.setDescription("Typed superset of JavaScript");
                typescript.setCategory(SkillCategory.PROGRAMMING);
                skillRepository.save(typescript);


                Skill deep_Learning = new Skill();
                deep_Learning.setName("Deep Learning");
                deep_Learning.setDescription("Subset of machine learning with neural networks");
                deep_Learning.setCategory(SkillCategory.DATA_SCIENCE);
                skillRepository.save(deep_Learning);


                Skill mongodb = new Skill();
                mongodb.setName("MongoDB");
                mongodb.setDescription("NoSQL database for modern applications");
                mongodb.setCategory(SkillCategory.DATA_SCIENCE);
                skillRepository.save(mongodb);


                Skill postgresql = new Skill();
                postgresql.setName("PostgreSQL");
                postgresql.setDescription("Advanced open-source relational database");
                postgresql.setCategory(SkillCategory.DATA_SCIENCE);
                skillRepository.save(postgresql);


                Skill aws = new Skill();
                aws.setName("AWS");
                aws.setDescription("Amazon Web Services cloud platform");
                aws.setCategory(SkillCategory.DEVOPS);
                skillRepository.save(aws);


                Skill docker = new Skill();
                docker.setName("Docker");
                docker.setDescription("Containerization platform");
                docker.setCategory(SkillCategory.DEVOPS);
                skillRepository.save(docker);


                Skill sql = new Skill();
                sql.setName("SQL");
                sql.setDescription("Structured Query Language for database management");
                sql.setCategory(SkillCategory.DATA_SCIENCE);
                skillRepository.save(sql);


                Skill machine_Learning = new Skill();
                machine_Learning.setName("Machine Learning");
                machine_Learning.setDescription("Algorithms that improve automatically through experience");
                machine_Learning.setCategory(SkillCategory.DATA_SCIENCE);
                skillRepository.save(machine_Learning);


                Recruiter recruiter1 = new Recruiter();
                recruiter1.setFirstName("Youssef");
                recruiter1.setUsername("youssef_tech");
                recruiter1.setLastName("El Amrani");
                recruiter1.setEmail("youssef@techmorocco.com");
                recruiter1.setPassword(passwordEncoder.encode("password123"));
                recruiter1.setPhoneNumber("+212661234567");
                recruiter1.setCompanyName("TechMorocco");
                recruiter1.setCompanyWebsite("https://techmorocco.com");
                recruiter1.setRole(Role.RECRUITER);
                recruiter1.setIsActive(true);
                userRepository.save(recruiter1);

                
                Recruiter recruiter2 = new Recruiter();
                recruiter2.setFirstName("Fatima");
                recruiter2.setUsername("fatima_startup");
                recruiter2.setLastName("Benali");
                recruiter2.setEmail("fatima@startuphub.ma");
                recruiter2.setPassword(passwordEncoder.encode("password123"));
                recruiter2.setPhoneNumber("+212662345678");
                recruiter2.setCompanyName("StartupHub");
                recruiter2.setCompanyWebsite("https://startuphub.ma");
                recruiter2.setRole(Role.RECRUITER);
                recruiter2.setIsActive(true);
                userRepository.save(recruiter2);
                Recruiter recruiter3 = new Recruiter();
                recruiter3.setFirstName("Omar");
                recruiter3.setUsername("omar_digital");
                recruiter3.setLastName("Tazi");
                recruiter3.setEmail("omar@digitalmaroc.com");
                recruiter3.setPassword(passwordEncoder.encode("password123"));
                recruiter3.setPhoneNumber("+212663456789");
                recruiter3.setCompanyName("DigitalMaroc");
                recruiter3.setCompanyWebsite("https://digitalmaroc.com");
                recruiter3.setRole(Role.RECRUITER);
                recruiter3.setIsActive(true);
                userRepository.save(recruiter3);
                Freelancer freelancer1 = new Freelancer();
                freelancer1.setFirstName("Karim");
                freelancer1.setUsername("karim_dev");
                freelancer1.setLastName("Benjelloun");
                freelancer1.setEmail("karim@devpro.ma");
                freelancer1.setPassword(passwordEncoder.encode("password123"));
                freelancer1.setPhoneNumber("+212664567890");
                freelancer1.setTitle("Senior Full Stack Developer");
                freelancer1.setSummary("Passionate full stack developer with 8+ years of experience building scalable web applications. Specialized in Angular, React, Node.js, and cloud technologies.");
                freelancer1.setHourlyRate(350.0);
                freelancer1.setRole(Role.FREELANCER);
                freelancer1.setIsActive(true);
                freelancer1.setSkills(Arrays.asList(angular, react, springBoot, nodeJs, typescript, mongodb, aws, docker));
                Profile profile1 = new Profile();
                profile1.setBio("I love turning complex problems into elegant solutions. With 8+ years in the industry, I've delivered projects for startups and enterprises alike.");
                profile1.setLocation("Casablanca");
                profile1.setExperienceLevel(ExperienceLevel.SENIOR);
                profile1.setProfilePicture("https://randomuser.me/api/portraits/men/1.jpg");
                freelancer1.setProfile(profile1);
                userRepository.save(freelancer1);
                Portfolio portfolio1_1 = new Portfolio();
                portfolio1_1.setTitle("E-commerce Platform");
                portfolio1_1.setDescription("Full-featured online store with payment integration and inventory management");
                portfolio1_1.setImageUrl("https://images.pexels.com/photos/230544/pexels-photo-230544.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop");
                portfolio1_1.setProjectUrl("https://github.com/karim/ecommerce");
                portfolio1_1.setFreelancer(freelancer1);
                portfolioRepository.save(portfolio1_1);
                Portfolio portfolio1_2 = new Portfolio();
                portfolio1_2.setTitle("Healthcare App");
                portfolio1_2.setDescription("Telemedicine platform for remote consultations with video calling");
                portfolio1_2.setImageUrl("https://images.pexels.com/photos/40568/medical-appointment-doctor-healthcare-40568.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop");
                portfolio1_2.setProjectUrl("https://github.com/karim/healthcare");
                portfolio1_2.setFreelancer(freelancer1);
                portfolioRepository.save(portfolio1_2);
                Portfolio portfolio1_3 = new Portfolio();
                portfolio1_3.setTitle("Finance Dashboard");
                portfolio1_3.setDescription("Real-time analytics dashboard for fintech with interactive charts");
                portfolio1_3.setImageUrl("https://images.pexels.com/photos/186461/pexels-photo-186461.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop");
                portfolio1_3.setProjectUrl("https://github.com/karim/finance-dash");
                portfolio1_3.setFreelancer(freelancer1);
                portfolioRepository.save(portfolio1_3);
                Freelancer freelancer2 = new Freelancer();
                freelancer2.setFirstName("Leila");
                freelancer2.setUsername("leila_design");
                freelancer2.setLastName("Mansouri");
                freelancer2.setEmail("leila@designstudio.ma");
                freelancer2.setPassword(passwordEncoder.encode("password123"));
                freelancer2.setPhoneNumber("+212665678901");
                freelancer2.setTitle("UI/UX Designer");
                freelancer2.setSummary("Creative designer with a passion for crafting beautiful and intuitive user experiences. 6 years of experience in product design and branding.");
                freelancer2.setHourlyRate(280.0);
                freelancer2.setRole(Role.FREELANCER);
                freelancer2.setIsActive(true);
                freelancer2.setSkills(Arrays.asList(figma, uiDesign, uxResearch));
                Profile profile2 = new Profile();
                profile2.setBio("I believe great design solves problems. My approach combines aesthetics with usability to create memorable digital experiences.");
                profile2.setLocation("Rabat");
                profile2.setExperienceLevel(ExperienceLevel.MID);
                profile2.setProfilePicture("https://randomuser.me/api/portraits/women/2.jpg");
                freelancer2.setProfile(profile2);
                userRepository.save(freelancer2);
                Portfolio portfolio2_1 = new Portfolio();
                portfolio2_1.setTitle("Banking App Redesign");
                portfolio2_1.setDescription("Modern mobile banking experience with improved UX");
                portfolio2_1.setImageUrl("https://images.pexels.com/photos/50987/money-card-business-credit-card-50987.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop");
                portfolio2_1.setProjectUrl("https://dribbble.com/leila/banking");
                portfolio2_1.setFreelancer(freelancer2);
                portfolioRepository.save(portfolio2_1);
                Portfolio portfolio2_2 = new Portfolio();
                portfolio2_2.setTitle("SaaS Dashboard");
                portfolio2_2.setDescription("Complex analytics made simple with intuitive design");
                portfolio2_2.setImageUrl("https://images.pexels.com/photos/669615/pexels-photo-669615.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop");
                portfolio2_2.setProjectUrl("https://dribbble.com/leila/saas");
                portfolio2_2.setFreelancer(freelancer2);
                portfolioRepository.save(portfolio2_2);
                Freelancer freelancer3 = new Freelancer();
                freelancer3.setFirstName("Hassan");
                freelancer3.setUsername("hassan_mobile");
                freelancer3.setLastName("El Amrani");
                freelancer3.setEmail("hassan@mobiledev.ma");
                freelancer3.setPassword(passwordEncoder.encode("password123"));
                freelancer3.setPhoneNumber("+212666789012");
                freelancer3.setTitle("Mobile App Developer");
                freelancer3.setSummary("Mobile developer specializing in React Native and Flutter. Built apps with millions of downloads. Focus on performance and exceptional UX.");
                freelancer3.setHourlyRate(300.0);
                freelancer3.setRole(Role.FREELANCER);
                freelancer3.setIsActive(true);
                freelancer3.setSkills(Arrays.asList(reactNative, flutter, typescript));
                Profile profile3 = new Profile();
                profile3.setBio("Creating mobile experiences that users love. I focus on clean code, smooth animations, and intuitive interfaces.");
                profile3.setLocation("Marrakech");
                profile3.setExperienceLevel(ExperienceLevel.MID);
                profile3.setProfilePicture("https://randomuser.me/api/portraits/men/3.jpg");
                freelancer3.setProfile(profile3);
                userRepository.save(freelancer3);
                Portfolio portfolio3_1 = new Portfolio();
                portfolio3_1.setTitle("Fitness Tracker");
                portfolio3_1.setDescription("Health and workout companion app with 500K+ downloads");
                portfolio3_1.setImageUrl("https://images.pexels.com/photos/841130/pexels-photo-841130.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop");
                portfolio3_1.setProjectUrl("https://github.com/hassan/fitness");
                portfolio3_1.setFreelancer(freelancer3);
                portfolioRepository.save(portfolio3_1);
                Portfolio portfolio3_2 = new Portfolio();
                portfolio3_2.setTitle("Food Delivery App");
                portfolio3_2.setDescription("Complete delivery solution with real-time tracking");
                portfolio3_2.setImageUrl("https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop");
                portfolio3_2.setProjectUrl("https://github.com/hassan/delivery");
                portfolio3_2.setFreelancer(freelancer3);
                portfolioRepository.save(portfolio3_2);
                Freelancer freelancer4 = new Freelancer();
                freelancer4.setFirstName("Amina");
                freelancer4.setUsername("amina_devops");
                freelancer4.setLastName("Berrada");
                freelancer4.setEmail("amina@cloudpro.ma");
                freelancer4.setPassword(passwordEncoder.encode("password123"));
                freelancer4.setPhoneNumber("+212667890123");
                freelancer4.setTitle("DevOps Engineer");
                freelancer4.setSummary("DevOps specialist with expertise in cloud infrastructure, CI/CD pipelines, and containerization. Helping teams ship faster and more reliably.");
                freelancer4.setHourlyRate(400.0);
                freelancer4.setRole(Role.FREELANCER);
                freelancer4.setIsActive(true);
                freelancer4.setSkills(Arrays.asList(aws, docker, nodeJs));
                Profile profile4 = new Profile();
                profile4.setBio("Automating everything that can be automated. I help teams build robust deployment pipelines and scalable infrastructure.");
                profile4.setLocation("Casablanca");
                profile4.setExperienceLevel(ExperienceLevel.SENIOR);
                profile4.setProfilePicture("https://randomuser.me/api/portraits/women/4.jpg");
                freelancer4.setProfile(profile4);
                userRepository.save(freelancer4);
                Portfolio portfolio4_1 = new Portfolio();
                portfolio4_1.setTitle("Cloud Migration");
                portfolio4_1.setDescription("Enterprise AWS migration reducing costs by 40%");
                portfolio4_1.setImageUrl("https://images.pexels.com/photos/1148820/pexels-photo-1148820.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop");
                portfolio4_1.setProjectUrl("https://github.com/amina/cloud-migration");
                portfolio4_1.setFreelancer(freelancer4);
                portfolioRepository.save(portfolio4_1);
                Freelancer freelancer5 = new Freelancer();
                freelancer5.setFirstName("Yassine");
                freelancer5.setUsername("yassine_backend");
                freelancer5.setLastName("Cherkaoui");
                freelancer5.setEmail("yassine@backend.ma");
                freelancer5.setPassword(passwordEncoder.encode("password123"));
                freelancer5.setPhoneNumber("+212668901234");
                freelancer5.setTitle("Backend Developer");
                freelancer5.setSummary("Backend specialist with deep expertise in Java, Spring Boot, and microservices architecture. Building reliable and scalable systems.");
                freelancer5.setHourlyRate(320.0);
                freelancer5.setRole(Role.FREELANCER);
                freelancer5.setIsActive(true);
                freelancer5.setSkills(Arrays.asList(springBoot, postgresql, docker, aws));
                Profile profile5 = new Profile();
                profile5.setBio("Clean code advocate. I design and build backend systems that are maintainable, testable, and performant.");
                profile5.setLocation("Fes");
                profile5.setExperienceLevel(ExperienceLevel.SENIOR);
                profile5.setProfilePicture("https://randomuser.me/api/portraits/men/5.jpg");
                freelancer5.setProfile(profile5);
                userRepository.save(freelancer5);
                Portfolio portfolio5_1 = new Portfolio();
                portfolio5_1.setTitle("Payment Gateway");
                portfolio5_1.setDescription("Secure payment processing system handling 1M+ transactions");
                portfolio5_1.setImageUrl("https://images.pexels.com/photos/4386431/pexels-photo-4386431.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop");
                portfolio5_1.setProjectUrl("https://github.com/yassine/payment-gateway");
                portfolio5_1.setFreelancer(freelancer5);
                portfolioRepository.save(portfolio5_1);
                Portfolio portfolio5_2 = new Portfolio();
                portfolio5_2.setTitle("API Platform");
                portfolio5_2.setDescription("RESTful API platform with comprehensive documentation");
                portfolio5_2.setImageUrl("https://images.pexels.com/photos/546819/pexels-photo-546819.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop");
                portfolio5_2.setProjectUrl("https://github.com/yassine/api-platform");
                portfolio5_2.setFreelancer(freelancer5);
                portfolioRepository.save(portfolio5_2);
                Mission mission1 = new Mission();
                mission1.setTitle("Build E-commerce Website");
                mission1.setDescription("We need a full-stack developer to build a modern e-commerce platform with Angular frontend and Spring Boot backend. Features include product catalog, shopping cart, payment integration, and admin dashboard.");
                mission1.setBudget(25000.0);
                mission1.setDeadline(LocalDateTime.now().plusDays(45));
                mission1.setRecruiter(recruiter1);
                mission1.setStatus(MissionStatus.OPEN);
                mission1.setType(MissionType.FIXED);
                mission1.setRequiredSkills(Arrays.asList(angular, springBoot, postgresql));
                missionRepository.save(mission1);
                Mission mission2 = new Mission();
                mission2.setTitle("Mobile App UI/UX Design");
                mission2.setDescription("Looking for a talented UI/UX designer to create wireframes, mockups, and final designs for a fitness tracking mobile app. Must include user research and prototyping.");
                mission2.setBudget(12000.0);
                mission2.setDeadline(LocalDateTime.now().plusDays(21));
                mission2.setRecruiter(recruiter2);
                mission2.setStatus(MissionStatus.OPEN);
                mission2.setType(MissionType.FIXED);
                mission2.setRequiredSkills(Arrays.asList(figma, uiDesign, uxResearch));
                missionRepository.save(mission2);
                Mission mission3 = new Mission();
                mission3.setTitle("React Native Developer for Fintech App");
                mission3.setDescription("Join our team to develop a cutting-edge fintech mobile application. Experience with payment integrations and security best practices required.");
                mission3.setBudget(35000.0);
                mission3.setDeadline(LocalDateTime.now().plusDays(60));
                mission3.setRecruiter(recruiter3);
                mission3.setStatus(MissionStatus.OPEN);
                mission3.setType(MissionType.FIXED);
                mission3.setRequiredSkills(Arrays.asList(reactNative, typescript, nodeJs));
                missionRepository.save(mission3);
                Mission mission4 = new Mission();
                mission4.setTitle("AWS Cloud Migration");
                mission4.setDescription("Migrate our on-premise infrastructure to AWS. Includes setting up VPC, EC2, RDS, and implementing CI/CD pipelines with proper monitoring.");
                mission4.setBudget(40000.0);
                mission4.setDeadline(LocalDateTime.now().plusDays(90));
                mission4.setRecruiter(recruiter1);
                mission4.setStatus(MissionStatus.OPEN);
                mission4.setType(MissionType.FIXED);
                mission4.setRequiredSkills(Arrays.asList(aws, docker));
                missionRepository.save(mission4);
                Mission mission5 = new Mission();
                mission5.setTitle("Full Stack Web Application");
                mission5.setDescription("Build a project management tool with real-time collaboration features. Tech stack: React, Node.js, MongoDB, WebSockets.");
                mission5.setBudget(30000.0);
                mission5.setDeadline(LocalDateTime.now().plusDays(75));
                mission5.setRecruiter(recruiter2);
                mission5.setStatus(MissionStatus.OPEN);
                mission5.setType(MissionType.FIXED);
                mission5.setRequiredSkills(Arrays.asList(react, nodeJs, mongodb));
                missionRepository.save(mission5);
                Mission mission6 = new Mission();
                mission6.setTitle("Backend API Development");
                mission6.setDescription("Develop RESTful APIs for our mobile application. Must include authentication, rate limiting, and comprehensive documentation.");
                mission6.setBudget(18000.0);
                mission6.setDeadline(LocalDateTime.now().plusDays(30));
                mission6.setRecruiter(recruiter3);
                mission6.setStatus(MissionStatus.OPEN);
                mission6.setType(MissionType.HOURLY);
                mission6.setRequiredSkills(Arrays.asList(springBoot, postgresql, docker));
                missionRepository.save(mission6);
                Review review1_1 = new Review();
                review1_1.setRating(5);
                review1_1.setComment("Karim delivered an exceptional e-commerce platform. His technical skills are outstanding and communication was excellent throughout the project. Highly recommended!");
                review1_1.setAuthorName("Youssef El Amrani");
                review1_1.setFreelancer(freelancer1);
                review1_1.setRecruiter(recruiter1);
                review1_1.setCreatedAt(LocalDateTime.now().minusDays(30));
                reviewRepository.save(review1_1);
                Review review1_2 = new Review();
                review1_2.setRating(5);
                review1_2.setComment("Working with Karim was a pleasure. He understood our requirements perfectly and delivered ahead of schedule. The code quality was impeccable.");
                review1_2.setAuthorName("Fatima Benali");
                review1_2.setFreelancer(freelancer1);
                review1_2.setRecruiter(recruiter2);
                review1_2.setCreatedAt(LocalDateTime.now().minusDays(60));
                reviewRepository.save(review1_2);
                Review review1_3 = new Review();
                review1_3.setRating(4);
                review1_3.setComment("Great developer with strong problem-solving skills. Minor delays but overall excellent work on our dashboard project.");
                review1_3.setAuthorName("Omar Tazi");
                review1_3.setFreelancer(freelancer1);
                review1_3.setRecruiter(recruiter3);
                review1_3.setCreatedAt(LocalDateTime.now().minusDays(90));
                reviewRepository.save(review1_3);
                Review review2_1 = new Review();
                review2_1.setRating(5);
                review2_1.setComment("Leila's design work transformed our app completely. Her attention to detail and understanding of user psychology is remarkable.");
                review2_1.setAuthorName("Fatima Benali");
                review2_1.setFreelancer(freelancer2);
                review2_1.setRecruiter(recruiter2);
                review2_1.setCreatedAt(LocalDateTime.now().minusDays(15));
                reviewRepository.save(review2_1);
                Review review2_2 = new Review();
                review2_2.setRating(5);
                review2_2.setComment("Incredible designer! She delivered beautiful mockups that our users absolutely love. Will definitely work with her again.");
                review2_2.setAuthorName("Omar Tazi");
                review2_2.setFreelancer(freelancer2);
                review2_2.setRecruiter(recruiter3);
                review2_2.setCreatedAt(LocalDateTime.now().minusDays(45));
                reviewRepository.save(review2_2);
                Review review3_1 = new Review();
                review3_1.setRating(5);
                review3_1.setComment("Hassan built us an amazing mobile app. The performance is fantastic and users have rated it 4.8 stars on the app store!");
                review3_1.setAuthorName("Youssef El Amrani");
                review3_1.setFreelancer(freelancer3);
                review3_1.setRecruiter(recruiter1);
                review3_1.setCreatedAt(LocalDateTime.now().minusDays(20));
                reviewRepository.save(review3_1);
                Review review3_2 = new Review();
                review3_2.setRating(4);
                review3_2.setComment("Solid mobile developer. Good communication and delivered a quality app. Would recommend for mobile projects.");
                review3_2.setAuthorName("Fatima Benali");
                review3_2.setFreelancer(freelancer3);
                review3_2.setRecruiter(recruiter2);
                review3_2.setCreatedAt(LocalDateTime.now().minusDays(75));
                reviewRepository.save(review3_2);
                Review review4_1 = new Review();
                review4_1.setRating(5);
                review4_1.setComment("Amina migrated our entire infrastructure to AWS flawlessly. She reduced our cloud costs by 40% and improved deployment speed significantly.");
                review4_1.setAuthorName("Youssef El Amrani");
                review4_1.setFreelancer(freelancer4);
                review4_1.setRecruiter(recruiter1);
                review4_1.setCreatedAt(LocalDateTime.now().minusDays(10));
                reviewRepository.save(review4_1);
                Review review4_2 = new Review();
                review4_2.setRating(5);
                review4_2.setComment("Expert DevOps engineer. Set up our CI/CD pipeline and now deployments that took hours are done in minutes. Highly skilled!");
                review4_2.setAuthorName("Omar Tazi");
                review4_2.setFreelancer(freelancer4);
                review4_2.setRecruiter(recruiter3);
                review4_2.setCreatedAt(LocalDateTime.now().minusDays(40));
                reviewRepository.save(review4_2);
                Review review5_1 = new Review();
                review5_1.setRating(5);
                review5_1.setComment("Yassine built a robust payment gateway that handles millions of transactions. His code is clean and well-documented. Excellent work!");
                review5_1.setAuthorName("Omar Tazi");
                review5_1.setFreelancer(freelancer5);
                review5_1.setRecruiter(recruiter3);
                review5_1.setCreatedAt(LocalDateTime.now().minusDays(25));
                reviewRepository.save(review5_1);
                Review review5_2 = new Review();
                review5_2.setRating(4);
                review5_2.setComment("Strong backend developer with excellent knowledge of Spring Boot. Delivered a scalable API platform for our startup.");
                review5_2.setAuthorName("Fatima Benali");
                review5_2.setFreelancer(freelancer5);
                review5_2.setRecruiter(recruiter2);
                review5_2.setCreatedAt(LocalDateTime.now().minusDays(55));
                reviewRepository.save(review5_2);
                System.out.println("✅ Sample data initialized successfully!");
                System.out.println("📊 Created: 14 Skills, 3 Recruiters, 5 Freelancers with Portfolios, 6 Missions, 11 Reviews");
            }
        };
    }
}
