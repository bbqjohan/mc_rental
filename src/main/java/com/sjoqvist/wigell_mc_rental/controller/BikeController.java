package com.sjoqvist.wigell_mc_rental.controller;

import com.sjoqvist.wigell_mc_rental.dto.BikeCreateDto;
import com.sjoqvist.wigell_mc_rental.dto.BikeDto;
import com.sjoqvist.wigell_mc_rental.dto.BikeUpdateDto;
import com.sjoqvist.wigell_mc_rental.service.BikeService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/bikes")
public class BikeController {
    private final BikeService bikeService;

    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @GetMapping
    public ResponseEntity<Page<BikeDto>> getAllBikes(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(bikeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BikeDto> getBikeById(@PathVariable Long id) {
        return ResponseEntity.ok(bikeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BikeDto> createBike(@RequestBody @Valid BikeCreateDto dto) {
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
            @PathVariable Long id, @RequestBody @Valid BikeUpdateDto dto) {
        return ResponseEntity.ok(bikeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public void deleteBike(@PathVariable Long id) {
        bikeService.delete(id);
    }
}
