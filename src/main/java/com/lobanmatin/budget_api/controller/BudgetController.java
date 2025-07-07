package com.lobanmatin.budget_api.controller;

import com.lobanmatin.budget_api.dto.BudgetRequest;
import com.lobanmatin.budget_api.dto.UserRequest;
import com.lobanmatin.budget_api.model.Budget;
import com.lobanmatin.budget_api.model.ExpenseCategory;
import com.lobanmatin.budget_api.service.BudgetService;
import com.lobanmatin.budget_api.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/budget")
@Tag(name = "Budget", description = "Endpoints for managing user budget CRUD operations")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @Operation(
            summary = "Create a budget for a user",
            description = "Create the budget of a user using their income and total desired spending threshold",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User's total income and monthly spending limit",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BudgetRequest.class),
                            examples = {
                                    @ExampleObject(name = "Example 1", value = """
                            {
                            totalIncome: 4000,
                            totalLimit: 1200
                            }
                            """)
                            }
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Budget successfully generated for user."),
                    @ApiResponse(responseCode = "403", description = "Invalid income/spending limit values or budget already exists, budget creation unsuccessful.")
            }
    )
    @PostMapping
    public ResponseEntity<Budget> createBudget(@Valid @RequestBody BudgetRequest budgetRequest,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        budgetService.createBudget(userDetails.getId(), budgetRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Get a user's budget",
            description = "Retrieve the budget associated with the currently authenticated user, if it exists.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Budget successfully retrieved.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Budget.class))),
                    @ApiResponse(responseCode = "403", description = "No budget found for the user.")
            }
    )
    @GetMapping
    public ResponseEntity<Budget> getBudget(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Budget budget = budgetService.getBudget(userDetails.getId());
        return ResponseEntity.ok(budget);
    }

    @Operation(
            summary = "Delete a user's budget",
            description = "Delete the budget associated with the currently authenticated user, if it exists.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Budget successfully deleted."),
                    @ApiResponse(responseCode = "403", description = "No budget found for the user.")
            }
    )
    @DeleteMapping
    public ResponseEntity<Void> deleteBudget(@AuthenticationPrincipal CustomUserDetails userDetails) {
        budgetService.deleteBudget(userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Adjust the total spending limit of a user's budget",
            description = "Adjust the total spending limit of the budget associated " +
                    "with the currently authenticated user, if it exists.",
            parameters = {
                    @Parameter(
                            name = "limit",
                            description = "Spending limit to be imposed on the total allowable limit",
                            example = "1200"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Limit successfully adjusted."),
                    @ApiResponse(responseCode = "403", description = "No budget found for the user.")
            }
    )
    @PostMapping("/limit/total")
    public ResponseEntity<Void> adjustTotalLimit(@RequestParam BigDecimal limit,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        budgetService.adjustTotalLimit(userDetails.getId(), limit);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Adjust the spending limit of a user's budget for a certain category",
            description = "Adjust the spending limit of a spending category of the budget associated " +
                    "with the currently authenticated user, if it exists.",
            parameters = {
                    @Parameter(
                            name = "Category",
                            description = "Spending Category the limit is being adjusted for, " +
                                    "must be part of ExpenseCategory enum",
                            required = true,
                            example = "TRANSPORT"
                    ),
                    @Parameter(
                            name = "limit",
                            description = "Spending limit to be imposed on the ",
                            example = "1200"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Limit successfully adjusted."),
                    @ApiResponse(responseCode = "403", description = "No budget found for the user, " +
                            "or invalid spending category or limit exceeds allowable limit.")
            }
    )
    @PostMapping("/limit/category")
    public ResponseEntity<Void> adjustCategoryLimit(@RequestParam ExpenseCategory category,
                                            @RequestParam BigDecimal limit,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        budgetService.adjustCategoryLimit(userDetails.getId(), category, limit);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Delete the spending limit on a specific category",
            description = "Delete the category-specific spending limit from the budget of the currently authenticated user.",
            parameters = {
                    @Parameter(
                            name = "category",
                            description = "The category to remove the spending limit from. Must be part of the ExpenseCategory enum.",
                            required = true,
                            example = "TRANSPORT"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Spending limit successfully deleted."),
                    @ApiResponse(responseCode = "403", description = "No budget found or category limit does not exist.")
            }
    )
    @DeleteMapping("/limit/category")
    public ResponseEntity<Void> removeCategoryLimit(@RequestParam ExpenseCategory category,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        budgetService.removeLimit(userDetails.getId(), category);
        return ResponseEntity.noContent().build();
    }
}
