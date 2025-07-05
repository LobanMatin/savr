package com.lobanmating.budget_api.dto;

import com.lobanmating.budget_api.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
// Do not include user ID so that it cannot be manipulated through malicious JSON inputs
public class ExpenseRequest {

    @NotBlank(message = "Name is required")
    private String title;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    String category;

    @PastOrPresent(message = "Date cannot be in the future")
    @NotNull(message = "Date is required")
    private LocalDate date;
}
