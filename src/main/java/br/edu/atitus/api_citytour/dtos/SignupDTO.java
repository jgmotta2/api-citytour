package br.edu.atitus.api_citytour.dtos;

import java.time.LocalDate;

public record SignupDTO(
        String name,
        String email,
        String password,
        LocalDate birthDate)
{ }
