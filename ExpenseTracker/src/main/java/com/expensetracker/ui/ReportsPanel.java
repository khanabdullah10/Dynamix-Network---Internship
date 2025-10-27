package com.expensetracker.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import com.expensetracker.service.ReportService;

public class ReportsPanel extends JPanel {
    private final JLabel dailyValue = new JLabel("0.00");
    private final JLabel monthlyValue = new JLabel("0.00");
    private final JLabel yearlyValue = new JLabel("0.00");

    public ReportsPanel() {
        setLayout(new BorderLayout());

        JPanel summary = new JPanel(new GridLayout(1, 3, 12, 12));
        summary.add(card("Daily", dailyValue));
        summary.add(card("Monthly", monthlyValue));
        summary.add(card("Yearly", yearlyValue));

        add(summary, BorderLayout.NORTH);

        JTextArea txt = new JTextArea();
        txt.setEditable(false);
        txt.setText("Reports will appear here (charts/tables).\n");
        add(new JScrollPane(txt), BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh");
        add(refresh, BorderLayout.SOUTH);
        refresh.addActionListener(e -> refreshTotals());

        refreshTotals();
    }

    private JComponent card(String title, JLabel valueLabel) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 18f));
        p.add(valueLabel, BorderLayout.CENTER);
        return p;
    }

    private void refreshTotals() {
        try {
            ReportService svc = new ReportService();
            LocalDate today = LocalDate.now();
            double d = svc.totalForDate(today);
            double m = svc.totalForMonth(today.getYear(), today.getMonthValue());
            double y = svc.totalForYear(today.getYear());
            dailyValue.setText(String.format("%.2f", d));
            monthlyValue.setText(String.format("%.2f", m));
            yearlyValue.setText(String.format("%.2f", y));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


