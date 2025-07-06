package com.lobanmating.budget_api.model;

import java.util.Arrays;

public enum ExpenseCategory {
    FOOD("Food"),
    TRANSPORT("Transport"),
    UTILITIES("Utilities"),
    ENTERTAINMENT("Entertainment"),
    HEALTH("Health"),
    OTHER("Other"),
    NA("N/A");

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ExpenseCategory fromString(String value) {
        if (value == null || value.isBlank()) {
            return NA;
        }

        value = value.trim().toUpperCase();
        if (value.equals("N/A")) {
            return NA;
        }

        try {
            return ExpenseCategory.valueOf(value);
        } catch (IllegalArgumentException e) {
            return NA; // or throw an error if you want stricter validation
        }
    }
}
