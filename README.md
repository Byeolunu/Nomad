# Nomad - Freelance Platform in Morocco 🇲🇦

Nomad is a modern web platform designed to connect Moroccan freelance talents with recruiters seeking expertise. The project follows a decoupled architecture with a robust Spring Boot back-end and a responsive Angular front-end.

## 🏗️ Project Structure

```text
Nomad/
├── back/ (Spring Boot)
│   ├── src/main/java/nutar/back/
│   │   ├── config/          # App & Security configurations
│   │   ├── dao/             # Data Access Layer
│   │   │   ├── entites/     # JPA Entities (Freelancer, Mission, etc.)
│   │   │   └── repositories/# Spring Data JPA Repositories
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── mappers/         # MapStruct Mappers (Entity <-> DTO)
│   │   ├── service/         # Business Logic Layer
│   │   └── web/             # REST Controllers (API Endpoints)
│   └── src/main/resources/  # Configuration files (application.properties)
│
└── front/ (Angular)
    ├── src/app/
    │   ├── auth/            # Authentication (Login/Signup)
    │   ├── components/      # Shared UI Components (header, footer, ui)
    │   ├── dashboard/       # User management interface
    │   ├── freelancers/     # Freelancer catalog & search
    │   ├── home/            # Landing page
    │   ├── missions/        # Mission listings & details
    │   └── services/        # Backend API integration (HttpClient)
    └── tailwind.config.js   # UI Styling configuration
```

---

## 🛠️ Tech Stack

| Component | Technology |
| :--- | :--- |
| **Back-End** | Java 17, Spring Boot 3.5+, Spring Security |
| **Database** | MySQL 8 |
| **Front-End** | Angular 20, Tailwind CSS, TypeScript |
| **Tools** | Maven, Lombok, MapStruct |

---

## 🚀 How to Run

### Prerequisites
- **Java 17** or higher
- **Node.js 20+** and **npm**
- **MySQL 8** (or compatible)
- **Maven** (included with back/)

### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd back
   ```

2. Configure MySQL connection in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/nomad?createDatabaseIfNotExist=true
   spring.datasource.username=your_mysql_user
   spring.datasource.password=your_mysql_password
   ```

3. Build and run:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
   The API will be available at: **`http://localhost:8090`**

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd front
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```
   The application will be accessible at: **`http://localhost:4200`**

---

## ✨ Key Features
- 🔐 Secure authentication with role-based access (Freelancer/Recruiter)
- 💼 Freelancer profiles with portfolio showcase
- 📋 Mission listings with detailed descriptions
- 📝 Application management for freelancers
- ⭐ Review and rating system
- 🔍 Advanced search and filtering

---

## 👥 User Roles
- **Freelancer**: Create a profile, showcase portfolio, apply for missions, and receive feedback.
- **Recruiter**: Post missions, browse freelancer profiles, manage applications, and leave reviews.

---

## Contributing

We welcome contributions! Here's how you can help improve Nomad:

### Getting Started
1. **Fork** the repository
2. **Clone** your fork:
   ```bash
   git clone https://github.com/yourusername/Nomad.git
   cd Nomad
   ```
3. **Create a feature branch**:
   ```bash
   git checkout -b feature/amazing-feature
   ```

### Development Workflow
- Make your changes and test thoroughly
- Ensure code follows the project's conventions
- Commit with clear, descriptive messages:
  ```bash
  git commit -m "Add: amazing feature description"
  ```
- Push to your branch:
  ```bash
  git push origin feature/amazing-feature
  ```
- Submit a **Pull Request** with a detailed description

### Areas We'd Love Help With
- Bug fixes and performance improvements
- New features for freelancers and recruiters
- UI/UX enhancements
- Documentation and guides
- Testing coverage

---

## 📝 License

This project is licensed under the **MIT License** – feel free to use it for your projects!

---

## Authors & Contributors

**Nomad** was created by the @byeolunu and @iKawtar . We appreciate all contributions and feedback from the community!

Have questions? Feel free to open an **Issue** or reach out to the team.

---

**Happy Coding!**
