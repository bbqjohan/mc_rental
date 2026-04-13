package com.sjoqvist.wigell_mc_rental.dto;

import com.sjoqvist.wigell_mc_rental.entity.BookingStatus;

import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BookingPatchDto(
        @Positive Long bikeId, LocalDate from, LocalDate to, BookingStatus status) {}
