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

        String normalized = value.trim();

        // Try to match displayName (case-insensitive)
        return Arrays.stream(ExpenseCategory.values())
                .filter(c -> c.displayName.equalsIgnoreCase(normalized)
                        || c.name().equalsIgnoreCase(normalized)
                        || (normalized.equalsIgnoreCase("N/A") && c == NA))
                .findFirst()
                .orElse(NA);
    }
}
