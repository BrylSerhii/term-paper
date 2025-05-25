# Database Guide for Study Platform

This guide provides instructions on how to set up and access the database for the Study Platform application using IntelliJ IDEA.

## Database Schema

The Study Platform uses a PostgreSQL database with the following tables:

1. **users** - Stores user information with role-based access control
   - Roles: STUDENT, TEACHER, ADMIN
   - Only students can self-register
   - Teachers and admins must be created manually or programmatically

2. **courses** - Stores course information

3. **lessons** - Stores lesson information for courses

4. **enrollments** - Tracks student enrollments in courses

5. **progress** - Tracks student progress through lessons

6. **materials** - Stores educational content (text and video links)
   - Only teachers and admins can create/manage materials

7. **assignments** - Stores text-based tasks created by teachers
   - Only teachers and admins can create/manage assignments

8. **grades** - Stores student grades for completed assignments
   - Only teachers and admins can grade assignments

## Setting Up Database Connection in IntelliJ IDEA

### Prerequisites

- IntelliJ IDEA Ultimate Edition (the Database tools are not available in the Community Edition)
- PostgreSQL JDBC driver (automatically included in the project dependencies)

### Steps to Connect to the Database

1. **Open Database Tool Window**
   - Click on the "Database" tab on the right side of the IntelliJ IDEA window
   - If you don't see it, go to View -> Tool Windows -> Database

2. **Add New Data Source**
   - Click the "+" button in the Database tool window
   - Select "PostgreSQL"

3. **Configure Connection**
   - Name: Study Platform
   - Host: localhost (or the Docker container IP if using Docker)
   - Port: 5432 (or 5433 if using the Docker setup)
   - Database: studyplatform
   - User: postgres
   - Password: postgres (or postgres_password if using Docker)
   - URL: jdbc:postgresql://localhost:5432/studyplatform (will be auto-generated)

4. **Test Connection**
   - Click the "Test Connection" button to verify the connection works
   - If successful, click "Apply" and then "OK"

## Viewing and Modifying Data

Once connected, you can:

1. **Browse Tables**
   - Expand the database connection in the Database tool window
   - Expand the "studyplatform" database
   - Expand the "public" schema to see all tables

2. **View Table Data**
   - Right-click on a table and select "Table Editor"
   - This opens a spreadsheet-like interface to view and edit data

3. **Execute SQL Queries**
   - Click the "+" button in the Database tool window and select "Console"
   - Write and execute SQL queries against the database

## Creating Teachers and Admins

Since teachers and admins cannot self-register, they must be created manually. You can do this in several ways:

### Default Development Users

In the development environment, the application automatically creates default admin and teacher users if none exist:

- **Admin User**:
  - Username: admin
  - Email: admin@studyplatform.com
  - Password: admin123

- **Teacher User**:
  - Username: teacher
  - Email: teacher@studyplatform.com
  - Password: teacher123

These default users are created by the `DataInitializer` class and are intended for development and testing purposes only. In a production environment, you should create secure admin and teacher accounts using one of the following methods:

### Option 1: Using SQL

Execute the following SQL in the database console:

```sql
INSERT INTO users (username, email, password, email_verified, role)
VALUES (
    'teacher1',
    'teacher1@example.com',
    '$2a$10$YourBcryptHashHere', -- Generate this using BCrypt
    true,
    'TEACHER'
);
```

### Option 2: Using the AuthenticationService

Create a simple admin controller or use the application's initialization to call the `createStaffUser` method:

```java
@PostConstruct
public void initializeAdminUser() {
    try {
        authenticationService.createStaffUser(
            "admin",
            "admin@example.com",
            "securePassword123",
            User.Role.ADMIN
        );
    } catch (IllegalArgumentException e) {
        // Admin already exists, ignore
    }
}
```

## Database Maintenance

### Backup

To backup the database:

```bash
pg_dump -U postgres -h localhost -p 5432 studyplatform > backup.sql
```

### Restore

To restore from a backup:

```bash
psql -U postgres -h localhost -p 5432 studyplatform < backup.sql
```

## Troubleshooting

1. **Connection Refused**
   - Ensure PostgreSQL is running
   - Check the port number (5432 for local, 5433 for Docker)
   - Verify the credentials

2. **Table Not Found**
   - The application uses Hibernate's `ddl-auto: update` setting, which automatically creates tables
   - Make sure the application has been started at least once

3. **Permission Denied**
   - Ensure the database user has appropriate permissions
   - For Docker setup, the credentials are defined in docker-compose.yml
