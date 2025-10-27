package com.expensetracker.service;

import com.expensetracker.dao.CategoryDao;
import com.expensetracker.dao.TransactionDao;
import com.expensetracker.model.Transaction;

import java.time.LocalDate;

public class SeedService {
    public void seedIfEmpty() {
        // seeding disabled per request
    }

    private Transaction tx(LocalDate date, String type, String category, double amount, String note) {
        Transaction t = new Transaction();
        t.setDate(date);
        t.setType(type);
        t.setCategory(category);
        t.setAmount(amount);
        t.setNote(note);
        return t;
    }
}


