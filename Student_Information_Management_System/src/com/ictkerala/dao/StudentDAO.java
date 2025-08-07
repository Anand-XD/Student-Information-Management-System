package com.ictkerala.dao;

import com.ictkerala.model.Student;
import com.ictkerala.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StudentDAO {

    public boolean addStudent(Student student) {
        String sql = "INSERT INTO Student_db (name, email, dob, phone) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getDob());
            stmt.setString(4, student.getPhone());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setStudentId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student_db";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("dob"),
                        rs.getString("phone")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM Student_db WHERE student_id = ?";
        Student student = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    student = new Student(
                            rs.getInt("student_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("dob"),
                            rs.getString("phone")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public List<Student> searchStudentsByName(String name) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student_db WHERE name LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(new Student(
                            rs.getInt("student_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("dob"),
                            rs.getString("phone")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE Student_db SET name=?, email=?, dob=?, phone=? WHERE student_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getDob());
            stmt.setString(4, student.getPhone());
            stmt.setInt(5, student.getStudentId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        String deleteEnrollments = "DELETE FROM Enrollment_Details_db WHERE student_id = ?";
        String deleteStudent = "DELETE FROM Student_db WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (
                PreparedStatement stmt1 = conn.prepareStatement(deleteEnrollments);
                PreparedStatement stmt2 = conn.prepareStatement(deleteStudent)
            ) {
                stmt1.setInt(1, studentId);
                stmt1.executeUpdate();

                stmt2.setInt(1, studentId);
                int rows = stmt2.executeUpdate();

                conn.commit(); // Commit both if no error
                return rows > 0;
            } catch (SQLException e) {
                conn.rollback(); // Roll back if any issue
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Map<String, String> getCourseDetailsForStudent(int studentId) {
        Map<String, String> courseInfo = new LinkedHashMap<>();
        String sql = "SELECT c.course_name, e.grade, e.marks " +
                     "FROM Course_db c " +
                     "JOIN Enrollment_Details_db e ON c.course_id = e.course_id " +
                     "WHERE e.student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String course = rs.getString("course_name");
                    String grade = rs.getString("grade");
                    int marks = rs.getInt("marks");
                    courseInfo.put(course, "[Grade = " + grade + ", Marks = " + marks + "]");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseInfo;
    }
}