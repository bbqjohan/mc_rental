package com.sjoqvist.wigell_mc_rental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BookingPatchDatesDto(
        @Positive Long bikeId, @NotNull LocalDate from, @NotNull LocalDate to) {}
