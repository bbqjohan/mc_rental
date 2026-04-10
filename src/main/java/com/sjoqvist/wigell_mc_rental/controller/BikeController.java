package com.sjoqvist.wigell_mc_rental.controller;

import com.sjoqvist.wigell_mc_rental.dto.BikeDto;
import com.sjoqvist.wigell_mc_rental.dto.BikeDtoCreate;
import com.sjoqvist.wigell_mc_rental.dto.BikeDtoUpdate;
import com.sjoqvist.wigell_mc_rental.service.BikeService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bikes")
public class BikeController {
    private final BikeService bikeService;

    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @GetMapping
    public ResponseEntity<List<BikeDto>> getAllBikes() {
        return ResponseEntity.ok(bikeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BikeDto> getBikeById(@PathVariable Long id) {
        return ResponseEntity.ok(bikeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BikeDto> createBike(@RequestBody @Valid BikeDtoCreate dto) {
        var entity = bikeService.create(dto);
        var location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(entity.id())
                        .toUri();

        return ResponseEntity.created(location).body(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BikeDto> updateBike(
            @PathVariable Long id, @RequestBody @Valid BikeDtoUpdate dto) {
        return ResponseEntity.ok(bikeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public void deleteBike(@PathVariable Long id) {
        bikeService.delete(id);
    }
}
