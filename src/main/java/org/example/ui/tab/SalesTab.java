package org.example.ui.tab;

import javax.swing.*;
import java.awt.*;

public class SalesTab extends JPanel {
    private final JTable resultTable;

    public SalesTab(JTable agentTotalSales) {
        this.resultTable = agentTotalSales;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);
    }
}
