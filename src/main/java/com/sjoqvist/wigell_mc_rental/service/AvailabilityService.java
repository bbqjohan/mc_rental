package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.repository.BikeRepo;
import com.sjoqvist.wigell_mc_rental.repository.BookingRepo;

import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {
    private final BikeRepo bikeRepo;
    private final BookingRepo bookingRepo;

    public AvailabilityService(BikeRepo bikeRepo, BookingRepo bookingRepo) {
        this.bikeRepo = bikeRepo;
        this.bookingRepo = bookingRepo;
    }

//    @Transactional
//    public List<BikeDto> getAllAvailableBikes(LocalDate from, LocalDate to) {
//
//    }
}
