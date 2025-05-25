# Study Platform Database Setup

This directory contains files related to setting up and managing the database for the Study Platform application.

## Files

- **create_tables.sql**: SQL script to create all the necessary tables for the Study Platform application
- **DATABASE_SETUP.md**: Comprehensive guide for setting up and accessing the database
- **DATABASE_CONNECTION_FIX.md**: Instructions for fixing database connection issues in IntelliJ IDEA
- **DATABASE_TABLES_CREATION.md**: Step-by-step guide for executing the SQL script to create tables

## Quick Start

To quickly create all the necessary database tables:

1. Make sure PostgreSQL is running (either locally or in Docker)
2. Open IntelliJ IDEA and go to View -> Tool Windows -> Database
3. Right-click on your database connection and select "New" -> "Console File"
4. Load the create_tables.sql file and execute it

For more detailed instructions, refer to DATABASE_TABLES_CREATION.md.

## Database Schema

The Study Platform uses a PostgreSQL database with the following tables:

1. **users** - Stores user information with role-based access control
2. **courses** - Stores course information
3. **lessons** - Stores lesson information for courses
4. **enrollments** - Tracks student enrollments in courses
5. **progress** - Tracks student progress through lessons
6. **materials** - Stores educational content (text and video links)
7. **assignments** - Stores text-based tasks created by teachers
8. **grades** - Stores student grades for completed assignments
9. **verification_token** - Stores tokens for email verification

## Default Users

The create_tables.sql script creates the following default users:

- **Admin User**:
  - Username: admin
  - Email: admin@studyplatform.com
  - Password: admin123

- **Teacher User**:
  - Username: teacher
  - Email: teacher@studyplatform.com
  - Password: teacher123

These default users are intended for development and testing purposes only.