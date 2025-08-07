package com.ictkerala.dao;

import com.ictkerala.model.Enrollment;
import com.ictkerala.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    public boolean enrollStudent(Enrollment enrollment) {
        String sql = "INSERT INTO Enrollment_Details_db (student_id, course_id, marks, grade) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, enrollment.getStudentId());
            stmt.setInt(2, enrollment.getCourseId());
            stmt.setInt(3, enrollment.getMarks());
            stmt.setString(4, enrollment.getGrade());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM Enrollment_Details_db WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = new Enrollment(
                            rs.getInt("enrollment_id"),
                            rs.getInt("student_id"),
                            rs.getInt("course_id"),
                            rs.getInt("marks"),
                            rs.getString("grade")
                    );
                    enrollments.add(enrollment);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enrollments;
    }

    public boolean updateMarksAndGrade(int studentId, int courseId, int marks, String grade) {
        String sql = "UPDATE Enrollment_Details_db SET marks = ?, grade = ? WHERE student_id = ? AND course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, marks);
            stmt.setString(2, grade);
            stmt.setInt(3, studentId);
            stmt.setInt(4, courseId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deEnrollCourse(int studentId, int courseId) {
        String sql = "DELETE FROM Enrollment_Details_db WHERE student_id = ? AND course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isAlreadyEnrolled(int studentId, int courseId) {
        String sql = "SELECT 1 FROM Enrollment_Details_db WHERE student_id = ? AND course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true if record exists
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}