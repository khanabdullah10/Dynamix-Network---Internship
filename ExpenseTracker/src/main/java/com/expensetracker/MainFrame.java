package com.expensetracker;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import com.expensetracker.ui.AddPanel;
import com.expensetracker.ui.HistoryPanel;
import com.expensetracker.ui.ReportsPanel;
import com.expensetracker.ui.DashboardPanel;
import com.expensetracker.ui.BudgetsPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Add", new AddPanel());
        tabs.addTab("History", new HistoryPanel());
        tabs.addTab("Reports", new ReportsPanel());
        tabs.addTab("Budgets", new BudgetsPanel());
        tabs.addTab("Dashboard", new DashboardPanel());

        setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        javax.swing.JLabel title = new javax.swing.JLabel("Expense Tracker");
        title.setFont(title.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem importItem = new JMenuItem("Import CSV...");
        importItem.addActionListener(e -> {
            try {
                javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
                int ans = chooser.showOpenDialog(this);
                if (ans == javax.swing.JFileChooser.APPROVE_OPTION) {
                    java.io.File f = chooser.getSelectedFile();
                    java.util.List<com.expensetracker.model.Transaction> list = new java.util.ArrayList<>();
                    // very simple CSV reader: id,date,type,category,amount,currency,note
                    try (java.io.BufferedReader r = new java.io.BufferedReader(new java.io.FileReader(f))) {
                        String line; boolean first=true;
                        while ((line = r.readLine()) != null) {
                            if (first) { first=false; continue; }
                            String[] parts = line.split(",", -1);
                            if (parts.length < 7) continue;
                            com.expensetracker.model.Transaction t = new com.expensetracker.model.Transaction();
                            t.setDate(java.time.LocalDate.parse(parts[1]));
                            t.setType(parts[2]);
                            t.setCategory(parts[3]);
                            try { t.setAmount(Double.parseDouble(parts[4])); } catch (NumberFormatException ignored) {}
                            t.setCurrency(parts[5]);
                            t.setNote(parts[6].replace("\"", ""));
                            list.add(t);
                        }
                    }
                    com.expensetracker.dao.TransactionDao dao = new com.expensetracker.dao.TransactionDao();
                    for (var t : list) dao.insert(t);
                    JOptionPane.showMessageDialog(this, "Imported " + list.size() + " transactions.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        JMenuItem export = new JMenuItem("Export CSV...");
        export.addActionListener(e -> {
            try {
                ((HistoryPanel) tabs.getComponentAt(2)).requestFocusInWindow();
                ((HistoryPanel) tabs.getComponentAt(2)).dispatchEvent(new java.awt.event.ActionEvent(this, 0, "export"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Open History tab to export.");
            }
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> dispose());
        file.add(importItem);
        file.add(export);
        file.addSeparator();
        file.add(exit);

        JMenu view = new JMenu("View");
        JMenuItem dark = new JMenuItem("Toggle Dark Mode");
        dark.addActionListener(e -> {
            try {
                if (javax.swing.UIManager.getLookAndFeel().getName().toLowerCase().contains("light")) {
                    new com.formdev.flatlaf.FlatDarkLaf().setup();
                } else {
                    new com.formdev.flatlaf.FlatLightLaf().setup();
                }
                javax.swing.SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ignored) {}
        });
        view.add(dark);

        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this, "Expense Tracker\nSwing + SQLite", "About", JOptionPane.INFORMATION_MESSAGE));
        help.add(about);

        bar.add(file);
        bar.add(view);
        bar.add(help);
        setJMenuBar(bar);
    }
}


