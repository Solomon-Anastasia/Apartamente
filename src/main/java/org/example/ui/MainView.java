package org.example.ui;

import org.example.dao.AgentDao;
import org.example.dao.ApartamentDao;
import org.example.db.DbConfig;
import org.example.model.Agent;
import org.example.model.Apartament;
import org.example.ui.agent.InsertAgentDialog;
import org.example.ui.agent.UpdateAgentDialog;
import org.example.ui.apartament.InsertApartamentDialog;
import org.example.ui.apartament.UpdateApartamentDialog;
import org.example.ui.tab.PhoneSearchTab;
import org.example.ui.tab.SalesTab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MainView extends JFrame {
    private JTabbedPane tabbedPane;

    private JTable agentTable;
    private JTable apartamentTable;
    private JTable filteredApartamentTable;
    private JTable agentTotalSales;

    private JButton updateButton;
    private JButton insertButton;
    private JButton deleteButton;

    private int agentIdForUpdate;


    public MainView() {
        initializeUI();
        getTablesData();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Apartamente");
        setSize(600, 400);

        tabbedPane = new JTabbedPane();
        agentTable = new JTable();
        apartamentTable = new JTable();
        filteredApartamentTable = new JTable();
        agentTotalSales = new JTable();
        updateButton = new JButton("Update");
        insertButton = new JButton("Insert");
        deleteButton = new JButton("Delete");

        tabbedPane.addTab("Agenti", new JScrollPane(agentTable));
        tabbedPane.addTab("Apartamente", new JScrollPane(apartamentTable));
        tabbedPane.addTab("Apartamente etajul 2/3", new JScrollPane(filteredApartamentTable));
        tabbedPane.addTab("Cautare Telefon", new PhoneSearchTab());
        tabbedPane.addTab("Vanzari", new SalesTab(agentTotalSales));

        tabbedPane.addChangeListener(e -> getTablesData());

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(insertButton);
        buttonPanel.add(deleteButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        agentTable.getSelectionModel().addListSelectionListener(event -> {
            if (event.getValueIsAdjusting()) {
                int selectedRow = agentTable.getSelectedRow();
                if (selectedRow != -1) {
                    agentIdForUpdate = AgentDao.retrieveAgentIdByNumeAndPrenume(
                            (String) agentTable.getValueAt(selectedRow, 0),
                            (String) agentTable.getValueAt(selectedRow, 1)
                    );
                    setButtonsState(
                            List.of(updateButton, deleteButton),
                            true);
                } else {
                    setButtonsState(
                            List.of(updateButton, deleteButton),
                            false);
                }
            }
        });

        apartamentTable.getSelectionModel().addListSelectionListener(event -> {
            if (event.getValueIsAdjusting()) {
                int selectedRow = apartamentTable.getSelectedRow();
                setButtonsState(
                        List.of(updateButton, deleteButton),
                        selectedRow != -1);
            }
        });

        updateButton.addActionListener(e -> {
            String selectedTab = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
            if (selectedTab.equals("Agenti")) {
                int selectedRow = agentTable.getSelectedRow();
                if (selectedRow != -1) {
                    Agent agent = new Agent(
                            agentTable.getValueAt(selectedRow, 0).toString(),
                            agentTable.getValueAt(selectedRow, 1).toString(),
                            Integer.parseInt(agentTable.getValueAt(selectedRow, 2).toString()),
                            agentTable.getValueAt(selectedRow, 3).toString()
                    );

                    UpdateAgentDialog dialog = new UpdateAgentDialog(MainView.this, agent, agentIdForUpdate);
                    dialog.setVisible(true);
                    closeWindow(dialog);
                } else {
                    JOptionPane.showMessageDialog(MainView.this, "No row is selected.", "Select Agent", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (selectedTab.equals("Apartamente")) {
                int selectedRow = apartamentTable.getSelectedRow();
                if (selectedRow != -1) {
                    String agentFullName = apartamentTable.getValueAt(selectedRow, 4).toString();
                    String[] parts = agentFullName.split(" ");
                    int agentIdForApartamentUpdate = AgentDao.retrieveAgentIdByNumeAndPrenume(parts[0], parts[1]);

                    Apartament apartament = new Apartament(
                            (Integer) apartamentTable.getValueAt(selectedRow, 0),
                            (Integer) apartamentTable.getValueAt(selectedRow, 1),
                            (Double) apartamentTable.getValueAt(selectedRow, 2),
                            (Double) apartamentTable.getValueAt(selectedRow, 3),
                            agentIdForApartamentUpdate
                    );

                    UpdateApartamentDialog dialog = new UpdateApartamentDialog(MainView.this, apartament, agentIdForApartamentUpdate);
                    dialog.setVisible(true);
                    closeWindow(dialog);
                } else {
                    JOptionPane.showMessageDialog(MainView.this, "No row is selected.", "Select Apartament", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        insertButton.addActionListener(e -> {
            String selectedTab = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
            if (selectedTab.equals("Agenti")) {
                InsertAgentDialog dialog = new InsertAgentDialog();
                dialog.setVisible(true);
                closeWindow(dialog);
            } else if (selectedTab.equals("Apartamente")) {
                InsertApartamentDialog dialog;
                try {
                    dialog = new InsertApartamentDialog(MainView.this);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dialog.setVisible(true);
                closeWindow(dialog);
            }
        });

        deleteButton.addActionListener(e -> {
            String selectedTab = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
            if (selectedTab.equals("Agenti")) {
                int selectedRow = agentTable.getSelectedRow();
                if (selectedRow != -1) {
                    String nume = (String) agentTable.getValueAt(selectedRow, 0);
                    String prenume = (String) agentTable.getValueAt(selectedRow, 1);

                    int dialogResult = JOptionPane.showConfirmDialog(MainView.this, "Are you sure you want to delete the agent " + nume + " " + prenume + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        try {
                            AgentDao.deleteAgent(DbConfig.getConnection(), MainView.this, nume, prenume);
                            getTablesData();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(MainView.this, "No row is selected.", "Select Agent", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (selectedTab.equals("Apartamente")) {
                int selectedRow = apartamentTable.getSelectedRow();
                if (selectedRow != -1) {
                    Apartament apartament = new Apartament(
                            (Integer) apartamentTable.getValueAt(selectedRow, 0),
                            (Integer) apartamentTable.getValueAt(selectedRow, 1),
                            (Double) apartamentTable.getValueAt(selectedRow, 2),
                            (Double) apartamentTable.getValueAt(selectedRow, 3)
                    );

                    int dialogResult = JOptionPane.showConfirmDialog(MainView.this, "Are you sure you want to delete the apartamen?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        try {
                            ApartamentDao.deleteApartament(DbConfig.getConnection(), MainView.this, apartament);
                            getTablesData();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(MainView.this, "No row is selected.", "Select Apartament", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    public void getTablesData() {
        String selectedTab = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
        setButtonsState(
                List.of(updateButton, deleteButton),
                false);

        try (Connection connection = DbConfig.getConnection()) {
            switch (selectedTab) {
                case "Apartamente" -> {
                    apartamentTable.setModel(ApartamentDao.loadApartamentData(connection));

                    setButtonsState(
                            List.of(insertButton),
                            true);
                }
                case "Agenti" -> {
                    agentTable.setModel(AgentDao.loadAgentData(connection));

                    setButtonsState(
                            List.of(insertButton),
                            true);
                }
                case "Apartamente etajul 2/3" -> {
                    filteredApartamentTable.setModel(ApartamentDao.getApartamenteOnFloor4(connection));

                    setButtonsState(
                            List.of(updateButton, deleteButton, insertButton),
                            false);
                }

                case "Cautare Telefon" -> setButtonsState(
                        List.of(updateButton, deleteButton, insertButton),
                        false);
                case "Vanzari" -> {
                    agentTotalSales.setModel(AgentDao.getTotalAgentSales(connection));
                    setButtonsState(
                            List.of(updateButton, deleteButton, insertButton),
                            false);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void closeWindow(Dialog dialog) {
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                getTablesData();
            }
        });
    }

    public void setButtonsState(List<JButton> buttons, boolean state) {
        for (JButton button : buttons) {
            button.setEnabled(state);
        }
    }
}