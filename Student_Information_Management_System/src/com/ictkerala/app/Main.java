package com.ictkerala.app;

import com.ictkerala.dao.*;
import com.ictkerala.model.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO courseDAO = new CourseDAO();
    private static final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n===== Student Information Management System =====");
            System.out.println("1. Console-based Application");
            System.out.println("2. GUI-based Application");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Clear newline

                switch (choice) {
                    case 1:
                        runConsoleApp();
                        break;
                    case 2:
                        // This would launch your MainMenu GUI
                        System.out.println("Launching GUI application...");
                        // new com.ictkerala.gui.MainMenu().setVisible(true);
                        break;
                    case 0:
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                choice = -1; // Set to a non-zero value to continue the loop
            }

        } while (choice != 0);

        scanner.close();
    }

    private static void runConsoleApp() {
        int choice;
        do {
            System.out.println("\n===== Student Information Management System (Console) =====");
            System.out.println("1. Add Student");
            System.out.println("2. Add New Course");
            System.out.println("3. Enroll Student in Course");
            System.out.println("4. View All Students");
            System.out.println("5. Search Student by ID");
            System.out.println("6. Search Student by Name");
            System.out.println("7. Update Student");
            System.out.println("8. Delete Student");
            System.out.println("9. Export Data to Text File");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Clear newline

                switch (choice) {
                    case 1:
                        handleAddStudent();
                        break;
                    case 2:
                        handleAddCourse();
                        break;
                    case 3:
                        handleEnrollStudent();
                        break;
                    case 4:
                        handleViewAllStudents();
                        break;
                    case 5:
                        handleSearchStudentById();
                        break;
                    case 6:
                        handleSearchStudentByName();
                        break;
                    case 7:
                        handleUpdateStudent();
                        break;
                    case 8:
                        handleDeleteStudent();
                        break;
                    case 9:
                        handleExportData();
                        break;
                    case 0:
                        System.out.println("Returning to main menu...");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                choice = -1;
            }

        } while (choice != 0);
    }

    private static void handleAddStudent() {
        System.out.println("Enter Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Email:");
        String email = scanner.nextLine();
        System.out.println("Enter DOB (YYYY-MM-DD):");
        String dob = scanner.nextLine();
        System.out.println("Enter Phone:");
        String phone = scanner.nextLine();

        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setDob(dob);
        student.setPhone(phone);

        boolean added = studentDAO.addStudent(student);
        System.out.println(added ? "Student added successfully!" : "Failed to add student.");
    }

    private static void handleAddCourse() {
        System.out.println("Enter Course Name:");
        String courseName = scanner.nextLine();
        Course course = new Course();
        course.setCourseName(courseName);

        boolean courseAdded = courseDAO.addCourse(course);
        System.out.println(courseAdded ? "Course added successfully!" : "Failed to add course.");
    }

    private static void handleEnrollStudent() {
        System.out.print("Enter Student ID to enroll: ");
        int studentId = scanner.nextInt();
        scanner.nextLine(); // Clear newline

        List<Course> courses = courseDAO.getAllCourses();
        System.out.println("Available Courses:");
        for (Course c : courses) {
            System.out.println("ID=" + c.getCourseId() + ", Name=" + c.getCourseName());
        }

        System.out.print("Enter Course ID to enroll: ");
        int courseId = scanner.nextInt();
        scanner.nextLine();

        if (enrollmentDAO.isAlreadyEnrolled(studentId, courseId)) {
            System.out.println("Student is already enrolled in that course.");
            return;
        }

        System.out.print("Enter Marks: ");
        int marks = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Grade: ");
        String grade = scanner.nextLine();

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setMarks(marks);
        enrollment.setGrade(grade);

        boolean enrolled = enrollmentDAO.enrollStudent(enrollment);
        System.out.println(enrolled ? "Enrollment successful." : "Enrollment failed.");
    }

    private static void handleViewAllStudents() {
        List<Student> allStudents = studentDAO.getAllStudents();

        if (allStudents.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Student s : allStudents) {
            Map<String, String> courseDetails = studentDAO.getCourseDetailsForStudent(s.getStudentId());
            System.out.println(s.toString() + ", Courses=" + courseDetails.entrySet() + "]");
        }
    }

    private static void handleSearchStudentById() {
        System.out.print("Enter Student ID: ");
        int idToSearch = scanner.nextInt();
        scanner.nextLine();

        Student student = studentDAO.getStudentById(idToSearch);

        if (student == null) {
            System.out.println("No student found.");
        } else {
            Map<String, String> details = studentDAO.getCourseDetailsForStudent(student.getStudentId());

            System.out.println(student.toString() + ", Courses=" + details.entrySet() + "]");
        }
    }

    private static void handleSearchStudentByName() {
        System.out.print("Enter name to search: ");
        String nameToSearch = scanner.nextLine();

        List<Student> matched = studentDAO.searchStudentsByName(nameToSearch);

        if (matched.isEmpty()) {
            System.out.println("No matching students found.");
        } else {
            for (Student studentMatch : matched) {
                Map<String, String> details = studentDAO.getCourseDetailsForStudent(studentMatch.getStudentId());
                System.out.println(studentMatch.toString() + ", Courses=" + details.entrySet() + "]");
            }
        }
    }

    private static void handleUpdateStudent() {
        System.out.print("Enter Student ID to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine();

        Student studentToUpdate = studentDAO.getStudentById(updateId);

        if (studentToUpdate == null) {
            System.out.println("No student found.");
            return;
        }

        System.out.println("1. Update student info");
        System.out.println("2. Update course details (de-enroll / marks / grade)");
        System.out.print("Choose: ");
        int updateChoice = scanner.nextInt();
        scanner.nextLine();

        if (updateChoice == 1) {
            updateStudentInfo(studentToUpdate);
        } else if (updateChoice == 2) {
            updateCourseDetails(updateId);
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void updateStudentInfo(Student studentToUpdate) {
        System.out.print("New Name [" + studentToUpdate.getName() + "]: ");
        String newName = scanner.nextLine();
        if (!newName.isBlank()) studentToUpdate.setName(newName);

        System.out.print("New Email [" + studentToUpdate.getEmail() + "]: ");
        String newEmail = scanner.nextLine();
        if (!newEmail.isBlank()) studentToUpdate.setEmail(newEmail);

        System.out.print("New DOB [" + studentToUpdate.getDob() + "]: ");
        String newDob = scanner.nextLine();
        if (!newDob.isBlank()) studentToUpdate.setDob(newDob);

        System.out.print("New Phone [" + studentToUpdate.getPhone() + "]: ");
        String newPhone = scanner.nextLine();
        if (!newPhone.isBlank()) studentToUpdate.setPhone(newPhone);

        boolean updated = studentDAO.updateStudent(studentToUpdate);
        System.out.println(updated ? "Student info updated." : "Update failed.");
    }

    private static void updateCourseDetails(int studentId) {
        System.out.println("1. De-enroll from a course");
        System.out.println("2. Update marks and grade");
        System.out.print("Choose: ");
        int courseChoice = scanner.nextInt();
        scanner.nextLine();

        List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByStudentId(studentId);
        if (enrollments.isEmpty()) {
            System.out.println("No course enrollments found.");
            return;
        }

        System.out.println("Enrolled Courses:");
        for (Enrollment e : enrollments) {
            Course course = courseDAO.getCourseById(e.getCourseId());
            System.out.println("Course ID=" + course.getCourseId() + ", Name=" + course.getCourseName() +
                    ", Marks=" + e.getMarks() + ", Grade=" + e.getGrade());
        }

        System.out.print("Enter Course ID to modify: ");
        int courseId = scanner.nextInt();
        scanner.nextLine();

        if (courseChoice == 1) {
            boolean deEnrolled = enrollmentDAO.deEnrollCourse(studentId, courseId);
            System.out.println(deEnrolled ? "De-enrolled successfully." : "Failed to de-enroll.");
        } else if (courseChoice == 2) {
            System.out.print("New Marks: ");
            int newMarks = scanner.nextInt();
            scanner.nextLine();
            System.out.print("New Grade: ");
            String newGrade = scanner.nextLine();

            boolean updated = enrollmentDAO.updateMarksAndGrade(studentId, courseId, newMarks, newGrade);
            System.out.println(updated ? "Marks/Grade updated." : "Update failed.");
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void handleDeleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        int deleteId = scanner.nextInt();
        scanner.nextLine();

        boolean deleted = studentDAO.deleteStudent(deleteId);
        if (deleted) {
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("No student found or failed to delete.");
        }
    }

    private static void handleExportData() {
        System.out.println("Exporting data...");
        List<Student> allStudents = studentDAO.getAllStudents();
        String filePath = "students_export.txt";

        try (FileWriter writer = new FileWriter(filePath)) {
            for (Student s : allStudents) {
                Map<String, String> courses = studentDAO.getCourseDetailsForStudent(s.getStudentId());
                writer.write("[ID=" + s.getStudentId() +
                             ", Name=" + s.getName() +
                             ", Email=" + s.getEmail() +
                             ", DOB=" + s.getDob() +
                             ", Phone=" + s.getPhone() +
                             ", Courses=" + courses.entrySet() + "]\n");
            }
            System.out.println("Export complete. Saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("Export failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}