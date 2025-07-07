package com.lobanmatin.budget_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@ToString(exclude = "password")
public class UserRequest {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Schema(description = "Email used for user creation and authentication", example = "user@example.com")
    private String email;

    //TODO: add more password constraints using custom annotation
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "Password used for user creation and authentication, must be at least 6 characters",
            example = "examplepassword")
    private String password;
}