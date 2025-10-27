package com.expensetracker;

import javax.swing.SwingUtilities;
import com.expensetracker.service.SeedService;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.FlatLaf;
import com.expensetracker.util.ThemeManager;

public class App {
    public static void main(String[] args) {
        // seed sample data on first run
        new SeedService().seedIfEmpty();
        SwingUtilities.invokeLater(() -> {
            try {
                FlatAnimatedLafChange.showSnapshot();
                ThemeManager.applyPalette("Ocean", null);
                FlatAnimatedLafChange.hideSnapshotWithAnimation();
            } catch (Exception ignored) { }
            MainFrame mainFrame = new MainFrame();
            // allow palette switching via system property: -Dtheme=Midnight/Emerald/Amethyst/Sunset/Ocean
            String palette = System.getProperty("theme");
            if (palette != null && !palette.isBlank()) {
                ThemeManager.applyPalette(palette, mainFrame);
            }
            mainFrame.setVisible(true);
            javax.swing.SwingUtilities.invokeLater(() -> {
                javax.swing.JOptionPane.showMessageDialog(mainFrame, "Welcome to the ExpenseTracker application!", "Welcome", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            });
        });
    }
}


