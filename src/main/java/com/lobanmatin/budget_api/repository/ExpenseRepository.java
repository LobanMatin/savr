package com.lobanmatin.budget_api.repository;

import com.lobanmatin.budget_api.model.Expense;
import com.lobanmatin.budget_api.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    List<Expense> findByUserIdAndCategory(Long userId, ExpenseCategory category);
    boolean existsByUserIdAndDateAndAmountAndTitle(Long userId, LocalDate date, BigDecimal amount, String title);

    void deleteAllByUserId(Long userId);

    Optional<Expense> findByIdAndUserId(Long expenseId, Long userId);
}
