package com.lobanmatin.budget_api.service;

import com.lobanmatin.budget_api.dto.BudgetRequest;
import com.lobanmatin.budget_api.exception.ResourceNotFoundException;
import com.lobanmatin.budget_api.model.Budget;
import com.lobanmatin.budget_api.model.ExpenseCategory;
import com.lobanmatin.budget_api.repository.BudgetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import com.lobanmatin.budget_api.model.User;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetService budgetService;


    // Test creating budget functionality
    @Test
    void createBudget_shouldSaveToRepo() {
        Long userId = 1L;
        BudgetRequest req = new BudgetRequest(new BigDecimal(1200), new BigDecimal(5000));

        // Verify budget is saved once
        when(budgetRepository.save(any(Budget.class))).thenAnswer(inv -> inv.getArgument(0));
        budgetService.createBudget(userId, req);
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }

    // Test whether the correct exception is thrown when budget already exists
    @Test
    void createBudget_shouldThrowIfBudgetExists() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Budget existingBudget = Budget.builder()
                .user(user)
                .build();


        when(budgetRepository.findByUserId(userId)).thenReturn(Optional.of(existingBudget));

        BudgetRequest req = new BudgetRequest(new BigDecimal(1200), new BigDecimal(5000));
        assertThrows(DataIntegrityViolationException.class, () -> {
            budgetService.createBudget(userId, req);
        });
    }


    @Test
    void getBudget_shouldReturnBudget() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Budget existingBudget = Budget.builder()
                .user(user)
                .totalIncome(new BigDecimal(5000))
                .totalLimit(new BigDecimal(1200))
                .build();


        when(budgetRepository.findByUserId(userId)).thenReturn(Optional.of(existingBudget));
        Budget result = budgetService.getBudget(userId);

        assertNotNull(result);
        assertEquals(existingBudget.getUser().getId(), result.getUser().getId());
        assertEquals(existingBudget.getTotalIncome(), result.getTotalIncome());
        assertEquals(existingBudget.getTotalLimit(), result.getTotalLimit());

        verify(budgetRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getBudget_shouldThrowIfBudgetDoesNotExist() {
        Long userId = 1L;

        when(budgetRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            budgetService.getBudget(userId);
        });
    }

    @Test
    void deleteBudget_shouldDeleteFromRepo_andFailOnSubsequentGet() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Budget existingBudget = Budget.builder()
                .user(user)
                .totalIncome(new BigDecimal(5000))
                .totalLimit(new BigDecimal(1200))
                .build();

        when(budgetRepository.findByUserId(userId))
                .thenReturn(Optional.of(existingBudget))
                .thenReturn(Optional.empty());

        budgetService.deleteBudget(userId);

        assertThrows(ResourceNotFoundException.class, () -> budgetService.getBudget(userId));
    }

    @Test
    void adjustTotalLimit_shouldUpdateLimitAndSave() {
        Long userId = 1L;
        BigDecimal newLimit = new BigDecimal(3000);

        User user = new User();
        user.setId(userId);

        Budget existingBudget = Budget.builder()
                .user(user)
                .totalIncome(new BigDecimal(5000))
                .totalLimit(new BigDecimal(1200))
                .build();

        when(budgetRepository.findByUserId(userId)).thenReturn(Optional.of(existingBudget));
        when(budgetRepository.save(any(Budget.class))).thenAnswer(inv -> inv.getArgument(0));

        budgetService.adjustTotalLimit(userId, newLimit);
        assertEquals(newLimit, existingBudget.getTotalLimit());

        verify(budgetRepository, times(1)).save(existingBudget);
    }

    @Test
    void adjustTotalLimit_shouldThrowIfLimitGreaterThanIncome() {
        Long userId = 1L;
        BigDecimal newLimit = new BigDecimal(6000);

        User user = new User();
        user.setId(userId);

        Budget existingBudget = Budget.builder()
                .user(user)
                .totalIncome(new BigDecimal(5000))
                .totalLimit(new BigDecimal(1200))
                .build();

        when(budgetRepository.findByUserId(userId)).thenReturn(Optional.of(existingBudget));

        assertThrows(RuntimeException.class, () ->
                budgetService.adjustTotalLimit(userId, newLimit)
        );

        verify(budgetRepository, never()).save(any());
    }

    @Test
    void adjustTotalLimit_shouldThrowIfLimitNegative() {
        Long userId = 1L;
        BigDecimal newLimit = new BigDecimal(-6000);

        User user = new User();
        user.setId(userId);

        Budget existingBudget = Budget.builder()
                .user(user)
                .totalIncome(new BigDecimal(5000))
                .totalLimit(new BigDecimal(1200))
                .build();

        when(budgetRepository.findByUserId(userId)).thenReturn(Optional.of(existingBudget));

        assertThrows(RuntimeException.class, () ->
                budgetService.adjustTotalLimit(userId, newLimit)
        );

        verify(budgetRepository, never()).save(any());
    }


    @Test
    void deleteLimit_ShouldDeleteCategoryLimitAndSave() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        ExpenseCategory category = ExpenseCategory.FOOD;

        Map<ExpenseCategory, BigDecimal> categoryLimits = new HashMap<>();
        categoryLimits.put(category, new BigDecimal(500));

        Budget existingBudget = Budget.builder()
                .user(user)
                .totalIncome(new BigDecimal(5000))
                .totalLimit(new BigDecimal(1200))
                .categoryLimits(categoryLimits)
                .build();

        when(budgetRepository.findByUserId(userId)).thenReturn(Optional.of(existingBudget));
        when(budgetRepository.save(any(Budget.class))).thenAnswer(invocation -> invocation.getArgument(0));


        budgetService.removeLimit(userId, category);
        assertFalse(existingBudget.getCategoryLimits().containsKey(category), "Category limit should be removed");
        verify(budgetRepository, times(1)).save(existingBudget);
    }

    @Test
    void deleteLimit_ShouldThrowIfCategoryMissing() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        ExpenseCategory category = ExpenseCategory.FOOD;

        Budget existingBudget = Budget.builder()
                .user(user)
                .totalIncome(new BigDecimal(5000))
                .totalLimit(new BigDecimal(1200))
                .categoryLimits(new HashMap<>())
                .build();

        when(budgetRepository.findByUserId(userId)).thenReturn(Optional.of(existingBudget));

        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.removeLimit(userId, category)
        );

        verify(budgetRepository, never()).save(any());
    }
}
