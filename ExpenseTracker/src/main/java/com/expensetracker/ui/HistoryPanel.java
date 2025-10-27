package com.expensetracker.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.expensetracker.dao.TransactionDao;
import com.expensetracker.model.Transaction;
import com.expensetracker.util.CsvExporter;

public class HistoryPanel extends JPanel {
    private final JTable table;
    private final DefaultTableModel model;

    public HistoryPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Date", "Type", "Category", "Amount", "Currency", "Note"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setAutoCreateRowSorter(true);
        // hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filters.add(new JLabel("Search:"));
        JTextField searchField = new JTextField(20);
        filters.add(searchField);

        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton exportBtn = new JButton("Export CSV");
        filters.add(editBtn);
        filters.add(deleteBtn);
        filters.add(exportBtn);

        add(filters, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh");
        filters.add(refresh);
        refresh.addActionListener(e -> loadData(searchField.getText().trim()));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { loadData(searchField.getText().trim()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { loadData(searchField.getText().trim()); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { loadData(searchField.getText().trim()); }
        });

        loadData("");

        editBtn.addActionListener(e -> onEdit());
        deleteBtn.addActionListener(e -> onDelete());
        exportBtn.addActionListener(e -> onExport());
    }

    private void loadData(String query) {
        model.setRowCount(0);
        try {
            List<Transaction> list = new TransactionDao().listAll();
            for (Transaction t : list) {
                if (!query.isEmpty()) {
                    String joined = String.join(" ", t.getDate().toString(), t.getType(), t.getCategory(), String.valueOf(t.getAmount()), t.getNote() == null ? "" : t.getNote());
                    if (!joined.toLowerCase().contains(query.toLowerCase())) continue;
                }
                model.addRow(new Object[]{t.getId(), t.getDate(), t.getType(), t.getCategory(), t.getAmount(), t.getCurrency(), t.getNote()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Transaction getSelectedTransaction() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int modelRow = table.convertRowIndexToModel(row);
        Transaction t = new Transaction();
        t.setId(Long.parseLong(String.valueOf(model.getValueAt(modelRow, 0))));
        t.setDate(java.time.LocalDate.parse(String.valueOf(model.getValueAt(modelRow, 1))));
        t.setType(String.valueOf(model.getValueAt(modelRow, 2)));
        t.setCategory(String.valueOf(model.getValueAt(modelRow, 3)));
        try { t.setAmount(Double.parseDouble(String.valueOf(model.getValueAt(modelRow, 4)))); } catch (NumberFormatException ignored) {}
        t.setCurrency(String.valueOf(model.getValueAt(modelRow, 5)));
        t.setNote(String.valueOf(model.getValueAt(modelRow, 6)));
        return t;
    }

    private void onEdit() {
        Transaction t = getSelectedTransaction();
        if (t == null) {
            JOptionPane.showMessageDialog(this, "Select a row to edit.");
            return;
        }
        TransactionDialog dlg = new TransactionDialog(SwingUtilities.getWindowAncestor(this), t);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            try {
                new TransactionDao().update(t);
                loadData("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onDelete() {
        Transaction t = getSelectedTransaction();
        if (t == null) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }
        int ans = JOptionPane.showConfirmDialog(this, "Delete selected transaction?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ans == JOptionPane.YES_OPTION) {
            try {
                new TransactionDao().deleteById(t.getId());
                loadData("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onExport() {
        try {
            List<Transaction> list = new TransactionDao().listAll();
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("transactions.csv"));
            int ans = chooser.showSaveDialog(this);
            if (ans == JFileChooser.APPROVE_OPTION) {
                CsvExporter.exportTransactions(chooser.getSelectedFile(), list);
                JOptionPane.showMessageDialog(this, "Exported.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


