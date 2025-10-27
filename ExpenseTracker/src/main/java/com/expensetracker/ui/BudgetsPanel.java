package com.expensetracker.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.expensetracker.dao.BudgetDao;
import com.expensetracker.dao.CategoryDao;
import com.expensetracker.model.Budget;

public class BudgetsPanel extends JPanel {
    private final DefaultTableModel model;
    private final JTable table;

    public BudgetsPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"Category", "Monthly Limit"}, 0);
        table = new JTable(model);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Save Budgets");
        bottom.add(save);
        add(bottom, BorderLayout.SOUTH);

        load();
        save.addActionListener(e -> saveAll());
    }

    private void load() {
        try {
            model.setRowCount(0);
            List<com.expensetracker.model.Category> cats = new CategoryDao().listAll();
            for (var c : cats) {
                Budget b = new BudgetDao().findByCategory(c.getName());
                model.addRow(new Object[]{c.getName(), b == null ? 0.0 : b.getMonthlyLimit()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAll() {
        try {
            BudgetDao dao = new BudgetDao();
            for (int i = 0; i < model.getRowCount(); i++) {
                String cat = String.valueOf(model.getValueAt(i, 0));
                double limit = 0.0;
                try { limit = Double.parseDouble(String.valueOf(model.getValueAt(i, 1))); } catch (NumberFormatException ignored) {}
                dao.upsert(new Budget(cat, limit));
            }
            JOptionPane.showMessageDialog(this, "Saved.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


