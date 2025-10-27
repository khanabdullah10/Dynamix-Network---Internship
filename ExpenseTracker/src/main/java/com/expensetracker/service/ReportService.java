package com.expensetracker.service;

import com.expensetracker.db.Database;

import java.sql.*;
import java.time.LocalDate;

public class ReportService {
    public double totalForDate(LocalDate date) throws SQLException {
        String sql = "SELECT SUM(CASE WHEN type='Income' THEN amount ELSE -amount END) AS total FROM transactions WHERE date = ?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, date.toString());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble("total") : 0.0;
            }
        }
    }

    public double totalForMonth(int year, int month) throws SQLException {
        String sql = "SELECT SUM(CASE WHEN type='Income' THEN amount ELSE -amount END) AS total FROM transactions WHERE YEAR(date)=? AND MONTH(date)=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble("total") : 0.0;
            }
        }
    }

    public double totalForYear(int year) throws SQLException {
        String sql = "SELECT SUM(CASE WHEN type='Income' THEN amount ELSE -amount END) AS total FROM transactions WHERE YEAR(date)=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble("total") : 0.0;
            }
        }
    }

    public java.util.Map<String, Double> monthlySpendingByCategory(int year, int month) throws SQLException {
        String sql = "SELECT category, SUM(CASE WHEN type='Expense' THEN amount ELSE 0 END) AS spent FROM transactions WHERE YEAR(date)=? AND MONTH(date)=? GROUP BY category ORDER BY spent DESC";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                java.util.LinkedHashMap<String, Double> map = new java.util.LinkedHashMap<>();
                while (rs.next()) map.put(rs.getString(1), rs.getDouble(2));
                return map;
            }
        }
    }
}


