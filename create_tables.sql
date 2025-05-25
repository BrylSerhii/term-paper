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

-- Create default admin user
INSERT INTO users (username, email, password, email_verified, role)
VALUES (
    'admin',
    'admin@studyplatform.com',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', -- admin123 encoded with BCrypt
    true,
    'ADMIN'
);

-- Create default teacher user
INSERT INTO users (username, email, password, email_verified, role)
VALUES (
    'teacher',
    'teacher@studyplatform.com',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', -- teacher123 encoded with BCrypt
    true,
    'TEACHER'
);