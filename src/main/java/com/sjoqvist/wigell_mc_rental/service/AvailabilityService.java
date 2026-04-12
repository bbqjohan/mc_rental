package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.BikeDto;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {
    List<BikeDto> findAllAvailableBikes(LocalDate from, LocalDate to);
}
