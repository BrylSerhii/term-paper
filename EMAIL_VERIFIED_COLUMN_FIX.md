# Email Verified Column Fix

## Issue Description

The error message:
```
[2025-05-25 14:29:51] [42703] ERROR: column "email_verified" of relation "users" does not exist
[2025-05-25 14:29:51] Position: 47
```

This error occurs because the `email_verified` column is missing from the `users` table in your database, despite being defined in:
1. The Java entity class (`User.java`)
2. The SQL creation script (`create_tables.sql`)

## Cause

This issue can occur if:
- The database was created before the `email_verified` column was added to the entity
- The database was created manually without including this column
- The column was accidentally dropped or renamed

## Solution

A fix script has been created to add the missing column. Follow these steps:

### Option 1: Using IntelliJ IDEA Database Tools

1. Open IntelliJ IDEA
2. Go to View -> Tool Windows -> Database
3. Right-click on your database connection and select "New" -> "Console File"
4. Load the `fix_users_table.sql` file and execute it

### Option 2: Using psql Command Line

For local PostgreSQL:
```bash
psql -U postgres -d studyplatform -f fix_users_table.sql
```

For Docker setup:
```bash
psql -U postgres -h localhost -p 5433 -d studyplatform -f fix_users_table.sql
```

## What the Fix Does

The script:
1. Checks if the `email_verified` column exists in the `users` table
2. If it doesn't exist, adds the column with a `NOT NULL` constraint and a `DEFAULT FALSE` value
3. Updates any existing users to have `email_verified` set to `true` (to ensure they can still log in)

## Verifying the Fix

After running the fix script, try inserting a user with the `email_verified` column:

```sql
INSERT INTO users (username, email, password, email_verified, role)
VALUES (
    'teacher',
    'teacher@studyplatform.com',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', -- teacher123 encoded with BCrypt
    true,
    'TEACHER'
);
```

If the insert succeeds, the issue has been fixed.