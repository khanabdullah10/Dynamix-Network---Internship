package com.expensetracker.dao;

import com.expensetracker.db.Database;
import com.expensetracker.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    public List<Category> listAll() throws SQLException {
        try (Connection c = Database.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT id,name FROM categories ORDER BY name")) {
            List<Category> list = new ArrayList<>();
            while (rs.next()) {
                Category cat = new Category();
                cat.setId(rs.getLong("id"));
                cat.setName(rs.getString("name"));
                list.add(cat);
            }
            return list;
        }
    }

    public void insert(String name) throws SQLException {
        String sql = "IF NOT EXISTS (SELECT 1 FROM categories WHERE name=?) INSERT INTO categories(name) VALUES(?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, name);
            ps.executeUpdate();
        }
    }
}


