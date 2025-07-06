package com.lobanmating.budget_api.service;

import com.lobanmating.budget_api.dto.BudgetRequest;
import com.lobanmating.budget_api.model.Budget;
import com.lobanmating.budget_api.model.User;
import com.lobanmating.budget_api.repository.BudgetRepository;
import com.lobanmating.budget_api.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public void createBudget(Long userId, BudgetRequest budgetRequest) {
        if (budgetRepository.findByUserId(userId).isPresent()) {
            throw new IllegalStateException("Budget already exists for this user.");
        }

        Budget budget = Budget.builder()
                .totalLimit(budgetRequest.getTotalLimit())
                .totalIncome(budgetRequest.getTotalIncome())
                .user(User.withId(userId))
                .categoryLimits(new HashMap<>())
                .build();

        budgetRepository.save(budget);
    }

    public Budget getBudget(Long userId) {

        Optional<Budget> budgetOptional = budgetRepository.findByUserId(userId);

        if (budgetOptional.isEmpty()) {
            throw new IllegalStateException("Budget does not exist for this user.");
        }

        return budgetOptional.get();
    }

    public void deleteBudget(Long userId) {
        if (budgetRepository.findByUserId(userId).isEmpty()) {
            throw new IllegalStateException("Budget does not exist for this user.");
        }

        budgetRepository.deleteByUserId(userId);
    }

    public void adjustLimit(Long userId, String category, BigDecimal limit) {
        Budget budget = budgetRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Budget not found for this user."));

        budget.getCategoryLimits().put(category, limit); // Add or update
        budgetRepository.save(budget);
    }

    public void removeLimit(Long userId, String category) {
        Budget budget = budgetRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Budget not found for this user."));

        if (!budget.getCategoryLimits().containsKey(category)) {
            throw new IllegalArgumentException("Category limit does not exist.");
        }

        budget.getCategoryLimits().remove(category);
        budgetRepository.save(budget);
    }
}
