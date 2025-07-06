package com.lobanmating.budget_api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetRequest {

    @NotNull(message = "Total limit is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total limit must be positive")
    private BigDecimal totalLimit;

    @NotNull(message = "Total income is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total income must be positive")
    private BigDecimal totalIncome;
}