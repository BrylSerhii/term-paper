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

## User Registration System

The platform implements a secure user registration system with the following features:

### Registration Process
1. Users enter a username, email, and password
2. Password requirements:
   - Minimum 6 characters
   - Must contain only English letters (a-z, A-Z) and digits (0-9)
3. Client-side validation ensures password requirements are met before submission
4. Server-side validation provides additional security

### Email Verification
1. After registration, a verification email is sent to the user's email address
2. The email contains a unique verification link that expires after 60 minutes
3. Users must click the verification link to activate their account
4. Accounts remain inactive until email verification is completed

### Security Features
- Passwords are securely hashed using BCrypt
- JWT (JSON Web Token) authentication for secure API access
- Protection against common security vulnerabilities

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Security with JWT authentication
- Spring Data JPA
- PostgreSQL database
- Maven build system

## Database Schema

The application uses a relational database with the following structure:

1. **Users** - Stores user information with role-based access control
   - Roles: STUDENT, TEACHER, ADMIN
   - Only students can self-register
   - Teachers and admins must be created manually

2. **Courses** - Stores course information

3. **Lessons** - Stores lesson information for courses

4. **Enrollments** - Tracks student enrollments in courses

5. **Progress** - Tracks student progress through lessons

6. **Materials** - Stores educational content (text and video links)
   - Only teachers and admins can create/manage materials

7. **Assignments** - Stores text-based tasks created by teachers
   - Only teachers and admins can create/manage assignments

8. **Grades** - Stores student grades for completed assignments
   - Only teachers and admins can grade assignments

For detailed information on how to access and manage the database, see [DATABASE_GUIDE.md](DATABASE_GUIDE.md).

### Frontend
- HTML5
- CSS3
- JavaScript (ES6+)
- Responsive design

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
