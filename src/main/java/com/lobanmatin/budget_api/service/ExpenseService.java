package com.lobanmatin.budget_api.service;

import com.lobanmatin.budget_api.dto.ExpenseRequest;
import com.lobanmatin.budget_api.model.User;
import com.lobanmatin.budget_api.exception.ResourceNotFoundException;
import com.lobanmatin.budget_api.model.Expense;
import com.lobanmatin.budget_api.model.ExpenseCategory;
import com.lobanmatin.budget_api.repository.ExpenseRepository;
import com.lobanmatin.budget_api.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void createExpense(ExpenseRequest expenseRequest) {
        // Default category to N/A
        ExpenseCategory category = ExpenseCategory.NA;

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

    @Transactional
    public void deleteAllExpenses() {
        expenseRepository.deleteAllByUserId(getCurrentUserId());
    }

    public void updateCategory(Long expenseId, Long userId, String category) {
        Expense expense = expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        ExpenseCategory categoryEnum = ExpenseCategory.fromString(category);
        expense.setCategory(categoryEnum);
        expenseRepository.save(expense);
    }

    public void uploadExpensesFromCSV(MultipartFile file) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // skip header
                }

                String[] values = line.split(",");
                if (values.length < 3) {
                    // Skip malformed lines
                    continue;
                }

                try {
                    LocalDate date = LocalDate.parse(values[0].trim(), formatter);
                    BigDecimal amount = new BigDecimal(values[1].trim());
                    String title = values[2].trim();

                    if (!expenseRepository.existsByUserIdAndDateAndAmountAndTitle(getCurrentUserId(), date, amount, title)) {
                        ExpenseRequest request = new ExpenseRequest(title, amount, date);
                        createExpense(request);
                    }

                } catch (Exception e) {
                    System.err.println("Skipping line due to parse error: " + line);
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file", e);
        }
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userDetails.getId();
    }


}
