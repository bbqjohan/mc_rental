package com.sjoqvist.wigell_mc_rental.dto;

import jakarta.validation.constraints.*;

public record CustomerDtoCreate(
        @NotBlank @Size(max = 100) String name,
        @NotNull @Positive Long addressId,
        AppUserCreateDto user) {}
