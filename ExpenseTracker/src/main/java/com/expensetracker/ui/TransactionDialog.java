package com.expensetracker.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import com.expensetracker.model.Transaction;

public class TransactionDialog extends JDialog {
    private final JTextField amountField = new JTextField();
    private final JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Income", "Expense"});
    private final JComboBox<Object> categoryCombo = new JComboBox<>();
    private final JTextField noteField = new JTextField();
    private final JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
    private boolean saved = false;

    public TransactionDialog(Window owner, Transaction t) {
        super(owner, "Edit Transaction", ModalityType.APPLICATION_MODAL);
        setSize(420, 320);
        setLocationRelativeTo(owner);

        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;
        addRow(form, gbc, row++, new JLabel("Amount"), amountField);
        addRow(form, gbc, row++, new JLabel("Type"), typeCombo);
        addRow(form, gbc, row++, new JLabel("Category"), categoryCombo);
        addRow(form, gbc, row++, new JLabel("Date"), dateSpinner);
        addRow(form, gbc, row++, new JLabel("Note"), noteField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        buttons.add(save);
        buttons.add(cancel);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        loadCategories();
        populate(t);

        save.addActionListener(e -> {
            if (applyTo(t)) {
                saved = true;
                dispose();
            }
        });
        cancel.addActionListener(e -> dispose());
    }

    public boolean isSaved() { return saved; }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, JComponent label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(label, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void populate(Transaction t) {
        amountField.setText(String.valueOf(t.getAmount()));
        typeCombo.setSelectedItem(t.getType());
        noteField.setText(t.getNote() == null ? "" : t.getNote());
        categoryCombo.setSelectedItem(t.getCategory());
        dateSpinner.setValue(java.sql.Date.valueOf(t.getDate()));
    }

    private boolean applyTo(Transaction t) {
        String amountText = amountField.getText().trim();
        double amount;
        try { amount = Double.parseDouble(amountText); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a number.");
            return false;
        }
        t.setAmount(amount);
        t.setType((String) typeCombo.getSelectedItem());
        Object cat = categoryCombo.getSelectedItem();
        t.setCategory(cat == null ? "Other" : cat.toString());
        Date date = (Date) dateSpinner.getValue();
        t.setDate(new java.sql.Date(date.getTime()).toLocalDate());
        t.setNote(noteField.getText().trim());
        return true;
    }

    private void loadCategories() {
        try {
            var list = new com.expensetracker.dao.CategoryDao().listAll();
            categoryCombo.removeAllItems();
            for (var c : list) categoryCombo.addItem(c);
        } catch (Exception ex) {
            for (String s : new String[]{"Food", "Rent", "Entertainment", "Transport", "Utilities", "Other"}) {
                categoryCombo.addItem(s);
            }
        }
    }
}


