package com.lobanmatin.budget_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Title of the expense", example = "Lunch at cafe")
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Expense monetary amount", example = "18.50")
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @Schema(description = "Date of expense in YYYY-MM-DD", example = "2025-07-07")
    @PastOrPresent(message = "Date cannot be in the future")
    @NotNull(message = "Date is required")
    private LocalDate date;
}
