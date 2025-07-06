package com.lobanmating.budget_api.controller;

import com.lobanmating.budget_api.dto.ExpenseRequest;
import com.lobanmating.budget_api.model.Expense;
import com.lobanmating.budget_api.security.CustomUserDetails;
import com.lobanmating.budget_api.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        expenseService.createExpense(expenseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(@RequestParam(required = false) String category) {
        List<Expense> expenses;
        if (category == null) {
            expenses = expenseService.getAllExpenses();
        } else {
            expenses = expenseService.getExpensesByCategory(category);
        }
        return ResponseEntity.ok(expenses);
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadExpenses(@RequestParam("file") MultipartFile file,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        expenseService.uploadExpensesFromCSV(file);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateExpenseCategory(
            @PathVariable Long id,
            @RequestParam String category,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        expenseService.updateCategory(id, userDetails.getId(), category);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteExpenses() {
        expenseService.deleteAllExpenses();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseById(@PathVariable Long id) {
        expenseService.deleteExpenseById(id);
        return ResponseEntity.noContent().build();
    }
}
