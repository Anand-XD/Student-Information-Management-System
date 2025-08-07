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
public class SearchStudentGUI extends JFrame {

	private JTextField searchField;
    private JTextArea resultArea;
    private StudentDAO studentDAO;
    private EnrollmentDAO enrollmentDAO;
    private CourseDAO courseDAO;

    public SearchStudentGUI() {
        setTitle("🔍 Search Student by ID");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        studentDAO = new StudentDAO();
        enrollmentDAO = new EnrollmentDAO();
        courseDAO = new CourseDAO();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new FlowLayout());

        JLabel searchLabel = new JLabel("Enter Student ID:");
        searchField = new JTextField(10);
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
                searchStudent();
            }
        });

        setVisible(true);
    }

    private void searchStudent() {
        resultArea.setText(""); // Clear previous results

        try {
            int studentId = Integer.parseInt(searchField.getText().trim());

            Student student = studentDAO.getStudentById(studentId);

            if (student == null) {
                resultArea.setText("Student not found.");
                return;
            }

            // Student basic info
            resultArea.append("Student Details:\n");
            resultArea.append("--------------------\n");
            resultArea.append("ID: " + student.getStudentId() + "\n");
            resultArea.append("Name: " + student.getName() + "\n");
            resultArea.append("Email: " + student.getEmail() + "\n");
            resultArea.append("DOB: " + student.getDob() + "\n");
            resultArea.append("Phone: " + student.getPhone() + "\n\n");
            resultArea.append("--------------------\n");

            // Enrollments
            List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByStudentId(studentId);

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

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}