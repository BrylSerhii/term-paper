# Study Platform

A comprehensive online learning platform built with Spring Boot and modern web technologies.

## Overview

Study Platform is a web application that allows users to browse, enroll in, and complete online courses. The platform includes features for user authentication, course management, lesson tracking, and progress monitoring.

## Features

- User registration and authentication
- Course browsing and enrollment
- Lesson completion tracking
- Progress monitoring
- Responsive design for desktop and mobile devices

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Security with JWT authentication
- Spring Data JPA
- PostgreSQL database
- Maven build system

### Frontend
- HTML5
- CSS3
- JavaScript (ES6+)
- Responsive design

## Project Structure

The project follows a standard Spring Boot application structure:

```
study-platform/
├── src/
│   ├── main/
│   │   ├── java/            # Java source code
│   │   │   └── com/
│   │   │       └── studyplatform/
│   │   │           ├── config/       # Configuration classes
│   │   │           ├── controller/   # REST controllers
│   │   │           ├── dto/          # Data Transfer Objects
│   │   │           ├── exception/    # Exception handling
│   │   │           ├── model/        # JPA entities
│   │   │           ├── repository/   # Spring Data repositories
│   │   │           ├── security/     # Security configuration
│   │   │           └── service/      # Business logic
│   │   └── resources/
│   │       ├── static/       # Static web resources
│   │       │   ├── css/      # CSS stylesheets
│   │       │   ├── js/       # JavaScript files
│   │       │   └── images/   # Image assets
│   │       └── application.yml  # Application configuration
│   └── test/                 # Test code
└── pom.xml                   # Maven configuration
```

## Frontend Integration

The application integrates a frontend HTML website with the Spring Boot backend:

1. **Static Resources**: HTML, CSS, and JavaScript files are stored in the `src/main/resources/static` directory.

2. **Single Page Application Support**: The application is configured to support SPA routing, forwarding non-API requests to the index.html file.

3. **API Communication**: The frontend JavaScript communicates with the backend REST API using fetch requests to endpoints under the `/api` path.

4. **Resource Handling**: Static resources are served directly by Spring Boot, with custom configuration in the `WebConfig` class.

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL (or Docker for containerized setup)

### Using Maven
1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```
   mvn spring-boot:run
   ```
4. Access the application at http://localhost:8080

### Using Docker
1. Clone the repository
2. Navigate to the project directory
3. Build and start the containers:
   ```
   docker-compose up -d
   ```
4. Access the application at http://localhost:8080

## API Endpoints

The application provides the following API endpoints:

- **Health Check**: `GET /api/health` - Verify the API is running
- **Authentication**:
  - `POST /api/auth/register` - Register a new user
  - `POST /api/auth/login` - Authenticate and get JWT token
- **Courses**:
  - `GET /api/courses` - Get all courses
  - `GET /api/courses/{id}` - Get course details
- **Enrollments**:
  - `POST /api/enrollments` - Enroll in a course
  - `GET /api/enrollments` - Get user enrollments
- **Progress**:
  - `POST /api/progress` - Mark lesson as completed
  - `GET /api/progress/{enrollmentId}` - Get progress for enrollment

## Development

### Building the Project
```
mvn clean package
```

### Running Tests
```
mvn test
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.