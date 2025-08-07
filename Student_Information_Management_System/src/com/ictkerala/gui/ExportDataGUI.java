package com.ictkerala.gui;

import com.ictkerala.dao.StudentDAO;
import com.ictkerala.model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ExportDataGUI extends JFrame {

	private StudentDAO studentDAO;

    public ExportDataGUI() {
        setTitle("📤 Export Student Data");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        studentDAO = new StudentDAO();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton exportButton = new JButton("Export All Data to File");
        exportButton.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(exportButton, BorderLayout.CENTER);

        add(panel);

        exportButton.addActionListener(this::handleExport);

        setVisible(true);
    }

    private void handleExport(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Exported Data As");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".txt")) {
                    filePath += ".txt";
                }
                
                List<Student> allStudents = studentDAO.getAllStudents();

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
                }

                JOptionPane.showMessageDialog(this, "Data exported successfully to " + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error writing to file.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}