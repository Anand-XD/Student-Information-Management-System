package com.ictkerala.gui;

import com.ictkerala.dao.StudentDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class DeleteStudentGUI extends JFrame {

	private JTextField studentIdField;
    private StudentDAO studentDAO;

    public DeleteStudentGUI() {
        setTitle("❌ Delete Student");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        studentDAO = new StudentDAO();

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel idLabel = new JLabel("Enter Student ID to Delete:");
        studentIdField = new JTextField(10);
        JButton deleteButton = new JButton("Delete Student");
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(idLabel);
        panel.add(studentIdField);
        panel.add(deleteButton);

        add(panel);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        setVisible(true);
    }

    private void deleteStudent() {
        try {
            int studentId = Integer.parseInt(studentIdField.getText().trim());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete student " + studentId + "? This action cannot be undone.",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = studentDAO.deleteStudent(studentId);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    studentIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete student. Student may not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}