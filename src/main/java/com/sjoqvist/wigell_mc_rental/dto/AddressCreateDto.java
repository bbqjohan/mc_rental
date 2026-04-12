package com.sjoqvist.wigell_mc_rental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressCreateDto(
        @NotBlank @Size(max = 100) String city,
        @NotBlank @Size(max = 100) String street,
        @NotBlank @Size(max = 20) String postalCode) {}
