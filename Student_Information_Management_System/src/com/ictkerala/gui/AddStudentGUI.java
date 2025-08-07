package com.ictkerala.gui;

import com.ictkerala.dao.StudentDAO;
import com.ictkerala.model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class AddStudentGUI extends JFrame {

	private JTextField nameField, emailField, dobField, phoneField;

    public AddStudentGUI() {
        setTitle("🎓 Add New Student");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("📋 Add New Student", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        formPanel.add(new JLabel("👤 Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("📧 Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("🎂 Date of Birth (YYYY-MM-DD):"));
        dobField = new JTextField();
        formPanel.add(dobField);

        formPanel.add(new JLabel("📱 Phone Number:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("✅ Add Student");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.addActionListener(new AddButtonListener());
        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String dob = dobField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || dob.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(null, "⚠️ Please fill in all fields.");
                return;
            }

            Student student = new Student();
            student.setName(name);
            student.setEmail(email);
            student.setDob(dob);
            student.setPhone(phone);
            
            StudentDAO studentDAO = new StudentDAO();
            boolean success = studentDAO.addStudent(student);

            if (success) {
                JOptionPane.showMessageDialog(null, "🎉 Student added successfully!");
                nameField.setText("");
                emailField.setText("");
                dobField.setText("");
                phoneField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "❌ Failed to add student. Check database connection.");
            }
        }
    }
}