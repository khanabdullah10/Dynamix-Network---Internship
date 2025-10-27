package com.expensetracker.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Model: Transaction
 */
public class Transaction {
    public enum Type { INCOME, EXPENSE }

    private String id;
    private LocalDate date;
    private Type type;
    private String category;
    private double amount;
    private String description;

    private static final DateTimeFormatter DF = DateTimeFormatter.ISO_LOCAL_DATE;

    public Transaction(LocalDate date, Type type, String category, double amount, String description) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description == null ? "" : description;
    }

    // constructor with id (when loading)
    public Transaction(String id, LocalDate date, Type type, String category, double amount, String description) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description == null ? "" : description;
    }

    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public Type getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }

    public String toCsvLine() {
        // CSV: id,date,type,category,amount,description
        return quote(id) + "," + quote(date.format(DF)) + "," + quote(type.name()) + "," + quote(category) + "," + quote(String.valueOf(amount)) + "," + quote(description);
    }

    public static Transaction fromCsvLine(String line) {
        String[] fields = splitCsv(line);
        if (fields.length < 6) return null;
        try {
            String id = unquote(fields[0]);
            LocalDate date = LocalDate.parse(unquote(fields[1]));
            Type type = Type.valueOf(unquote(fields[2]));
            String category = unquote(fields[3]);
            double amount = Double.parseDouble(unquote(fields[4]));
            String description = unquote(fields[5]);
            return new Transaction(id, date, type, category, amount, description);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String quote(String s) {
        if (s == null) s = "";
        s = s.replace("\"", "\"\"");
        return "\"" + s + "\"";
    }

    private static String unquote(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            s = s.substring(1, s.length()-1);
        }
        return s.replace("\"\"", "\"");
    }

    // simple CSV split that respects quoted fields
    private static String[] splitCsv(String line) {
        java.util.List<String> cols = new java.util.ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                // peek next char to handle doubled quotes
                if (inQuotes && i+1 < line.length() && line.charAt(i+1) == '"') {
                    cur.append('"');
                    i++; // skip next
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                cols.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(ch);
            }
        }
        cols.add(cur.toString());
        return cols.toArray(new String[0]);
    }
}

