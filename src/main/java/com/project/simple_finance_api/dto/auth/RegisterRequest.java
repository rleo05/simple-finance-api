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
        String password,
        @NotBlank(message = "A first name is mandatory")
        @Size(min = 4, max = 25, message = "First name size must be between 4 and 25")
        String first_name,
        @NotBlank(message = "A last name is mandatory")
        @Size(min = 2, max = 15, message = "Last name size must be between 2 and 15")
        String last_name,
        @Size(min = 9, max = 9, message = "Document size must be 9")
        @NotBlank(message = "A document is mandatory")
        String document) {
}