package com.lobanmatin.budget_api.controller;

import com.lobanmatin.budget_api.dto.UserRequest;
import com.lobanmatin.budget_api.model.User;
import com.lobanmatin.budget_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Endpoints for managing user accounts (admin access required for most)")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all registered users. Requires admin role.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of users retrieved successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class, type = "array")
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Access denied. Only admins can perform this operation.")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Get a specific user by ID",
            description = "Retrieve a specific user's details using their user ID. Requires admin role.",
            parameters = {
                    @Parameter(name = "id", description = "ID of the user to retrieve", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
                    @ApiResponse(responseCode = "404", description = "User not found."),
                    @ApiResponse(responseCode = "403", description = "Access denied. Only admins can perform this operation.")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(
            summary = "Delete all users",
            description = "Permanently delete all users in the system. Requires admin role.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "All users deleted successfully."),
                    @ApiResponse(responseCode = "403", description = "Access denied. Only admins can perform this operation.")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.noContent().build();
    }
}
