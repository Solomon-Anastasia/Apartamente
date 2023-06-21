package org.example;

import org.example.ui.MainView;

import javax.swing.*;

public class Runner {
    public static void startApplication() {
        SwingUtilities.invokeLater(() ->
                new MainView().setVisible(true)
        );
    }
}
