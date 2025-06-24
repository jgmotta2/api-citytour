package br.edu.atitus.api_citytour.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PointDTO(@NotBlank(message = "Description is mandatory")
                        @Size(max = 250, message = "Description must be less than 250 characters")
                        String description,

                       @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
                        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
                        double latitude,

                       @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
                        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
                        double longitude) {

}
