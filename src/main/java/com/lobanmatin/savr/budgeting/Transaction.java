package com.lobanmatin.savr.budgeting;
import java.time.LocalDate;
import java.util.List;

public class Transaction extends FinancialEntry{
    private final LocalDate date;

    public Transaction(double value, Frequency frequency, List<String> categories, LocalDate date) {
        super(value, frequency, categories);
        this.date = date;
    }

    public LocalDate getDate() { return this.date; }
}
