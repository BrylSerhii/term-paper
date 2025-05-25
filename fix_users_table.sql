-- Add email_verified column to users table if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'users'
        AND column_name = 'email_verified'
    ) THEN
        ALTER TABLE users ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT FALSE;
    END IF;
END $$;

-- Update existing users to have email_verified set to true
-- This ensures existing users can still log in
UPDATE users SET email_verified = true WHERE email_verified IS NULL;