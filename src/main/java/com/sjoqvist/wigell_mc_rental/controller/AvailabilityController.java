package com.sjoqvist.wigell_mc_rental.controller;

import com.sjoqvist.wigell_mc_rental.service.AvailabilityService;
import com.sjoqvist.wigell_mc_rental.service.BikeService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/availability")
public class AvailabilityController {
    private final BikeService bikeService;
    private final AvailabilityService availabilityService;

    public AvailabilityController(
            BikeService bikeService, AvailabilityService availabilityService) {
        this.bikeService = bikeService;
        this.availabilityService = availabilityService;
    }

//    @GetMapping
//    public ResponseEntity<List<BikeDto>> findAllAvailableBikes(
//            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
//            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
//        return ResponseEntity.ok(availabilityService.findAllAvailableBikes(from, to));
//    }
}
