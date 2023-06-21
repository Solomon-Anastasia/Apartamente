package org.example.dao;

import org.example.db.DbConfig;
import org.example.db.DbQuery;
import org.example.model.Agent;
import org.example.ui.MainView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgentDao {
    public static DefaultTableModel loadAgentData(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DbQuery.SELECT_ALL_FROM_AGENT);
        ResultSet resultSet = statement.executeQuery();

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Nume", "Prenume", "Varsta", "Telefon"});

        while (resultSet.next()) {
            model.addRow(new Object[]{
                    resultSet.getString("nume"),
                    resultSet.getString("prenume"),
                    resultSet.getInt("virsta"),
                    resultSet.getString("telefon")
            });
        }

        resultSet.close();
        statement.close();

        return model;
    }

    public static TableModel getTotalAgentSales(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DbQuery.CALCULATE_AGENT_SALES);
        ResultSet resultSet = statement.executeQuery();

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Nume", "Prenume", "Vanzari"});

        while (resultSet.next()) {
            model.addRow(new Object[]{
                    resultSet.getString("nume"),
                    resultSet.getString("prenume"),
                    resultSet.getDouble("total_vanzari")
            });
        }
        resultSet.close();
        statement.close();

        return model;
    }

    public static int retrieveAgentIdByNumeAndPrenume(String nume, String prenume) {
        int id = -1;

        try (Connection connection = DbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(DbQuery.RETREIVE_AGENT_ID_BY_NUME_AND_PRENUME)) {

            statement.setString(1, nume);
            statement.setString(2, prenume);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("codAgent");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return id;
    }

    public static void insertAgent(Connection connection, String name, String surname, int age, String phone) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DbQuery.INSERT_AGENT_QUERY);
        statement.setString(1, name);
        statement.setString(2, surname);
        statement.setInt(3, age);
        statement.setString(4, phone);
        statement.executeUpdate();
        statement.close();
    }

    public static void deleteAgent(Connection connection, MainView mainView, String nume, String prenume) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DbQuery.DELETE_AGENT)) {
            statement.setString(1, nume);
            statement.setString(2, prenume);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(mainView, "Agent deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainView, "Error occurred while deleting agent.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static List<Agent> getAllAgents(Connection connection) {
        List<Agent> agents = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(DbQuery.SELECT_ALL_FROM_AGENT);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                int varsta = resultSet.getInt("virsta");
                String adresa = resultSet.getString("telefon");

                Agent agent = new Agent(nume, prenume, varsta, adresa);
                agents.add(agent);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return agents;
    }
}
