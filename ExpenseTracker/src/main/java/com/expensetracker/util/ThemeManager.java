package com.expensetracker.util;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;

import javax.swing.*;
import java.awt.*;

public final class ThemeManager {
    private ThemeManager() { }

    public static void applyPalette(String name, java.awt.Window root) {
        try {
            switch ((name == null ? "Ocean" : name).toLowerCase()) {
                case "midnight":
                    // One Dark inspired palette
                    FlatDarkLaf.setup();
                    UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));
                    UIManager.put("Panel.background", new Color(40, 44, 52));
                    UIManager.put("TabbedPane.background", new Color(40, 44, 52));
                    UIManager.put("TabbedPane.contentAreaColor", new Color(45, 49, 58));
                    UIManager.put("TabbedPane.selectedBackground", new Color(45, 49, 58));
                    UIManager.put("TabbedPane.underlineColor", new Color(97, 175, 239));
                    UIManager.put("Button.background", new Color(97, 175, 239));
                    UIManager.put("Button.foreground", Color.BLACK);
                    UIManager.put("Component.focusColor", new Color(97, 175, 239));
                    break;
                case "emerald":
                    FlatArcIJTheme.setup();
                    setAccent(new Color(0, 158, 96)); // green
                    break;
                case "amethyst":
                    FlatArcIJTheme.setup();
                    setAccent(new Color(124, 77, 255)); // purple
                    break;
                case "sunset":
                    FlatArcOrangeIJTheme.setup();
                    setAccent(new Color(255, 140, 0)); // orange
                    break;
                case "ocean":
                default:
                    FlatArcIJTheme.setup();
                    setAccent(new Color(22, 138, 173)); // teal/blue
                    break;
            }

            FlatLaf.setUseNativeWindowDecorations(true);
            UIManager.put("Component.arc", 12);
            UIManager.put("Button.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            if (root != null) SwingUtilities.updateComponentTreeUI(root);
        } catch (Exception ignored) { }
    }

    private static void setAccent(Color accent) {
        resetCustomColors();
        UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Panel.background", new Color(245, 247, 250));
        UIManager.put("TabbedPane.background", new Color(245, 247, 250));
        UIManager.put("TabbedPane.contentAreaColor", Color.WHITE);
        UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
        UIManager.put("TabbedPane.underlineColor", accent);
        UIManager.put("TabbedPane.underlineHeight", 3);
        UIManager.put("Button.background", accent);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Component.focusColor", accent);
    }

    private static void resetCustomColors() {
        String[] keys = new String[] {
                "Panel.background","TabbedPane.background","TabbedPane.contentAreaColor",
                "TabbedPane.selectedBackground","TabbedPane.underlineColor","TabbedPane.underlineHeight",
                "Button.background","Button.foreground","Component.focusColor"
        };
        for (String k : keys) UIManager.put(k, null);
    }
}


