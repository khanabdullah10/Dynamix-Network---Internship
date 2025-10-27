package com.expensetracker.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Date;

import com.expensetracker.dao.TransactionDao;
import com.expensetracker.model.Transaction;

public class AddPanel extends JPanel {
    private final JTextField amountField;
    private final JComboBox<String> typeCombo;
    private final JComboBox<Object> categoryCombo;
    private final JTextField noteField;
    private final JSpinner dateSpinner;
    private final JComboBox<String> currencyCombo;

    public AddPanel() {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        amountField = new JTextField();
        typeCombo = new JComboBox<>(new String[]{"Income", "Expense"});
        categoryCombo = new JComboBox<>();
        categoryCombo.setEditable(true);
        noteField = new JTextField();
        dateSpinner = new JSpinner(new SpinnerDateModel());
        currencyCombo = new JComboBox<>(new String[]{"USD","EUR","GBP","AED","INR","PKR","EGP"});
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        int row = 0;
        addRow(form, gbc, row++, new JLabel("Amount"), amountField);
        addRow(form, gbc, row++, new JLabel("Type"), typeCombo);
        addRow(form, gbc, row++, new JLabel("Category"), categoryCombo);
        addRow(form, gbc, row++, new JLabel("Date"), dateSpinner);
        addRow(form, gbc, row++, new JLabel("Currency"), currencyCombo);
        addRow(form, gbc, row++, new JLabel("Note"), noteField);

        JButton addBtn = new JButton("Add Transaction");
        gbc.gridx = 1; gbc.gridy = row; gbc.gridwidth = 1;
        form.add(addBtn, gbc);

        add(form, BorderLayout.NORTH);

        loadCategories();

        addBtn.addActionListener(e -> onAdd());
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, JComponent label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(label, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void onAdd() {
        String amountText = amountField.getText().trim();
        if (amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Amount is required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a number.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String type = (String) typeCombo.getSelectedItem();
        Object selected = categoryCombo.getSelectedItem();
        String category = selected == null ? "Other" : selected.toString().trim();
        if (!category.isEmpty()) {
            try { new com.expensetracker.dao.CategoryDao().insert(category); } catch (Exception ignored) {}
        }
        Date date = (Date) dateSpinner.getValue();
        LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
        String note = noteField.getText().trim();
        String currency = (String) currencyCombo.getSelectedItem();

        Transaction t = new Transaction();
        t.setAmount(amount);
        t.setType(type);
        t.setCategory(category);
        t.setDate(localDate);
        t.setNote(note);
        t.setCurrency(currency == null ? "USD" : currency);

        try {
            new TransactionDao().insert(t);
            JOptionPane.showMessageDialog(this, "Saved.");
            amountField.setText("");
            noteField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCategories() {
        try {
            var list = new com.expensetracker.dao.CategoryDao().listAll();
            categoryCombo.removeAllItems();
            for (var c : list) categoryCombo.addItem(c);
        } catch (Exception ex) {
            // fallback defaults if DB fails
            for (String s : new String[]{"Food", "Rent", "Entertainment", "Transport", "Utilities", "Other"}) {
                categoryCombo.addItem(s);
            }
        }
    }
}


