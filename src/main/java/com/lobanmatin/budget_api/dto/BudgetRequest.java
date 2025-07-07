package com.lobanmatin.budget_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Total spending limit for user's budget, must be greater than 0", example = "1200")
    private BigDecimal totalLimit;

    @NotNull(message = "Total income is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total income must be positive")
    @Schema(description = "Total monthly income for user, must be greater than 0", example = "5000")
    private BigDecimal totalIncome;
}