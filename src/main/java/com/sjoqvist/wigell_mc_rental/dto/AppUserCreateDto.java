package com.sjoqvist.wigell_mc_rental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AppUserCreateDto(
        @NotBlank @Size(min = 4, max = 50) String username,
        @NotBlank @Size(min = 4, max = 50) String password) {}
