package br.edu.atitus.api_citytour.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordUpdateDTO(
        @NotBlank(message = "Old password is required.")
        String oldPassword,

        @NotBlank(message = "New password is required.")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "Invalid new password: must contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
        String newPassword
) {
}
