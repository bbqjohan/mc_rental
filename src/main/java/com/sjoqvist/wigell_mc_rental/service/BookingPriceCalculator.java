package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.entity.Bike;

import java.time.LocalDate;

public interface BookingPriceCalculator {
    double calculateTotalPrice(LocalDate from, LocalDate to, Bike bike);
}
