package com.expensetracker.model;

import java.time.LocalDate;

public class Transaction {
    private long id;
    private LocalDate date;
    private String type; // Income or Expense
    private String category;
    private double amount;
    private String note;
    private String currency = "USD";

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}


