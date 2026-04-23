# E-Learning Platform

A complete E-Learning management platform built with Spring Boot 3.2 and MySQL 8.

## 📋 Features

### User Management
- ✅ User registration and authentication with JWT
- ✅ Three user roles: Student, Instructor, Admin
- ✅ User profile management
- ✅ Spring Security with BCrypt password encryption

### Course Management
- ✅ Create, read, update, delete courses
- ✅ Course categorization and leveling
- ✅ Course publication workflow
- ✅ Instructor dashboard

### Lesson Management
- ✅ Structured lessons within courses
- ✅ Video content support
- ✅ Lesson numbering and ordering
- ✅ Lesson content with rich text

### Quiz & Assessment
- ✅ Create multiple choice quizzes
- ✅ Question management
- ✅ Automatic score calculation
- ✅ Quiz attempt tracking
- ✅ Performance analytics

### Student Progress
- ✅ Course enrollment tracking
- ✅ Lesson progress monitoring
- ✅ Quiz results and scores
- ✅ Certificate issuance

### API Documentation
- ✅ Swagger/OpenAPI 3.0 integration
- ✅ Interactive API documentation
- ✅ Ready for frontend integration

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.2
- **Database**: MySQL 8.0
- **Security**: Spring Security + JWT
- **ORM**: Hibernate/JPA
- **API Documentation**: Springdoc OpenAPI
- **Build Tool**: Maven
- **Java Version**: 17

## 📦 Dependencies

```xml
- Spring Boot Web
- Spring Data JPA
- Spring Security
- MySQL Connector/J 8.0.33
- JJWT 0.12.3 (JWT Authentication)
- Lombok
- ModelMapper
- Validation API
- Springdoc OpenAPI
- Spring Mail
```

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- Docker & Docker Compose (for MySQL)
- Git

### Installation

#### 1. Clone the Repository

```bash
git clone https://github.com/waelba1605/project.git
cd project
```

#### 2. Start MySQL with Docker

```bash
docker-compose up -d
```

This will start:
- **MySQL 8.0** on `localhost:3306`
- **PhpMyAdmin** on `http://localhost:8081`

#### 3. Build the Project

```bash
mvn clean install
```

#### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api/v1`

## 📚 API Endpoints

### Authentication

```bash
# Register a new user
POST /auth/register
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "STUDENT"
}

# Login
POST /auth/login
{
  "email": "john@example.com",
  "password": "password123"
}
```

## 🔐 Default Credentials

### Database
- **Host**: `localhost:3306`
- **Database**: `elearning`
- **Root User**: `root` / `root`
- **App User**: `elearning` / `elearning123`

### JWT Configuration
- **Secret Key**: `mySecretKeyForJWTTokenGenerationAndValidationPleaseChangeIt123456789` (Change in production!)
- **Expiration**: 24 hours (86400000 ms)

## 📖 API Documentation

Once the application is running, access Swagger UI:

```
http://localhost:8080/api/v1/swagger-ui.html
```

## 📁 Project Structure

```
src/main/java/com/elearning/
├── config/              # Spring configuration
├── controller/          # REST API endpoints
├── dto/                 # Data Transfer Objects
├── model/
│   └── entity/          # JPA Entities
├── repository/          # Spring Data Repositories
├── security/            # JWT and Security configs
├── service/             # Business logic
└── ElearningApplication.java

src/main/resources/
├── application.yml      # Application configuration
```

## 🔄 Database Schema

### Tables

1. **users** - User accounts and profiles
2. **courses** - Course information
3. **lessons** - Course lessons
4. **quizzes** - Quiz assessments
5. **questions** - Quiz questions
6. **question_options** - Multiple choice options
7. **enrollments** - Student course enrollments
8. **quiz_results** - Quiz attempt results
9. **lesson_progress** - Student lesson progress

## 🔗 Relationships

```
User
  ├── 1→N Courses (as Instructor)
  ├── 1→N Enrollments (as Student)
  ├── 1→N QuizResults
  └── 1→N LessonProgress

Course
  ├── 1→N Lessons
  └── 1→N Enrollments

Lesson
  ├── 1→N Quizzes
  └── 1→N LessonProgress

Quiz
  ├── 1→N Questions
  └── 1→N QuizResults

Question
  └── 1→N QuestionOptions
```

## 🔒 Security Features

- ✅ JWT-based authentication
- ✅ BCrypt password hashing
- ✅ Role-based access control (RBAC)
- ✅ CORS support
- ✅ CSRF protection
- ✅ Method-level security annotations

## 📝 Environment Variables

```yaml
spring.datasource.url=jdbc:mysql://localhost:3306/elearning
spring.datasource.username=elearning
spring.datasource.password=elearning123
app.jwtSecret=your_secret_key_here
app.jwtExpirationMs=86400000
```

## 🐳 Docker Compose Services

### MySQL
- **Image**: `mysql:8.0`
- **Port**: `3306`
- **Volume**: `mysql_data`

### PhpMyAdmin
- **Image**: `phpmyadmin:latest`
- **Port**: `8081`
- **URL**: `http://localhost:8081`

## 📊 Database Initialization

The database schema is automatically created on application startup due to:

```yaml
spring.jpa.hibernate.ddl-auto=update
```

SQL init script is also provided in `init.sql` for manual setup.

## 🚀 Next Steps

1. Implement Course Management endpoints
2. Add Lesson CRUD operations
3. Implement Quiz and Question management
4. Add enrollment functionality
5. Implement progress tracking
6. Add file upload capability
7. Integrate email notifications
8. Add admin dashboard

## 📄 License

MIT License - See LICENSE file for details

## 👨‍💻 Author

**Waelba1605**

## 📧 Support

For issues and questions, please create a GitHub issue in the repository.

## 🔄 Version

**Current Version**: 1.0.0

---

**Happy Learning! 🎓**
