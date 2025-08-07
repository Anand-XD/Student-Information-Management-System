package com.ictkerala.gui;

import com.ictkerala.dao.EnrollmentDAO;
import com.ictkerala.dao.StudentDAO;
import com.ictkerala.model.Enrollment;
import com.ictkerala.model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@SuppressWarnings({ "unused", "serial" })
public class UpdateStudentGUI extends JFrame {

	
	private JTextField studentIdField;
    private JButton fetchButton;

    private JTextField nameField, emailField, dobField, phoneField;
    private JButton updateInfoButton;

    private JComboBox<Integer> courseDropdown;
    private JTextField marksField, gradeField;
    private JButton updateMarksButton, deEnrollButton;

    private Student student;
    private StudentDAO studentDAO;
    private EnrollmentDAO enrollmentDAO;

    public UpdateStudentGUI() {
        setTitle("✏️ Update Student Details");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        studentDAO = new StudentDAO();
        enrollmentDAO = new EnrollmentDAO();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Panel for Fetching
        JPanel fetchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fetchPanel.add(new JLabel("Enter Student ID:"));
        studentIdField = new JTextField(10);
        fetchPanel.add(studentIdField);
        fetchButton = new JButton("Fetch Details");
        fetchPanel.add(fetchButton);

        mainPanel.add(fetchPanel, BorderLayout.NORTH);

        // Center Panel with tabs for different update options
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for Student Info Update
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        infoPanel.add(nameField);
        infoPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        infoPanel.add(emailField);
        infoPanel.add(new JLabel("DOB:"));
        dobField = new JTextField();
        infoPanel.add(dobField);
        infoPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        infoPanel.add(phoneField);
        updateInfoButton = new JButton("Update Info");
        updateInfoButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoPanel.add(new JLabel("")); // filler
        infoPanel.add(updateInfoButton);
        
        tabbedPane.addTab("Update Student Info", infoPanel);
        
        // Panel for Course Info Update
        JPanel coursePanel = new JPanel(new GridLayout(4, 2, 10, 10));
        coursePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        coursePanel.add(new JLabel("Select Course ID:"));
        courseDropdown = new JComboBox<>();
        coursePanel.add(courseDropdown);
        coursePanel.add(new JLabel("New Marks:"));
        marksField = new JTextField();
        coursePanel.add(marksField);
        coursePanel.add(new JLabel("New Grade:"));
        gradeField = new JTextField();
        coursePanel.add(gradeField);

        updateMarksButton = new JButton("Update Marks/Grade");
        deEnrollButton = new JButton("De-enroll from Course");
        updateMarksButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deEnrollButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JPanel courseButtonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        courseButtonPanel.add(updateMarksButton);
        courseButtonPanel.add(deEnrollButton);
        coursePanel.add(courseButtonPanel);
        coursePanel.add(new JLabel()); // filler
        
        tabbedPane.addTab("Update Course Details", coursePanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);

        // Button Actions
        fetchButton.addActionListener(e -> fetchStudentDetails());
        updateInfoButton.addActionListener(e -> updateStudentInfo());
        updateMarksButton.addActionListener(e -> updateMarksAndGrade());
        deEnrollButton.addActionListener(e -> deEnrollCourse());
        
        // Listener to populate marks/grade when course is selected
        courseDropdown.addActionListener(e -> {
            if (courseDropdown.getSelectedItem() != null) {
                int selectedCourseId = (int) courseDropdown.getSelectedItem();
                for (Enrollment en : enrollmentDAO.getEnrollmentsByStudentId(student.getStudentId())) {
                    if (en.getCourseId() == selectedCourseId) {
                        marksField.setText(String.valueOf(en.getMarks()));
                        gradeField.setText(en.getGrade());
                        break;
                    }
                }
            }
        });

        setVisible(true);
    }

    private void fetchStudentDetails() {
        try {
            int studentId = Integer.parseInt(studentIdField.getText().trim());
            student = studentDAO.getStudentById(studentId);

            if (student == null) {
                JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                clearFields();
                return;
            }

            // Fill student info fields
            nameField.setText(student.getName());
            emailField.setText(student.getEmail());
            dobField.setText(student.getDob());
            phoneField.setText(student.getPhone());
            
            // Populate course dropdown
            List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByStudentId(studentId);
            courseDropdown.removeAllItems();
            for (Enrollment e : enrollments) {
                courseDropdown.addItem(e.getCourseId());
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudentInfo() {
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Please fetch a student first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        student.setName(nameField.getText().trim());
        student.setEmail(emailField.getText().trim());
        student.setDob(dobField.getText().trim());
        student.setPhone(phoneField.getText().trim());

        boolean success = studentDAO.updateStudent(student);

        JOptionPane.showMessageDialog(this, success ? "Student info updated successfully!" : "Failed to update student info.", "Update Status", success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    private void updateMarksAndGrade() {
        if (student == null || courseDropdown.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please fetch a student and select a course first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int courseId = (int) courseDropdown.getSelectedItem();
            int marks = Integer.parseInt(marksField.getText().trim());
            String grade = gradeField.getText().trim();
            
            boolean success = enrollmentDAO.updateMarksAndGrade(student.getStudentId(), courseId, marks, grade);

            JOptionPane.showMessageDialog(this, success ? "Marks and Grade updated successfully!" : "Failed to update marks/grade.", "Update Status", success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid marks. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deEnrollCourse() {
        if (student == null || courseDropdown.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please fetch a student and select a course first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to de-enroll this student from the course?", "Confirm De-enroll", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int courseId = (int) courseDropdown.getSelectedItem();
            boolean success = enrollmentDAO.deEnrollCourse(student.getStudentId(), courseId);

            if (success) {
                JOptionPane.showMessageDialog(this, "De-enrolled from course successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                fetchStudentDetails(); // Refresh view
            } else {
                JOptionPane.showMessageDialog(this, "Failed to de-enroll from course.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        dobField.setText("");
        phoneField.setText("");
        marksField.setText("");
        gradeField.setText("");
        courseDropdown.removeAllItems();
    }
}