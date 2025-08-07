package com.ictkerala.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings({ "unused", "serial" })
public class MainMenu extends JFrame {


	public MainMenu() {
        setTitle("Student Info Manager 🎓");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Title Label
        JLabel titleLabel = new JLabel("Student Info Manager", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(10, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        //add action listeners
        JButton addStudentButton = createButton("1. Add Student");
        addStudentButton.addActionListener(e -> new AddStudentGUI());

        JButton addCourseButton = createButton("2. Add New Course");
        addCourseButton.addActionListener(e -> new AddCourseGUI());

        JButton enrollStudentButton = createButton("3. Enroll Student in Course");
        enrollStudentButton.addActionListener(e -> new EnrollStudentGUI());

        JButton viewStudentsButton = createButton("4️. View All Students");
        viewStudentsButton.addActionListener(e -> new ViewStudentsGUI());

        JButton searchIdButton = createButton("5. Search Student by ID");
        searchIdButton.addActionListener(e -> new SearchStudentGUI());

        JButton searchNameButton = createButton("6. Search Student by Name");
        searchNameButton.addActionListener(e -> new SearchStudentByNameGUI());

        JButton updateStudentButton = createButton("7. Update Student");
        updateStudentButton.addActionListener(e -> new UpdateStudentGUI());

        JButton deleteStudentButton = createButton("8. Delete Student");
        deleteStudentButton.addActionListener(e -> new DeleteStudentGUI());

        JButton exportDataButton = createButton("9. Export Data to Text File");
        exportDataButton.addActionListener(e -> new ExportDataGUI());

        JButton exitButton = createButton("10. Exit");
        exitButton.addActionListener(e -> dispose());

        buttonPanel.add(addStudentButton);
        buttonPanel.add(addCourseButton);
        buttonPanel.add(enrollStudentButton);
        buttonPanel.add(viewStudentsButton);
        buttonPanel.add(searchIdButton);
        buttonPanel.add(searchNameButton);
        buttonPanel.add(updateStudentButton);
        buttonPanel.add(deleteStudentButton);
        buttonPanel.add(exportDataButton);
        buttonPanel.add(exitButton);

        // Add components to frame
        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setFocusPainted(false);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}