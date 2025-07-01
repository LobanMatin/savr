package com.lobanmating.budget_api.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@ToString(exclude = "password")
public class UserRequest {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    //TODO: add more password constraints using custom annotation
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}