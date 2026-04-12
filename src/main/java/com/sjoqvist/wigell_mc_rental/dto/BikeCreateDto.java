package com.sjoqvist.wigell_mc_rental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BikeCreateDto(
        @NotBlank String model,
        @NotBlank String manufacturer,
        @NotNull @Positive Double dailyRateSek) {}
