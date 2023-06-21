package org.example.ui.apartament;

import org.example.dao.AgentDao;
import org.example.db.DbConfig;
import org.example.db.DbQuery;
import org.example.model.Agent;
import org.example.model.Apartament;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InsertApartamentDialog extends JDialog {
    private final JTextField etajField;
    private final JTextField nrCamereField;
    private final JTextField pretField;
    private final JTextField metriPatratiField;
    private final JComboBox<String> agentComboBox;
    private final JButton insertButton;

    public InsertApartamentDialog(JFrame parent) throws SQLException {
        super(parent, "Insert Apartament", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel etajLabel = new JLabel("Etaj:");
        etajField = new JTextField();

        JLabel nrCamereLabel = new JLabel("Nr de cameri:");
        nrCamereField = new JTextField();

        JLabel pretLabel = new JLabel("Pret:");
        pretField = new JTextField();

        JLabel metriPatratiLabel = new JLabel("Metri Patrati:");
        metriPatratiField = new JTextField();

        JLabel agentLabel = new JLabel("Agent:");
        agentComboBox = new JComboBox<>();

        insertButton = new JButton("Insert");

        panel.add(etajLabel);
        panel.add(etajField);
        panel.add(nrCamereLabel);
        panel.add(nrCamereField);
        panel.add(pretLabel);
        panel.add(pretField);
        panel.add(metriPatratiLabel);
        panel.add(metriPatratiField);
        panel.add(agentLabel);
        panel.add(agentComboBox);
        panel.add(new JLabel());
        panel.add(insertButton);

        loadAgentComboBox();

        insertButton.addActionListener(e -> insertApartament());

        add(panel);
    }

    private void loadAgentComboBox() throws SQLException {
        List<Agent> agents = AgentDao.getAllAgents(DbConfig.getConnection());

        agentComboBox.addItem("Select Agent");

        for (Agent agent : agents) {
            String fullName = agent.getNume() + " " + agent.getPrenume();
            agentComboBox.addItem(fullName);
        }
    }

    private void insertApartament() {
        if (etajField.getText().isEmpty() || nrCamereField.getText().isEmpty()
                || pretField.getText().isEmpty() || metriPatratiField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Some of the fields are blank", "Blank fields", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!etajField.getText().matches("^(1[0-5]|[1-9])$")) {
            JOptionPane.showMessageDialog(this, "Building can only have 1-15 floors.", "Etaj error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!pretField.getText().matches("^\\d+(?:\\.\\d{1,2})?$")) {
            JOptionPane.showMessageDialog(this, "Not suitable pret value.", "Pret error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!metriPatratiField.getText().matches("^\\d+(?:\\.\\d{1,2})?$")) {
            JOptionPane.showMessageDialog(this, "Not suitable metri patrati value.", "Metraj error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int etaj = Integer.parseInt(etajField.getText());
        int nrCamere = Integer.parseInt(nrCamereField.getText());
        double pret = Double.parseDouble(pretField.getText());
        double metriPatrati = Double.parseDouble(metriPatratiField.getText());

        if (agentComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select an agent.", "Agent Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String selectedAgent = agentComboBox.getSelectedItem().toString();
        String[] parts = selectedAgent.split(" ");
        int agentId = AgentDao.retrieveAgentIdByNumeAndPrenume(parts[0], parts[1]);

        Apartament apartament = new Apartament(etaj, nrCamere, pret, metriPatrati, agentId);

        try (Connection connection = DbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(DbQuery.INSERT_INTO_APARTAMENT)) {
            statement.setInt(1, apartament.getEtaj());
            statement.setInt(2, apartament.getNrCamere());
            statement.setDouble(3, apartament.getPret());
            statement.setDouble(4, apartament.getMetriPatrati());
            statement.setInt(5, agentId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Apartament inserted successfully.", "Insert Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to insert apartament.", "Insert Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while inserting the apartament.", "Insert Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}

