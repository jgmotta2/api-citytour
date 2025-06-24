package br.edu.atitus.api_citytour.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewDTO(
        @NotNull(message = "Rating is mandatory") @Min(value = 1, message = "Rating must be at least 1") @Max(value = 5, message = "Rating must be at most 5") int rating,
        @Size(max = 500, message = "Comment must be less than 500 characters") String comment
) {
}
