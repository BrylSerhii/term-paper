# PostgreSQL Password Information

## Your PostgreSQL Password

The password for your PostgreSQL database depends on whether you're using a local PostgreSQL installation or the Docker setup:

### For Local PostgreSQL Installation

If you're running PostgreSQL directly on your machine (not through Docker), your password is:

```
postgres
```

This is the default password specified in `application-dev.yml`.

### For Docker Setup

If you're using Docker (via docker-compose), your password is:

```
postgres_password
```

This is specified in the `docker-compose.yml` file.

## How to Determine Which Setup You're Using

You can determine which setup you're using based on:

1. **Port number**: 
   - Local PostgreSQL typically uses port 5432
   - Docker setup uses port 5433 (as configured in docker-compose.yml)

2. **How you started PostgreSQL**:
   - If you started PostgreSQL using the Windows service or a direct installation, you're using the local setup
   - If you started PostgreSQL using `docker-compose up -d`, you're using the Docker setup

## Using the Password with create_tables.sql

### When Using IntelliJ IDEA Database Tools

1. Make sure your database connection is configured with the correct password:
   - Go to View -> Tool Windows -> Database
   - Right-click on your database connection and select "Properties"
   - Verify the password field contains the correct password for your setup
   - Click "Test Connection" to verify it works
   - Click "Apply" and "OK"

2. Then follow the instructions in DATABASE_TABLES_CREATION.md to execute the script.

### When Using psql Command Line

For local PostgreSQL:
```bash
psql -U postgres -d studyplatform -f create_tables.sql
```

When prompted for a password, enter: `postgres`

For Docker setup:
```bash
psql -U postgres -h localhost -p 5433 -d studyplatform -f create_tables.sql
```

When prompted for a password, enter: `postgres_password`

## Where These Passwords Are Defined

1. **Local PostgreSQL password** is defined in:
   - `src/main/resources/application-dev.yml` (line 5): `password: ${DB_PASSWORD:postgres}`

2. **Docker PostgreSQL password** is defined in:
   - `docker-compose.yml` (line 34): `POSTGRES_PASSWORD=postgres_password`
   - `docker-compose.yml` (line 16): `DB_PASSWORD=postgres_password`

## Troubleshooting

If you're still having issues connecting:

1. Verify PostgreSQL is running
2. Check if you're using the correct port (5432 for local, 5433 for Docker)
3. Make sure you're using the correct password for your setup
4. Check the DATABASE_CONNECTION_FIX.md file for additional troubleshooting steps