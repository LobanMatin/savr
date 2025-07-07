package com.lobanmatin.budget_api.controller;

import com.lobanmatin.budget_api.dto.BudgetRequest;
import com.lobanmatin.budget_api.dto.ExpenseRequest;
import com.lobanmatin.budget_api.model.Budget;
import com.lobanmatin.budget_api.model.Expense;
import com.lobanmatin.budget_api.security.CustomUserDetails;
import com.lobanmatin.budget_api.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Create an expense for a user",
            description = "Create an expense for the currently authenticated user. The expense will be created with category set as N/A by default.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the expense including title, date (YYYY-MM-DD), and amount.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Example 1",
                                            value = """
                    {
                      "title": "Lunch at cafe",
                      "date": "2025-07-07",
                      "amount": 18.50
                    }
                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Expense successfully created for user.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Expense.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Invalid expense details or budget does not exist for user."
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        expenseService.createExpense(expenseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Get all of the user's expenses",
            description = "Retrieve all expenses associated with the currently authenticated user. " +
                    "If a category is specified, only expenses from that category will be returned.",
            parameters = {
                    @Parameter(
                            name = "category",
                            description = "Optional. Filter expenses by category. Must match an existing category (e.g. TRANSPORT, FOOD).",
                            required = false,
                            example = "FOOD"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Expenses successfully retrieved.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Expense.class))
                            )
                    )
            }
    )
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

    @Operation(
            summary = "Upload expenses from a CSV file",
            description = "Upload a CSV file containing expenses and add them to the currently authenticated user's expense records. " +
                    "Each row in the file must follow the order: **date, amount, title[, category]**. The category field is optional.",
            parameters = {
                    @Parameter(
                            name = "file",
                            description = "CSV file to upload. Must have a header row and follow the format: `date,amount,title[,category]`.\n\n" +
                                    "- `date` must be in `YYYY-MM-DD` format\n" +
                                    "- `amount` must be a valid number\n" +
                                    "- `title` is a short description of the expense\n" +
                                    "- `category` (optional) must match one of the predefined categories if present",
                            required = true,
                            example = "expenses.csv"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Expenses successfully uploaded and saved.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Expense.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error while processing the uploaded file."
                    )
            }
    )
    @PostMapping("/upload")
    public ResponseEntity<Void> uploadExpenses(@RequestParam("file") MultipartFile file,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        expenseService.uploadExpensesFromCSV(file);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Update the category of an existing expense",
            description = "Update the spending category for a specific expense, identified by its ID. Only affects the authenticated user's expenses.",
            parameters = {
                    @Parameter(name = "id", description = "ID of the expense to be updated", required = true, example = "12"),
                    @Parameter(name = "category", description = "New category to assign to the expense. Must match an enum value.", required = true, example = "TRANSPORT")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Expense category updated successfully."),
                    @ApiResponse(responseCode = "403", description = "Expense does not belong to the user or category is invalid."),
                    @ApiResponse(responseCode = "404", description = "Expense not found.")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateExpenseCategory(
            @PathVariable Long id,
            @RequestParam String category,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        expenseService.updateCategory(id, userDetails.getId(), category);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Delete all expenses (Admin only)",
            description = "Deletes all expenses in the system. This action is restricted to users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "All expenses deleted successfully."),
                    @ApiResponse(responseCode = "403", description = "User does not have the ADMIN role.")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteExpenses() {
        expenseService.deleteAllExpenses();
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete a specific expense",
            description = "Deletes an expense by its ID. Typically used to remove an individual record by any user (or restrict based on ownership elsewhere in the service).",
            parameters = {
                    @Parameter(name = "id", description = "ID of the expense to delete", required = true, example = "7")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Expense deleted successfully."),
                    @ApiResponse(responseCode = "404", description = "Expense not found.")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseById(@PathVariable Long id) {
        expenseService.deleteExpenseById(id);
        return ResponseEntity.noContent().build();
    }
}
