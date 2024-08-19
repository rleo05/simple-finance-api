package com.project.simple_finance_api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email(message = "Invalid email")
        @NotBlank(message = "An email address is mandatory")
        String email,
        @NotBlank(message = "A password is mandatory")
        @Size(min = 6, max = 18, message = "Password size must be between 6 and 18")
        String password) {
}