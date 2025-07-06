package com.lobanmating.budget_api.controller;

import com.lobanmating.budget_api.dto.BudgetRequest;
import com.lobanmating.budget_api.dto.ExpenseRequest;
import com.lobanmating.budget_api.model.Budget;
import com.lobanmating.budget_api.repository.BudgetRepository;
import com.lobanmating.budget_api.security.CustomUserDetails;
import com.lobanmating.budget_api.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@Valid @RequestBody BudgetRequest budgetRequest,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        budgetService.createBudget(userDetails.getId(), budgetRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Budget> getBudget(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Budget budget = budgetService.getBudget(userDetails.getId());
        return ResponseEntity.ok(budget);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBudget(@AuthenticationPrincipal CustomUserDetails userDetails) {
        budgetService.deleteBudget(userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/limit")
    public ResponseEntity<Void> adjustLimit(@RequestParam String category,
                                            @RequestParam BigDecimal limit,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        budgetService.adjustLimit(userDetails.getId(), category, limit);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/limit")
    public ResponseEntity<Void> removeLimit(@RequestParam String category,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        budgetService.removeLimit(userDetails.getId(), category);
        return ResponseEntity.ok().build();
    }
}
