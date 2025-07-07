package com.lobanmatin.budget_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
// Do not include user ID so that it cannot be manipulated through malicious JSON inputs
public class ExpenseRequest {

    @NotBlank(message = "Name is required")
    private String title;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @PastOrPresent(message = "Date cannot be in the future")
    @NotNull(message = "Date is required")
    private LocalDate date;
}
