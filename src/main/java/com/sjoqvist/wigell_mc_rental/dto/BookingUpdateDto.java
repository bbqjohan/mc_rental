package com.sjoqvist.wigell_mc_rental.dto;

import com.sjoqvist.wigell_mc_rental.entity.BookingStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BookingUpdateDto(
        @NotNull @Positive Long bikeId,
        @NotNull @Positive Long customerId,
        @NotNull LocalDate from,
        @NotNull LocalDate to,
        @NotNull BookingStatus status) {}
