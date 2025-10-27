package com.expensetracker.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.expensetracker.service.ReportService;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;

public class DashboardPanel extends JPanel {
    private final JLabel monthLabel = new JLabel();
    private XChartPanel<PieChart> chartPanel;

    public DashboardPanel() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Refresh");
        top.add(new JLabel("Monthly Spending by Category:"));
        top.add(monthLabel);
        top.add(refresh);
        add(top, BorderLayout.NORTH);

        chartPanel = new XChartPanel<>(new PieChartBuilder().width(600).height(400).title("Spending").build());
        add(chartPanel, BorderLayout.CENTER);

        refresh.addActionListener(e -> reload());
        reload();
    }

    private void reload() {
        try {
            LocalDate now = LocalDate.now();
            monthLabel.setText(String.format("%04d-%02d", now.getYear(), now.getMonthValue()));
            Map<String, Double> data = new ReportService().monthlySpendingByCategory(now.getYear(), now.getMonthValue());
            PieChart chart = new PieChartBuilder().width(600).height(400).title("Spending").build();
            if (data.isEmpty()) {
                chart.addSeries("No data", 1);
            } else {
                for (var e : data.entrySet()) chart.addSeries(e.getKey(), e.getValue());
            }
            remove(chartPanel);
            chartPanel = new XChartPanel<>(chart);
            add(chartPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


