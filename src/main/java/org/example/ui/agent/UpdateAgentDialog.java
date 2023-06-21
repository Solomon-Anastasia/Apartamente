package org.example.ui.agent;

import org.example.db.DbConfig;
import org.example.db.DbQuery;
import org.example.model.Agent;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateAgentDialog extends JDialog {
    private final JTextField numeField;
    private final JTextField prenumeField;
    private final JTextField virstaField;
    private final JTextField telefonField;
    private final JButton updateButton;

    private final int agentId;

    public UpdateAgentDialog(JFrame parent, Agent agent, int agentId) {
        super(parent, "Update Agent", true);
        this.agentId = agentId;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        numeField = new JTextField(agent.getNume());
        prenumeField = new JTextField(agent.getPrenume());
        virstaField = new JTextField(String.valueOf(agent.getVirsta()));
        telefonField = new JTextField(agent.getTelefon());
        updateButton = new JButton("Update");

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Nume:"));
        panel.add(numeField);
        panel.add(new JLabel("Prenume:"));
        panel.add(prenumeField);
        panel.add(new JLabel("Virsta:"));
        panel.add(virstaField);
        panel.add(new JLabel("Telefon:"));
        panel.add(telefonField);
        panel.add(updateButton);

        getContentPane().add(panel);

        updateButton.addActionListener(e -> updateAgent());
    }

    private void updateAgent() {
        if (numeField.getText().isEmpty() || prenumeField.getText().isEmpty()
                || virstaField.getText().isEmpty() || telefonField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Some of the fields are blank", "Blank fields", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!virstaField.getText().matches("^(1[8-9]|[2-7][0-9]|80)$")) {
            JOptionPane.showMessageDialog(this, "Varsta shoul be between 18 and 80.", "Varsta error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!telefonField.getText().matches("\\d{9}")) {
            JOptionPane.showMessageDialog(this, "Incorrect telefon format.", "Telefon error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String nume = numeField.getText();
        String prenume = prenumeField.getText();
        int virsta = Integer.parseInt(virstaField.getText());
        String telefon = telefonField.getText();

        try (Connection connection = DbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(DbQuery.UPDATE_AGENT)) {
            statement.setString(1, nume);
            statement.setString(2, prenume);
            statement.setInt(3, virsta);
            statement.setString(4, telefon);
            statement.setInt(5, agentId);

            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Agent updated successfully.", "Agent Updated", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update agent.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

