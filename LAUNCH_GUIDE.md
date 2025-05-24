# Study Platform - Launch Guide

This guide will help you set up and run the Study Platform application. You can choose between Docker (recommended) or Maven approaches.

## Prerequisites

- Java 17 or higher
- Maven (if not using Docker)
- Docker and Docker Compose (if using Docker)
- PostgreSQL (if not using Docker)

## Option 1: Using Docker (Recommended)

Docker is the easiest way to run the application as it automatically sets up both the application and the PostgreSQL database.

### Steps:

1. **Clone the repository** (if you haven't already)

2. **Navigate to the project directory**
   ```
   cd path\to\term_paper
   ```

3. **Build and start the containers**
   ```
   docker-compose up -d
   ```
   This command:
   - Builds the application container
   - Creates a PostgreSQL database container
   - Sets up the network between them
   - Configures all necessary environment variables
   - Runs everything in detached mode (-d flag)

4. **Verify the containers are running**
   ```
   docker ps
   ```
   You should see two containers: the application and PostgreSQL.

5. **Access the application**
   - Web interface: http://localhost:8080
   - API endpoints: http://localhost:8080/api
   - API documentation: http://localhost:8080/api/swagger-ui.html

6. **Stop the containers** (when you're done)
   ```
   docker-compose down
   ```

## Option 2: Using Maven

If you prefer not to use Docker, you can run the application using Maven, but you'll need to set up PostgreSQL separately.

### Steps:

1. **Install and configure PostgreSQL**
   - Install PostgreSQL from https://www.postgresql.org/download/
   - Create a database named `studyplatform`
   - Create a user with username `postgres` and password `postgres` (or use your own credentials and update the application configuration)

2. **Configure the application**
   - If you're using different database credentials, update them in `src\main\resources\application-dev.yml` or set environment variables:
     - DB_HOST (default: localhost)
     - DB_PORT (default: 5432)
     - DB_NAME (default: studyplatform)
     - DB_USERNAME (default: postgres)
     - DB_PASSWORD (default: postgres)
     - JWT_SECRET (set a secure string for production)

3. **Build and run the application**
   ```
   mvn spring-boot:run
   ```

4. **Access the application**
   - Web interface: http://localhost:8080
   - API endpoints: http://localhost:8080/api
   - API documentation: http://localhost:8080/api/swagger-ui.html

## Database Schema

The application uses Hibernate's `ddl-auto: update` setting, which automatically creates and updates the database schema based on the entity classes. No manual schema creation is needed.

## Troubleshooting

### Docker Issues
- If containers fail to start, check logs: `docker-compose logs`
- If port conflicts occur, modify the port mappings in docker-compose.yml
- If you encounter the error `failed to solve: process "/bin/sh -c mvn package -DskipTests" did not complete successfully: exit code: 1`, check for duplicate main application classes. There should only be one `StudyPlatformApplication.java` file in the `com.studyplatform` package. Remove any duplicate files in other packages (e.g., `com.term_paper.studyplatform`).

### Database Connection Issues
- Verify PostgreSQL is running: `pg_isready -h localhost -p 5432`
- Check database credentials in application-dev.yml
- Ensure the database exists: `psql -U postgres -c "SELECT datname FROM pg_database WHERE datname='studyplatform'"`

### Application Issues
- Check application logs for errors
- Verify Java version: `java -version` (should be 17 or higher)
- Ensure Maven is installed correctly: `mvn -v`
