package org.example.ui.apartament;

import org.example.db.DbConfig;
import org.example.db.DbQuery;
import org.example.model.Apartament;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateApartamentDialog extends JDialog {
    private final JTextField etajField;
    private final JTextField nrCamereField;
    private final JTextField pretField;
    private final JTextField metriPatratiField;
    private final JButton updateButton;

    private final Apartament apartament;

    public UpdateApartamentDialog(JFrame parent, Apartament apartament) {
        super(parent, "Update Apartament", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        this.apartament = apartament;

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel etajLabel = new JLabel("Etaj:");
        etajField = new JTextField(String.valueOf(apartament.getEtaj()));

        JLabel nrCamereLabel = new JLabel("Number of Rooms:");
        nrCamereField = new JTextField(String.valueOf(apartament.getNrCamere()));

        JLabel pretLabel = new JLabel("Pret:");
        pretField = new JTextField(String.valueOf(apartament.getPret()));

        JLabel metriPatratiLabel = new JLabel("Metri Patrati:");
        metriPatratiField = new JTextField(String.valueOf(apartament.getMetriPatrati()));

        updateButton = new JButton("Update");

        panel.add(etajLabel);
        panel.add(etajField);
        panel.add(nrCamereLabel);
        panel.add(nrCamereField);
        panel.add(pretLabel);
        panel.add(pretField);
        panel.add(metriPatratiLabel);
        panel.add(metriPatratiField);
        panel.add(new JLabel());
        panel.add(updateButton);

        updateButton.addActionListener(e -> updateApartament());

        add(panel);
    }

    private void updateApartament() {
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

        try (Connection connection = DbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(DbQuery.UPDATE_APARTAMENT)) {
            statement.setInt(1, etaj);
            statement.setInt(2, nrCamere);
            statement.setDouble(3, pret);
            statement.setDouble(4, metriPatrati);
            statement.setInt(5, apartament.getEtaj());
            statement.setInt(6, apartament.getNrCamere());
            statement.setDouble(7, apartament.getPret());
            statement.setDouble(8, apartament.getMetriPatrati());

            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Apartament updated successfully.", "Apartament Updated", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update apartament.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}