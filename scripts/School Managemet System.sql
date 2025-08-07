-- 1. Create the database
CREATE DATABASE IF NOT EXISTS studentdb;
USE studentdb;

-- 2. Create Student_db table
-- Added UNIQUE constraint to the email column
CREATE TABLE IF NOT EXISTS Student_db (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    dob        DATE,
    phone      VARCHAR(20)
);

-- 3. Create Course_db table
-- Added UNIQUE constraint to the course_name column
CREATE TABLE IF NOT EXISTS Course_db (
    course_id   INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL UNIQUE
);

-- 4. Create Enrollment_Details_db table
CREATE TABLE IF NOT EXISTS Enrollment_Details_db (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id    INT,
    course_id     INT,
    marks         INT,
    grade         VARCHAR(5),
    FOREIGN KEY (student_id) REFERENCES Student_db(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id)  REFERENCES Course_db(course_id) ON DELETE CASCADE
);

-- Add some sample data to the tables
INSERT INTO Student_db (name, email, dob, phone) VALUES
('John Doe', 'john.doe@example.com', '2000-01-15', '123-456-7890'),
('Jane Smith', 'jane.smith@example.com', '1999-05-20', '098-765-4321');

INSERT INTO Course_db (course_name) VALUES
('Introduction to Java'),
('Database Management'),
('Web Development');

INSERT INTO Enrollment_Details_db (student_id, course_id, marks, grade) VALUES
(1, 1, 85, 'A'),
(1, 2, 78, 'B'),
(2, 1, 92, 'A');