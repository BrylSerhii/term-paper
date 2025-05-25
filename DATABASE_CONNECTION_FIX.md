# Database Connection Fix for IntelliJ IDEA

## Issue Description

The error message "The server requested SCRAM-based authentication, but no password was provided" indicates that IntelliJ IDEA is trying to connect to the PostgreSQL database without providing a password. This is causing JPA-QL queries to fail.

## Solution

You need to update the database connection settings in IntelliJ IDEA to include the correct password. Follow these steps:

1. Open IntelliJ IDEA
2. Go to View -> Tool Windows -> Database
3. Find the data source named "studyplatform@localhost" in the Database tool window
4. Right-click on it and select "Properties" or "Edit"
5. In the data source properties dialog, check the following settings:
   - Host: localhost
   - Port: 5432 (for local PostgreSQL) or 5433 (if using Docker)
   - Database: studyplatform
   - User: postgres
   - Password: postgres (for local PostgreSQL) or postgres_password (if using Docker)
   
6. Make sure the password field is filled in with the correct password:
   - If you're using a local PostgreSQL installation, the password should be "postgres" (or whatever you set during installation)
   - If you're using Docker, the password should be "postgres_password" as specified in docker-compose.yml

7. Click "Test Connection" to verify that the connection works
8. Click "Apply" and then "OK" to save the changes

## Verifying the Fix

After updating the connection settings, try running your JPA-QL query again:

```
select entity from User entity
```

The query should now execute successfully without the authentication error.

## Additional Notes

If you're switching between local PostgreSQL and Docker, remember to update the following settings:

1. **Port**:
   - Local PostgreSQL: 5432
   - Docker: 5433

2. **Password**:
   - Local PostgreSQL: postgres (or your custom password)
   - Docker: postgres_password

The URL should be updated accordingly:
   - Local PostgreSQL: jdbc:postgresql://localhost:5432/studyplatform
   - Docker: jdbc:postgresql://localhost:5433/studyplatform