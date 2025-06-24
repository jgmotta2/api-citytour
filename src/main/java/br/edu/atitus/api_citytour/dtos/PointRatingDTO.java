package br.edu.atitus.api_citytour.dtos;

import java.util.UUID;

public record PointRatingDTO(
        UUID id,
        String description,
        double averageRating,
        long reviewCount
) {}
