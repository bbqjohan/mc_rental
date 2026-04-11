package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.entity.Bike;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class BookingPriceCalculatorImpl implements BookingPriceCalculator {
    public double calculateTotalPrice(LocalDate from, LocalDate to, Bike bike) {
        return bike.getDailyRateSek() * Math.max(1L, ChronoUnit.DAYS.between(from, to));
    }
}
