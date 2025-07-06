package com.lobanmating.budget_api.repository;

import com.lobanmating.budget_api.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
