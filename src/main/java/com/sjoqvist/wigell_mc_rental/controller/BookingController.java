package com.sjoqvist.wigell_mc_rental.controller;

import com.sjoqvist.wigell_mc_rental.dto.BookingCreateDto;
import com.sjoqvist.wigell_mc_rental.dto.BookingDto;
import com.sjoqvist.wigell_mc_rental.dto.BookingPatchDatesDto;
import com.sjoqvist.wigell_mc_rental.dto.BookingUpdateDto;
import com.sjoqvist.wigell_mc_rental.service.BookingService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<Page<BookingDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(bookingService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> findAll(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(
            @RequestBody @Valid BookingCreateDto bookingDto) {
        var booking = bookingService.create(bookingDto);
        var location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(booking.bookingId())
                        .toUri();

        return ResponseEntity.created(location).body(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDto> updateBooking(
            @PathVariable Long id, @RequestBody @Valid BookingUpdateDto dto) {
        return ResponseEntity.ok(bookingService.update(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingDto> updateBooking(
            @PathVariable Long id, @RequestBody @Valid BookingPatchDatesDto dto) {
        return ResponseEntity.ok(bookingService.changeOngoingBooking(id, dto));
    }
}
