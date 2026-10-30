package com.zaur.finance_tracker.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    String name;

    @NotBlank(message = "Surname is required")
    String surname;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    String email;

    @NotBlank(message = "Username is required")
    String username;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @NotBlank(message = "Password is required")
    String password;
}
