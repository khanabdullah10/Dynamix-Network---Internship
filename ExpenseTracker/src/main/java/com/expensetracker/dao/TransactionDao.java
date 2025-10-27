package com.expensetracker.dao;

import com.expensetracker.db.Database;
import com.expensetracker.model.Transaction;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    public void insert(Transaction t) throws SQLException {
        String sql = "INSERT INTO transactions(date,type,category,amount,note,currency) VALUES(?,?,?,?,?,?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getDate().toString());
            ps.setString(2, t.getType());
            ps.setString(3, t.getCategory());
            ps.setDouble(4, t.getAmount());
            ps.setString(5, t.getNote());
            ps.setString(6, t.getCurrency());
            ps.executeUpdate();
        }
    }

    public List<Transaction> listAll() throws SQLException {
        String sql = "SELECT id,date,type,category,amount,note,currency FROM transactions ORDER BY date DESC, id DESC";
        try (Connection c = Database.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            List<Transaction> list = new ArrayList<>();
            while (rs.next()) {
                Transaction t = map(rs);
                list.add(t);
            }
            return list;
        }
    }

    public int countAll() throws SQLException {
        try (Connection c = Database.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) AS cnt FROM transactions")) {
            return rs.next() ? rs.getInt("cnt") : 0;
        }
    }

    public void update(Transaction t) throws SQLException {
        String sql = "UPDATE transactions SET date=?, type=?, category=?, amount=?, note=?, currency=? WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getDate().toString());
            ps.setString(2, t.getType());
            ps.setString(3, t.getCategory());
            ps.setDouble(4, t.getAmount());
            ps.setString(5, t.getNote());
            ps.setString(6, t.getCurrency());
            ps.setLong(7, t.getId());
            ps.executeUpdate();
        }
    }

    public void deleteById(long id) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM transactions WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Transaction map(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setId(rs.getLong("id"));
        t.setDate(LocalDate.parse(rs.getString("date")));
        t.setType(rs.getString("type"));
        t.setCategory(rs.getString("category"));
        t.setAmount(rs.getDouble("amount"));
        t.setNote(rs.getString("note"));
        try { t.setCurrency(rs.getString("currency")); } catch (SQLException ignored) { t.setCurrency("USD"); }
        return t;
    }
}


