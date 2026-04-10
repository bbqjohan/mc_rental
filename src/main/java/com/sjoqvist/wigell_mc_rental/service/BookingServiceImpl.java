package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.entity.BikeStatus;
import com.sjoqvist.wigell_mc_rental.entity.Booking;
import com.sjoqvist.wigell_mc_rental.entity.BookingStatus;
import com.sjoqvist.wigell_mc_rental.exception.BikeNotFoundException;
import com.sjoqvist.wigell_mc_rental.exception.CustomerNotFoundException;
import com.sjoqvist.wigell_mc_rental.repository.BikeRepo;
import com.sjoqvist.wigell_mc_rental.repository.BookingRepo;
import com.sjoqvist.wigell_mc_rental.repository.CustomerRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Service
public class BookingServiceImpl {
    private final BikeRepo bikeRepo;
    private final CustomerRepo customerRepo;
    private final BookingRepo bookingRepo;

    public BookingServiceImpl(
            BikeRepo bikeRepo, CustomerRepo customerRepo, BookingRepo bookingRepo) {
        this.bikeRepo = bikeRepo;
        this.customerRepo = customerRepo;
        this.bookingRepo = bookingRepo;
    }

    @Transactional
    public void create(Long customerId, Long bikeId, LocalDate from, LocalDate to) {
        var customer =
                customerRepo
                        .findById(customerId)
                        .orElseThrow(() -> new CustomerNotFoundException(customerId));
        var bike = bikeRepo.findById(bikeId).orElseThrow(() -> new BikeNotFoundException(bikeId));

        if (from.isBefore(LocalDate.now())) {
            throw new RuntimeException("from is before now");
        }

        if (to.isBefore(from)) {
            throw new RuntimeException("to be before from");
        }

        if (bike.getStatus().equals(BikeStatus.UNAVAILABLE)) {
            throw new RuntimeException("bike is unavailable");
        }

        var bookings =
                bookingRepo.findAllByBikeIdAndStatusIn(
                        bikeId, Set.of(BookingStatus.ACTIVE, BookingStatus.RESERVED));
        bookings.forEach(
                booking -> {
                    if (!from.isBefore(booking.getFromDate())
                            && !from.isAfter(booking.getToDate())) {
                        throw new RuntimeException("from and to be before from and to be after");
                    }

                    if (!to.isBefore(booking.getFromDate()) && !to.isAfter(booking.getToDate())) {
                        throw new RuntimeException("from and to be before from and to be after");
                    }
                });

        var booking =
                new Booking(
                        bike,
                        customer,
                        from,
                        to,
                        null,
                        from.isEqual(LocalDate.now())
                                ? BookingStatus.ACTIVE
                                : BookingStatus.RESERVED);

        bike.setStatus(BikeStatus.UNAVAILABLE);

        bikeRepo.save(bike);
        bookingRepo.save(booking);
    }
}
