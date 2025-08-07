package com.ictkerala.model;

public class Student {
    private int studentId;      
    private String name;
    private String email;
    private String dob;
    private String phone;

    public Student() {}

    public Student(int studentId, String name, String email, String dob, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.phone = phone;
    }

    // Getters and setters
    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "[ID=" + studentId + ", Name=" + name + ", Email=" + email +
                ", DOB=" + dob + ", Phone=" + phone + "]";
    }
}