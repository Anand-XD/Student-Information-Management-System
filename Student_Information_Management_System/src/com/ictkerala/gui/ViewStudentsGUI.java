package com.ictkerala.gui;

import com.ictkerala.dao.StudentDAO;
import com.ictkerala.dao.EnrollmentDAO;
import com.ictkerala.model.Student;
import com.ictkerala.model.Enrollment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class ViewStudentsGUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton backButton;

    public ViewStudentsGUI() {
        setTitle("👀 All Students");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table model
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Name", "Email", "DOB", "Phone", "Courses (ID:Marks:Grade)"});

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load students
        loadAllStudents();

        setVisible(true);
    }

    private void loadAllStudents() {
        model.setRowCount(0); // Clear existing data

        StudentDAO studentDAO = new StudentDAO();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        List<Student> students = studentDAO.getAllStudents();

        for (Student s : students) {
            List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByStudentId(s.getStudentId());

            StringBuilder coursesInfo = new StringBuilder();
            for (Enrollment e : enrollments) {
                coursesInfo.append(e.getCourseId())
                           .append(":")
                           .append(e.getMarks())
                           .append(":")
                           .append(e.getGrade())
                           .append(", ");
            }

            if (!enrollments.isEmpty()) {
                coursesInfo.setLength(coursesInfo.length() - 2); // remove last comma
            }

            model.addRow(new Object[]{
                    s.getStudentId(),
                    s.getName(),
                    s.getEmail(),
                    s.getDob(),
                    s.getPhone(),
                    coursesInfo.toString()
            });
        }
    }
}