package com.lobanmating.budget_api.repository;

import com.lobanmating.budget_api.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    List<Expense> findByUserIdAndCategory(Long userId, String category);
    boolean existsByUserIdAndDateAndAmountAndTitle(Long userId, LocalDate date, BigDecimal amount, String title);

    void deleteAllByUserId(Long userId);
}
