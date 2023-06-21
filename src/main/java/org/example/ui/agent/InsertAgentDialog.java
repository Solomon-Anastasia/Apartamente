package org.example.ui.agent;

import org.example.db.DbConfig;
import org.example.dao.AgentDao;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertAgentDialog extends JDialog {
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel surnameLabel;
    private JTextField surnameField;
    private JLabel ageLabel;
    private JTextField ageField;
    private JLabel phoneLabel;
    private JTextField phoneField;
    private JButton insertButton;

    public InsertAgentDialog() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Insert Agent");
        setSize(300, 200);
        setModal(true);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        surnameLabel = new JLabel("Surname:");
        surnameField = new JTextField();
        ageLabel = new JLabel("Age:");
        ageField = new JTextField();
        phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField();
        insertButton = new JButton("Insert");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(surnameLabel);
        panel.add(surnameField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(new JLabel());
        panel.add(insertButton);

        getContentPane().add(panel, BorderLayout.CENTER);

        insertButton.addActionListener(e -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String phone = phoneField.getText();

            try (Connection connection = DbConfig.getConnection()) {
                AgentDao.insertAgent(connection, name, surname, age, phone);
                JOptionPane.showMessageDialog(InsertAgentDialog.this, "Agent inserted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(InsertAgentDialog.this, "Error occurred while inserting agent.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

