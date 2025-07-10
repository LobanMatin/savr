package com.lobanmatin.budget_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Data
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Total spending limit must be greater than 0")
    private BigDecimal totalLimit;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Total income must be greater than 0")
    private BigDecimal totalIncome;

    @ElementCollection
    @CollectionTable(name = "budget_category_limits", joinColumns = @JoinColumn(name = "budget_id"))
    @MapKeyColumn(name = "category")
    @Column(name = "category_limit")
    private Map<ExpenseCategory, BigDecimal> categoryLimits = new HashMap<>();
}
