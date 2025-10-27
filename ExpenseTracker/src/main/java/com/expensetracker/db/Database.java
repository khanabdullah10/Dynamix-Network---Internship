package com.expensetracker.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
    private static Connection connection;

    private static final String SERVER_URL = "jdbc:sqlserver://localhost:1433;encrypt=false";
    private static final String DATABASE_NAME = "ExpenseTracker";
    private static final String USER = "khan";
    private static final String PASSWORD = "khan321";

    private Database() { }

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            ensureDatabaseExists();
            String url = SERVER_URL + ";databaseName=" + DATABASE_NAME;
            connection = DriverManager.getConnection(url, USER, PASSWORD);
            initializeSchema(connection);
        }
        return connection;
    }

    private static void ensureDatabaseExists() throws SQLException {
        try (Connection conn = DriverManager.getConnection(SERVER_URL + ";databaseName=master", USER, PASSWORD);
             Statement st = conn.createStatement()) {
            st.executeUpdate("IF DB_ID('" + DATABASE_NAME + "') IS NULL CREATE DATABASE [" + DATABASE_NAME + "]");
        }
    }

    private static void initializeSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("IF OBJECT_ID('dbo.categories','U') IS NULL " +
                    "CREATE TABLE dbo.categories (" +
                    "id INT IDENTITY(1,1) PRIMARY KEY," +
                    "name NVARCHAR(100) NOT NULL UNIQUE" +
                    ")");

            st.executeUpdate("IF OBJECT_ID('dbo.transactions','U') IS NULL " +
                    "CREATE TABLE dbo.transactions (" +
                    "id INT IDENTITY(1,1) PRIMARY KEY," +
                    "date DATE NOT NULL," +
                    "type NVARCHAR(10) NOT NULL CHECK (type IN ('Income','Expense'))," +
                    "category NVARCHAR(100) NOT NULL," +
                    "amount DECIMAL(18,2) NOT NULL," +
                    "note NVARCHAR(4000) NULL," +
                    "currency NVARCHAR(10) NOT NULL DEFAULT 'USD'" +
                    ")");

            st.executeUpdate("IF NOT EXISTS (SELECT 1 FROM dbo.categories WHERE name='Food') INSERT INTO dbo.categories(name) VALUES('Food');" +
                    "IF NOT EXISTS (SELECT 1 FROM dbo.categories WHERE name='Rent') INSERT INTO dbo.categories(name) VALUES('Rent');" +
                    "IF NOT EXISTS (SELECT 1 FROM dbo.categories WHERE name='Entertainment') INSERT INTO dbo.categories(name) VALUES('Entertainment');" +
                    "IF NOT EXISTS (SELECT 1 FROM dbo.categories WHERE name='Transport') INSERT INTO dbo.categories(name) VALUES('Transport');" +
                    "IF NOT EXISTS (SELECT 1 FROM dbo.categories WHERE name='Utilities') INSERT INTO dbo.categories(name) VALUES('Utilities');" +
                    "IF NOT EXISTS (SELECT 1 FROM dbo.categories WHERE name='Other') INSERT INTO dbo.categories(name) VALUES('Other');");

            st.executeUpdate("IF OBJECT_ID('dbo.budgets','U') IS NULL " +
                    "CREATE TABLE dbo.budgets (" +
                    "category NVARCHAR(100) NOT NULL PRIMARY KEY," +
                    "monthly_limit DECIMAL(18,2) NOT NULL" +
                    ")");
        }
    }
}


