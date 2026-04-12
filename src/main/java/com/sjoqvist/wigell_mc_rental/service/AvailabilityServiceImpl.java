package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.BikeDto;
import com.sjoqvist.wigell_mc_rental.mapper.BikeMapper;
import com.sjoqvist.wigell_mc_rental.repository.BikeRepo;
import com.sjoqvist.wigell_mc_rental.repository.BookingRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {
    private final BikeRepo bikeRepo;
    private final BookingRepo bookingRepo;

    public AvailabilityServiceImpl(BikeRepo bikeRepo, BookingRepo bookingRepo) {
        this.bikeRepo = bikeRepo;
        this.bookingRepo = bookingRepo;
    }

    @Transactional
    public List<BikeDto> findAllAvailableBikes(LocalDate from, LocalDate to) {
        return bikeRepo.findAvailableBikes(from, to).stream().map(BikeMapper::toBikeDto).toList();
    }
}
