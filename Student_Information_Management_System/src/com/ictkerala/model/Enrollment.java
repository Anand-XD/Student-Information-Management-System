package com.ictkerala.model;

public class Enrollment {
    private int enrollmentId;     // Auto-generated
    private int studentId;
    private int courseId;
    private int marks;
    private String grade;

    public Enrollment() {}

    public Enrollment(int enrollmentId, int studentId, int courseId, int marks, String grade) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
        this.grade = grade;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }
    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public int getCourseId() {
        return courseId;
    }
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    public int getMarks() {
        return marks;
    }
    public void setMarks(int marks) {
        this.marks = marks;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "[CourseID=" + courseId + ", Grade=" + grade + ", Marks=" + marks + "]";
    }
}