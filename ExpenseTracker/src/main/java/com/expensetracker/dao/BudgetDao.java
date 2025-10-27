package com.expensetracker.dao;

import com.expensetracker.db.Database;
import com.expensetracker.model.Budget;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDao {
    public void upsert(Budget b) throws SQLException {
        String sql = "MERGE budgets AS target USING (SELECT ? AS category, ? AS monthly_limit) AS src ON target.category = src.category " +
                "WHEN MATCHED THEN UPDATE SET monthly_limit = src.monthly_limit " +
                "WHEN NOT MATCHED THEN INSERT (category, monthly_limit) VALUES (src.category, src.monthly_limit);";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getCategory());
            ps.setDouble(2, b.getMonthlyLimit());
            ps.executeUpdate();
        }
    }

    public Budget findByCategory(String category) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT category, monthly_limit FROM budgets WHERE category=?")) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Budget(rs.getString(1), rs.getDouble(2));
                return null;
            }
        }
    }

    public List<Budget> listAll() throws SQLException {
        try (Connection c = Database.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT category, monthly_limit FROM budgets ORDER BY category")) {
            List<Budget> list = new ArrayList<>();
            while (rs.next()) list.add(new Budget(rs.getString(1), rs.getDouble(2)));
            return list;
        }
    }
}


