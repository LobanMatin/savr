package com.lobanmating.budget_api.model;

import jakarta.persistence.*;
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
    private BigDecimal totalLimit;

    @Column(nullable = false)
    private BigDecimal totalIncome;

    @ElementCollection
    @CollectionTable(name = "budget_category_limits", joinColumns = @JoinColumn(name = "budget_id"))
    @MapKeyColumn(name = "category")
    @Column(name = "category_limit")
    private Map<String, BigDecimal> categoryLimits = new HashMap<>();
}
