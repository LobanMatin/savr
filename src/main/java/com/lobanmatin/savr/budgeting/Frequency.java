package com.lobanmatin.savr.budgeting;

public enum Frequency {
    WEEKLY(52.0),
    FORTNIGHTLY(26.0),
    MONTHLY(12.0),
    QUARTERLY(4.0),
    ANNUALLY(1.0),
    ONE_TIME(0.0);
    private final double periodsPerYear;

    Frequency(double periodsPerYear) {
        this.periodsPerYear = periodsPerYear;
    }

    public double getPeriodsPerYear() {
        return periodsPerYear;
    }
}
