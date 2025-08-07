package com.ictkerala.gui;

import com.ictkerala.dao.CourseDAO;
import com.ictkerala.model.Course;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

@SuppressWarnings({ "unused", "serial" })
public class AddCourseGUI extends JFrame {

	private JTextField courseNameField;
    private JLabel statusLabel;

    public AddCourseGUI() {
        setTitle("📚 Add New Course");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Status Label at the top
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(statusLabel, BorderLayout.NORTH);

        // Panel for inputs
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel courseNameLabel = new JLabel("Course Name:");
        courseNameField = new JTextField();

        formPanel.add(courseNameLabel);
        formPanel.add(courseNameField);

        add(formPanel, BorderLayout.CENTER);

        // Button
        JButton addButton = new JButton("➕ Add Course");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.addActionListener(new AddCourseButtonListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        buttonPanel.add(addButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private class AddCourseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String courseName = courseNameField.getText().trim();

            if (courseName.isEmpty()) {
                statusLabel.setText("⚠️ Please enter a course name.");
                statusLabel.setForeground(Color.RED);
                return;
            }

            Course course = new Course();
            course.setCourseName(courseName);

            try {
                boolean success = new CourseDAO().addCourse(course);

                if (success) {
                    statusLabel.setText("✅ Course added successfully!");
                    statusLabel.setForeground(new Color(0, 128, 0)); // Darker green for success
                    courseNameField.setText("");
                } else {
                    statusLabel.setText("❌ Failed to add course. Check database connection.");
                    statusLabel.setForeground(Color.RED);
                }
            } catch (Exception ex) {
                statusLabel.setText("❌ An error occurred: " + ex.getMessage());
                statusLabel.setForeground(Color.RED);
                ex.printStackTrace();
            }
        }
    }
}