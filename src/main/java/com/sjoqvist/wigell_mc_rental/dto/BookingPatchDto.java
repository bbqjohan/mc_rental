package com.sjoqvist.wigell_mc_rental.dto;

import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BookingPatchDto(
        @Positive Long bikeId, LocalDate from, LocalDate to) {}
