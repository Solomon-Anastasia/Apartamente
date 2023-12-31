package org.example.ui.tab;

import org.example.db.DbConfig;
import org.example.db.DbQuery;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhoneSearchTab extends JPanel {
    private JTextField telefonField;
    private JButton searchButton;
    private JTable resultTable;

    public PhoneSearchTab() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        telefonField = new JTextField(10);
        searchButton = new JButton("Cauta");
        searchPanel.add(new JLabel("Telefon:"));
        searchPanel.add(telefonField);
        searchPanel.add(searchButton);

        resultTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(resultTable);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            if (!telefonField.getText().matches("\\d{9}")) {
                JOptionPane.showMessageDialog(this, "Incorrect telefon format.", "Telefon error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (telefonField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(PhoneSearchTab.this, "Please enter a phone number.", "Empty Field", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String telefon = telefonField.getText();
            try (Connection connection = DbConfig.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(DbQuery.FIND_AGENT_BY_PHONE_NUMBER);
                statement.setString(1, telefon);
                ResultSet resultSet = statement.executeQuery();

                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(new String[]{"Nume", "Prenume", "Telefon", "Varsta"});

                while (resultSet.next()) {
                    model.addRow(new Object[]{
                            resultSet.getString("nume"),
                            resultSet.getString("prenume"),
                            resultSet.getString("telefon"),
                            resultSet.getInt("virsta")
                    });
                }

                resultTable.setModel(model);

                resultSet.close();
                statement.close();

                if (resultTable.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(PhoneSearchTab.this, "No agent was found.", "No agent", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}
