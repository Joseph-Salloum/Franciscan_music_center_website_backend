CREATE TABLE IF NOT EXISTS teachers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    public_id VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255) NOT NULL,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE,
    access_code_hash VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    public_id VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    date_of_start DATE NOT NULL,
    instrument VARCHAR(255) NOT NULL,
    teacher_id BIGINT NOT NULL,
    taking_solfeige BOOLEAN NOT NULL DEFAULT FALSE,
    access_code_hash VARCHAR(255) UNIQUE NOT NULL,

    CONSTRAINT fk_students_teacher
        FOREIGN KEY (teacher_id)
        REFERENCES teachers(id)
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS lessons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    public_id VARCHAR(255) UNIQUE NOT NULL,
    date DATE NOT NULL,
    student_id BIGINT NOT NULL,
    teacher_id BIGINT NOT NULL,
    state VARCHAR(50) NOT NULL,
    mark SMALLINT NOT NULL DEFAULT 0,
    note TEXT,
    instrument BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_lessons_student
        FOREIGN KEY (student_id)
        REFERENCES students(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_lessons_teacher
        FOREIGN KEY (teacher_id)
        REFERENCES teachers(id)
        ON DELETE CASCADE,

    CONSTRAINT check_mark_value
        CHECK (mark BETWEEN 0 AND 10)
);

CREATE TABLE IF NOT EXISTS medals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medal_name VARCHAR(255) UNIQUE NOT NULL,
    medal_description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS videos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    public_id VARCHAR(255) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    link VARCHAR(500) NOT NULL UNIQUE,
    date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS students_medals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    medal_id BIGINT NOT NULL,
    medal_date DATE NOT NULL,

    CONSTRAINT fk_student
        FOREIGN KEY (student_id)
        REFERENCES students(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_medal
        FOREIGN KEY (medal_id)
        REFERENCES medals(id)
        ON DELETE CASCADE,

    CONSTRAINT unique_student_medal_month
        UNIQUE (student_id, medal_id, medal_date)
);

CREATE INDEX idx_students_teacher ON students(teacher_id);
CREATE INDEX idx_lessons_student ON lessons(student_id);
CREATE INDEX idx_lessons_teacher ON lessons(teacher_id);
CREATE INDEX idx_video_title ON videos(title);
CREATE INDEX idx_video_date ON videos(date);