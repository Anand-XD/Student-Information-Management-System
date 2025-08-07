package com.ictkerala.gui;

import com.ictkerala.dao.EnrollmentDAO;
import com.ictkerala.dao.StudentDAO;
import com.ictkerala.model.Enrollment;
import com.ictkerala.model.Student;
import com.ictkerala.model.Course;
import com.ictkerala.dao.CourseDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@SuppressWarnings("serial")
public class SearchStudentByNameGUI extends JFrame {

    private JTextField searchField;
    private JTextArea resultArea;
    private StudentDAO studentDAO;
    private EnrollmentDAO enrollmentDAO;
    private CourseDAO courseDAO;

    public SearchStudentByNameGUI() {
        setTitle("🔍 Search Student by Name");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        studentDAO = new StudentDAO();
        enrollmentDAO = new EnrollmentDAO();
        courseDAO = new CourseDAO();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new FlowLayout());

        JLabel searchLabel = new JLabel("Enter Student Name:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudentByName();
            }
        });

        setVisible(true);
    }

    private void searchStudentByName() {
        resultArea.setText(""); // Clear previous results

        String studentName = searchField.getText().trim();
        if (studentName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to search.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Student> students = studentDAO.searchStudentsByName(studentName);

        if (students.isEmpty()) {
            resultArea.setText("No students found with that name.");
            return;
        }

        resultArea.append("Found " + students.size() + " students:\n\n");
        for (Student student : students) {
            // Student basic info
            resultArea.append("Student Details:\n");
            resultArea.append("--------------------\n");
            resultArea.append("ID: " + student.getStudentId() + "\n");
            resultArea.append("Name: " + student.getName() + "\n");
            resultArea.append("Email: " + student.getEmail() + "\n");
            resultArea.append("DOB: " + student.getDob() + "\n");
            resultArea.append("Phone: " + student.getPhone() + "\n\n");
            
            // Enrollments
            List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByStudentId(student.getStudentId());

            if (enrollments.isEmpty()) {
                resultArea.append("No courses enrolled.\n");
            } else {
                resultArea.append("Enrolled Courses:\n");
                for (Enrollment enrollment : enrollments) {
                    Course course = courseDAO.getCourseById(enrollment.getCourseId());
                    resultArea.append("  - " + course.getCourseName() +
                            ", Marks: " + enrollment.getMarks() +
                            ", Grade: " + enrollment.getGrade() + "\n");
                }
            }
            resultArea.append("====================\n\n");
        }
    }
}