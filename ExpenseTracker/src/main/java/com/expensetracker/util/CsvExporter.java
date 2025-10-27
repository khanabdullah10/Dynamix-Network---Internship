package com.expensetracker.util;

import com.expensetracker.model.Transaction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {
    public static void exportTransactions(File file, List<Transaction> list) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(file))) {
            w.write("id,date,type,category,amount,currency,note\n");
            for (Transaction t : list) {
                w.write(t.getId() + "," + t.getDate() + "," + t.getType() + "," +
                        escape(t.getCategory()) + "," + t.getAmount() + "," + escape(t.getCurrency()) + "," + escape(t.getNote()));
                w.write("\n");
            }
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        String v = s.replace("\"", "\"\"");
        if (v.contains(",") || v.contains("\n") || v.contains("\r")) {
            return '"' + v + '"';
        }
        return v;
    }
}


