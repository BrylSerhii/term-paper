# Database Setup Guide for Study Platform

## Introduction

This guide will help you set up and access the database for the Study Platform application using IntelliJ IDEA. The application uses PostgreSQL as its database system.

## Prerequisites

1. IntelliJ IDEA Ultimate Edition (the Database tools are not available in the Community Edition)
2. PostgreSQL installed locally or running in Docker
3. The Study Platform application code

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

9. **verification_token** - Stores tokens for email verification

## Setting Up Database Connection in IntelliJ IDEA

### Step 1: Start the PostgreSQL Database

If you're using Docker, make sure the Docker container is running:

```bash
docker-compose up -d
```

If you're using a local PostgreSQL installation, ensure it's running.

### Step 2: Open Database Tool Window in IntelliJ IDEA

1. Click on the "Database" tab on the right side of the IntelliJ IDEA window
2. If you don't see it, go to View -> Tool Windows -> Database

### Step 3: Add New Data Source

1. Click the "+" button in the Database tool window
2. Select "PostgreSQL" from the dropdown menu

### Step 4: Configure Connection Settings

Fill in the following details:
- **Name**: Study Platform
- **Host**: localhost (or the Docker container IP if using Docker)
- **Port**: 5432 (for local PostgreSQL) or 5433 (if using Docker as specified in docker-compose.yml)
- **Database**: studyplatform
- **User**: postgres
- **Password**: postgres (for local PostgreSQL) or postgres_password (if using Docker as specified in docker-compose.yml)
- **URL**: jdbc:postgresql://localhost:5432/studyplatform (for local PostgreSQL) or jdbc:postgresql://localhost:5433/studyplatform (if using Docker)

### Step 5: Test Connection

1. Click the "Test Connection" button to verify the connection works
2. If successful, click "Apply" and then "OK"

### Step 6: View Database Tables

1. Expand the database connection in the Database tool window
2. Expand the "studyplatform" database
3. Expand the "public" schema to see all tables

If you don't see any tables, make sure:
- The application has been started at least once (tables are created automatically)
- You have the correct database connection settings
- The database user has appropriate permissions

## Viewing and Editing Table Data

### View Table Data

1. Right-click on a table (e.g., "users")
2. Select "Table Editor" from the context menu
3. This opens a spreadsheet-like interface to view and edit data

### Execute SQL Queries

1. Click the "+" button in the Database tool window and select "Console"
2. Write and execute SQL queries against the database

Example queries:

```sql
-- View all users
SELECT * FROM users;

-- View all courses
SELECT * FROM courses;

-- View enrollments with user and course information
SELECT e.id, u.username, c.title 
FROM enrollments e
JOIN users u ON e.user_id = u.id
JOIN courses c ON e.course_id = c.id;
```

## Creating Tables Manually (If Needed)

If the tables are not automatically created, you can create them manually using the following SQL script:

```sql
-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    role VARCHAR(20) NOT NULL
);

-- Create courses table
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL
);

-- Create lessons table
CREATE TABLE lessons (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL REFERENCES courses(id),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    video_url VARCHAR(255) NOT NULL,
    duration_minutes INTEGER NOT NULL,
    order_in_course INTEGER NOT NULL
);

-- Create enrollments table
CREATE TABLE enrollments (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    course_id BIGINT NOT NULL REFERENCES courses(id),
    enrolled_at TIMESTAMP NOT NULL,
    UNIQUE(user_id, course_id)
);

-- Create progress table
CREATE TABLE progress (
    id BIGSERIAL PRIMARY KEY,
    enrollment_id BIGINT NOT NULL REFERENCES enrollments(id),
    lesson_id BIGINT NOT NULL REFERENCES lessons(id),
    completed_at TIMESTAMP NOT NULL,
    UNIQUE(enrollment_id, lesson_id)
);

-- Create materials table
CREATE TABLE materials (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    text_content TEXT NOT NULL,
    video_url VARCHAR(255),
    lesson_id BIGINT NOT NULL REFERENCES lessons(id),
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL
);

-- Create assignments table
CREATE TABLE assignments (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    task_content TEXT NOT NULL,
    max_points INTEGER NOT NULL,
    due_date TIMESTAMP NOT NULL,
    lesson_id BIGINT NOT NULL REFERENCES lessons(id),
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL
);

-- Create grades table
CREATE TABLE grades (
    id BIGSERIAL PRIMARY KEY,
    assignment_id BIGINT NOT NULL REFERENCES assignments(id),
    student_id BIGINT NOT NULL REFERENCES users(id),
    points INTEGER NOT NULL,
    feedback TEXT,
    graded_by BIGINT NOT NULL REFERENCES users(id),
    graded_at TIMESTAMP NOT NULL,
    submitted_at TIMESTAMP NOT NULL,
    UNIQUE(assignment_id, student_id)
);

-- Create verification_token table
CREATE TABLE verification_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL REFERENCES users(id),
    expiry_date TIMESTAMP NOT NULL
);
```

## Creating Default Users

The application automatically creates default admin and teacher users in the development environment:

- **Admin User**:
  - Username: admin
  - Email: admin@studyplatform.com
  - Password: admin123

- **Teacher User**:
  - Username: teacher
  - Email: teacher@studyplatform.com
  - Password: teacher123

You can also create users manually using SQL:

```sql
-- Create an admin user (password should be BCrypt encoded in production)
INSERT INTO users (username, email, password, email_verified, role)
VALUES (
    'admin2',
    'admin2@example.com',
    '$2a$10$YourBcryptHashHere', -- Generate this using BCrypt
    true,
    'ADMIN'
);

-- Create a teacher user
INSERT INTO users (username, email, password, email_verified, role)
VALUES (
    'teacher2',
    'teacher2@example.com',
    '$2a$10$YourBcryptHashHere', -- Generate this using BCrypt
    true,
    'TEACHER'
);
```

## Troubleshooting

### Tables Not Visible

If you can't see the tables in the Database tool window:

1. **Refresh the connection**: Right-click on the database connection and select "Refresh"
2. **Check schema**: Make sure you're looking in the correct schema (usually "public")
3. **Verify application startup**: The application needs to run at least once to create tables
4. **Check logs**: Look for any database-related errors in the application logs

### Connection Issues

If you can't connect to the database:

1. **Check PostgreSQL service**: Ensure PostgreSQL is running
2. **Verify credentials**: Double-check username and password
3. **Check port**: Make sure you're using the correct port (5432 by default)
4. **Database existence**: Ensure the "studyplatform" database exists

### Creating the Database Manually

If the database doesn't exist, you can create it manually:

```sql
CREATE DATABASE studyplatform;
```

## Conclusion

You should now be able to view and edit the database tables for the Study Platform application using IntelliJ IDEA. If you encounter any issues, refer to the troubleshooting section or check the application logs for more information.
