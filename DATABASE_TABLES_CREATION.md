# Creating Database Tables for Study Platform

This guide provides instructions on how to execute the SQL script to create all the necessary tables for the Study Platform application.

## Prerequisites

1. Make sure you have a working database connection as described in the DATABASE_SETUP.md and DATABASE_CONNECTION_FIX.md guides
2. Ensure PostgreSQL is running (either locally or in Docker)
3. The create_tables.sql file is available in your project

## Option 1: Using IntelliJ IDEA Database Tools

### Step 1: Open Database Console

1. Open IntelliJ IDEA
2. Go to View -> Tool Windows -> Database
3. Right-click on your database connection (studyplatform@localhost)
4. Select "New" -> "Console File"

### Step 2: Execute the SQL Script

1. In the console window, click on the folder icon in the toolbar
2. Navigate to and select the create_tables.sql file
3. The SQL script will be loaded into the console
4. Click the "Run" button (green triangle) to execute the entire script
5. Alternatively, you can select specific statements and execute only those by selecting them and clicking "Run"

## Option 2: Using psql Command Line Tool

### For Local PostgreSQL

1. Open a command prompt or terminal
2. Navigate to the directory containing the create_tables.sql file
3. Run the following command:

```bash
psql -U postgres -d studyplatform -f create_tables.sql
```

4. Enter your PostgreSQL password when prompted

### For Docker Setup

1. Open a command prompt or terminal
2. Navigate to the directory containing the create_tables.sql file
3. Run the following command:

```bash
psql -U postgres -h localhost -p 5433 -d studyplatform -f create_tables.sql
```

4. Enter your PostgreSQL password (postgres_password as specified in docker-compose.yml) when prompted

## Option 3: Using the Application's Auto-Creation Feature

The Study Platform application is configured to automatically create the necessary tables when it starts up, using Hibernate's `ddl-auto: update` setting. To use this feature:

1. Make sure your database connection is properly configured in application.yml and application-dev.yml
2. Start the application using one of the following methods:
   - Run the main class (StudyPlatformApplication.java) in IntelliJ IDEA
   - Use Maven: `mvn spring-boot:run`
   - Use Docker Compose: `docker-compose up -d`

## Verifying Table Creation

To verify that the tables have been created successfully:

1. Open IntelliJ IDEA
2. Go to View -> Tool Windows -> Database
3. Expand your database connection (studyplatform@localhost)
4. Expand the "studyplatform" database
5. Expand the "public" schema
6. You should see all the tables listed: users, courses, lessons, enrollments, progress, materials, assignments, grades, and verification_token

If you don't see the tables, try refreshing the database connection by right-clicking on it and selecting "Refresh".

## Troubleshooting

### Tables Already Exist

If you get errors about tables already existing, you can drop all tables and recreate them:

```sql
DROP TABLE IF EXISTS verification_token, grades, assignments, materials, progress, enrollments, lessons, courses, users CASCADE;
```

Then run the create_tables.sql script again.

### Permission Denied

If you get permission errors:

1. Make sure you're using the correct username and password
2. Verify that the user has the necessary permissions to create tables
3. For Docker setup, check the credentials in docker-compose.yml

### Foreign Key Constraints

The script creates tables with foreign key constraints, so they must be created in the correct order. The create_tables.sql file is already arranged in the proper order.

## Conclusion

You should now have all the necessary tables created in your database. You can proceed to use the Study Platform application, which will automatically populate these tables with data as you interact with it.