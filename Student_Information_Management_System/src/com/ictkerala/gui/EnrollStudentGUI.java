package com.ictkerala.gui;

import com.ictkerala.dao.CourseDAO;
import com.ictkerala.dao.EnrollmentDAO;
import com.ictkerala.model.Course;
import com.ictkerala.model.Enrollment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@SuppressWarnings("serial")
public class EnrollStudentGUI extends JFrame {

	private JTextField studentIdField;
    private JComboBox<Course> courseComboBox;
    private EnrollmentDAO enrollmentDAO;
    private CourseDAO courseDAO;

    public EnrollStudentGUI() {
        setTitle("🧾 Enroll Student in Course");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        enrollmentDAO = new EnrollmentDAO();
        courseDAO = new CourseDAO();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        studentIdField = new JTextField();
        courseComboBox = new JComboBox<>();

        // Load courses into dropdown
        loadCourses();

        formPanel.add(new JLabel("Enter Student ID:"));
        formPanel.add(studentIdField);
        formPanel.add(new JLabel("Select Course:"));
        formPanel.add(courseComboBox);
        
        panel.add(formPanel, BorderLayout.CENTER);

        JButton enrollButton = new JButton("Enroll");
        enrollButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        enrollButton.addActionListener(new EnrollButtonListener());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(enrollButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private void loadCourses() {
        List<Course> courses = courseDAO.getAllCourses();

        courseComboBox.removeAllItems();
        for (Course course : courses) {
            courseComboBox.addItem(course);
        }
    }

    private class EnrollButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int studentId = Integer.parseInt(studentIdField.getText().trim());
                Course selectedCourse = (Course) courseComboBox.getSelectedItem();

                if (selectedCourse == null) {
                    JOptionPane.showMessageDialog(EnrollStudentGUI.this, "Please select a course.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int courseId = selectedCourse.getCourseId();

                boolean alreadyEnrolled = enrollmentDAO.isAlreadyEnrolled(studentId, courseId);

                if (alreadyEnrolled) {
                    JOptionPane.showMessageDialog(EnrollStudentGUI.this, "Student is already enrolled in this course.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Prompt for marks and grade
                JTextField marksField = new JTextField();
                JTextField gradeField = new JTextField();
                
                JPanel myPanel = new JPanel(new GridLayout(0, 1));
                myPanel.add(new JLabel("Marks:"));
                myPanel.add(marksField);
                myPanel.add(new JLabel("Grade:"));
                myPanel.add(gradeField);
                
                int result = JOptionPane.showConfirmDialog(null, myPanel, 
                        "Enter Marks and Grade", JOptionPane.OK_CANCEL_OPTION);
                
                if (result == JOptionPane.OK_OPTION) {
                    int marks = Integer.parseInt(marksField.getText().trim());
                    String grade = gradeField.getText().trim();
                    
                    Enrollment enrollment = new Enrollment();
                    enrollment.setStudentId(studentId);
                    enrollment.setCourseId(courseId);
                    enrollment.setMarks(marks);
                    enrollment.setGrade(grade);

                    boolean success = enrollmentDAO.enrollStudent(enrollment);

                    if (success) {
                        JOptionPane.showMessageDialog(EnrollStudentGUI.this, "Student enrolled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        studentIdField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(EnrollStudentGUI.this, "Failed to enroll student. Check database.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(EnrollStudentGUI.this, "Invalid student ID, marks, or grade. Please enter numbers for ID and marks.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(EnrollStudentGUI.this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}