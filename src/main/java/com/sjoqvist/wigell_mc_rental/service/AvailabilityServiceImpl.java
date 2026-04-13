package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.BikeDto;
import com.sjoqvist.wigell_mc_rental.mapper.BikeMapper;
import com.sjoqvist.wigell_mc_rental.repository.BikeRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {
    private final BikeRepo bikeRepo;

    public AvailabilityServiceImpl(BikeRepo bikeRepo) {
        this.bikeRepo = bikeRepo;
    }

    @Transactional
    public List<BikeDto> findAllAvailableBikes(LocalDate from, LocalDate to) {
        return bikeRepo.findAvailableBikes(from, to).stream().map(BikeMapper::toBikeDto).toList();
    }
}
