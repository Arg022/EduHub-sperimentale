-- Enums
CREATE TYPE user_role AS ENUM ('student', 'teacher', 'admin');
CREATE TYPE enrollment_status AS ENUM ('pending', 'active', 'completed', 'cancelled');
CREATE TYPE attendance_mode AS ENUM ('in_person', 'online');
CREATE TYPE notification_type AS ENUM ('system', 'course', 'quiz', 'material');
CREATE TYPE notification_priority AS ENUM ('low', 'medium', 'high');
CREATE TYPE question_type AS ENUM ('multiple_choice', 'true_false', 'open_ended');
CREATE TYPE course_level AS ENUM ('beginner', 'intermediate', 'advanced');

-- Tables
CREATE TABLE user_account (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role user_role NOT NULL,
    registration_date DATE NOT NULL DEFAULT CURRENT_DATE,
    phone VARCHAR(20),
    address TEXT,
    last_access TIMESTAMP,
    CONSTRAINT email_check CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$')
);

CREATE TABLE course (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    syllabus TEXT,
    max_students INTEGER,
    level course_level NOT NULL,
    CONSTRAINT date_check CHECK (end_date > start_date)
);

CREATE TABLE subject (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE lesson (
    id UUID PRIMARY KEY,
    course_id UUID NOT NULL REFERENCES course(id),
    subject_id UUID NOT NULL REFERENCES subject(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CONSTRAINT time_check CHECK (end_time > start_time)
);

CREATE TABLE enrollment (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES user_account(id),
    course_id UUID NOT NULL REFERENCES course(id),
    enrollment_date DATE NOT NULL DEFAULT CURRENT_DATE,
    status enrollment_status NOT NULL DEFAULT 'pending',
    UNIQUE(user_id, course_id)
);

CREATE TABLE teaching (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES user_account(id),
    subject_id UUID NOT NULL REFERENCES subject(id),
    course_id UUID NOT NULL REFERENCES course(id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    CONSTRAINT date_check CHECK (end_date > start_date),
    UNIQUE(user_id, subject_id, course_id)
);

CREATE TABLE attendance (
    id UUID PRIMARY KEY,
    lesson_id UUID NOT NULL REFERENCES lesson(id),
    user_id UUID NOT NULL REFERENCES user_account(id),
    present BOOLEAN NOT NULL DEFAULT false,
    mode attendance_mode NOT NULL,
    notes TEXT,
    record_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(lesson_id, user_id)
);

CREATE TABLE quiz (
    id UUID PRIMARY KEY,
    course_id UUID NOT NULL REFERENCES course(id),
    creator_id UUID NOT NULL REFERENCES user_account(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration_minutes INTEGER NOT NULL,
    max_attempts INTEGER NOT NULL DEFAULT 1,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    publication_date TIMESTAMP
);

CREATE TABLE question (
    id UUID PRIMARY KEY,
    quiz_id UUID NOT NULL REFERENCES quiz(id),
    text TEXT NOT NULL,
    score FLOAT NOT NULL,
    question_type question_type NOT NULL
);

CREATE TABLE answer (
    id UUID PRIMARY KEY,
    question_id UUID NOT NULL REFERENCES question(id),
    text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL
);

CREATE TABLE quiz_result (
    id UUID PRIMARY KEY,
    quiz_id UUID NOT NULL REFERENCES quiz(id),
    user_id UUID NOT NULL REFERENCES user_account(id),
    total_score FLOAT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    completion_time TIMESTAMP NOT NULL,
    time_spent INTEGER NOT NULL,
    CONSTRAINT time_check CHECK (time_spent > 0)
);

CREATE TABLE student_answer (
    id UUID PRIMARY KEY,
    quiz_result_id UUID NOT NULL REFERENCES quiz_result(id),
    question_id UUID NOT NULL REFERENCES question(id),
    answer_id UUID REFERENCES answer(id),
    score_obtained FLOAT NOT NULL
);

CREATE TABLE notification (
    id UUID PRIMARY KEY,
    sender_id UUID NOT NULL REFERENCES user_account(id),
    recipient_id UUID NOT NULL REFERENCES user_account(id),
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    notification_type notification_type NOT NULL,
    priority notification_priority NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_date TIMESTAMP
);

-- Indexes
CREATE INDEX idx_user_email ON user_account(email);
CREATE INDEX idx_course_dates ON course(start_date, end_date);
CREATE INDEX idx_lesson_course ON lesson(course_id);
CREATE INDEX idx_enrollment_course ON enrollment(course_id);
CREATE INDEX idx_enrollment_user ON enrollment(user_id);
CREATE INDEX idx_attendance_lesson ON attendance(lesson_id);
CREATE INDEX idx_quiz_course ON quiz(course_id);
CREATE INDEX idx_notification_recipient ON notification(recipient_id);