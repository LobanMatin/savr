package com.lobanmatin.budget_api.controller;

import com.lobanmatin.budget_api.dto.UserRequest;
import com.lobanmatin.budget_api.model.Budget;
import com.lobanmatin.budget_api.security.JwtUtil;
import com.lobanmatin.budget_api.service.CustomUserDetailsService;
import com.lobanmatin.budget_api.service.TokenBlacklistService;
import com.lobanmatin.budget_api.security.CustomUserDetails;
import com.lobanmatin.budget_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Authorization", description = "Endpoints for managing user creation and authorisation through JWT tokens")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;

    @Operation(
            summary = "Authorize a user",
            description = "Verify user credentials and generate a JWT token upon successful authorization.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User credentials for login",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequest.class),
                            examples = {
                                    @ExampleObject(name = "Example 1", value = """
                            {
                            "email": "example@email.com",
                            "password": "examplepasword"
                            }
                            """)
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "JWT token generated successfully.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Invalid user credentials. Authorization failed."
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getId());
        claims.put("role", userDetails.getRole());

        String jwt = jwtUtil.generateToken(claims, userDetails);

        return ResponseEntity.ok(jwt);
    }


    @Operation(
            summary = "Register a new user",
            description = "Verify whether the email is not already registered before creating new user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credentials for creating new user",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequest.class),
                            examples = {
                                    @ExampleObject(name = "Example 1", value = """
                            {
                            "email": "example@email.com",
                            "password": "examplepasword"
                            }
                            """)
                            })
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully registered."),
                    @ApiResponse(responseCode = "500", description = "Invalid user credentials, email or password doesn't follow required format or email is already registered.")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRequest request) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Logout a user",
            description = "Logout a user by blacklisting their JWT token",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "JWT token with 'Bearer ' prefix",
                            required = true,
                            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully logged out."),
                    @ApiResponse(responseCode = "400", description = "Invalid or missing token.")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        tokenBlacklistService.blacklist(token);
        return ResponseEntity.ok("Logged out successfully");
    }
}
