package com.lobanmating.budget_api.service;

import com.lobanmating.budget_api.dto.ExpenseRequest;
import com.lobanmating.budget_api.model.Expense;
import com.lobanmating.budget_api.model.User;
import com.lobanmating.budget_api.repository.ExpenseRepository;
import com.lobanmating.budget_api.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void createExpense(ExpenseRequest expenseRequest) {
        // Account for category being an optional field
        String category = expenseRequest.getCategory();
        if (category == null || category.isBlank()) {
            category = "N/A";
        }

        Expense expense = Expense.builder()
                .title(expenseRequest.getTitle())
                .amount(expenseRequest.getAmount())
                .category(category)
                .date(expenseRequest.getDate())
                .user(User.withId(getCurrentUserId()))
                .build();

        expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        // Identify user id through authentication context to avoid additional db calls
        return expenseRepository.findByUserId(getCurrentUserId());
    }

    public List<Expense> getExpensesByCategory(String category) {
        return expenseRepository.findByUserIdAndCategory(getCurrentUserId(), category);
    }

    public void deleteExpenseById(Long id) {
        Long userId = getCurrentUserId();
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!expense.getUser().getId().equals(userId)) {
            throw new SecurityException("Not authorized to delete this expense");
        }
        expenseRepository.deleteById(id);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userDetails.getId();
    }

}
