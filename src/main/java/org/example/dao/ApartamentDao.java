package org.example.dao;

import org.example.db.DbQuery;
import org.example.model.Apartament;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApartamentDao {
    public static DefaultTableModel loadApartamentData(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DbQuery.SELECT_ALL_FROM_APARTAMENT_WITH_AGENT);
        ResultSet resultSet = statement.executeQuery();

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Etaj", "Nr. Camere", "Pret", "Metri Patrati", "Agent"});

        while (resultSet.next()) {
            String agent = resultSet.getString("nume") + " " + resultSet.getString("prenume");
            model.addRow(new Object[]{
                    resultSet.getInt("etaj"),
                    resultSet.getInt("nrCamere"),
                    resultSet.getDouble("pret"),
                    resultSet.getDouble("metriPatrati"),
                    agent
            });
        }

        resultSet.close();
        statement.close();

        return model;
    }

    public static DefaultTableModel getApartamenteOnFloor4(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DbQuery.SELECT_APARTAMENTE_ON_FLOOR_4);
        ResultSet resultSet = statement.executeQuery();

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Etaj", "Nr. Camere", "Pret", "Metri Patrati", "Agent"});

        while (resultSet.next()) {
            String agent = resultSet.getString("nume") + " " + resultSet.getString("prenume");
            model.addRow(new Object[]{
                    resultSet.getInt("etaj"),
                    resultSet.getInt("nrCamere"),
                    resultSet.getDouble("pret"),
                    resultSet.getDouble("metriPatrati"),
                    agent
            });
        }

        resultSet.close();
        statement.close();

        return model;
    }

    public static void deleteApartament(Connection connection, JFrame frame, Apartament apartament) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DbQuery.DELETE_APARTAMENT)) {
            statement.setInt(1, apartament.getEtaj());
            statement.setInt(2, apartament.getNrCamere());
            statement.setDouble(3, apartament.getPret());
            statement.setDouble(4, apartament.getMetriPatrati());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Apartament deleted successfully.", "Delete Apartament", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to delete apartament.", "Delete Apartament", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
