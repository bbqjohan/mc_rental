package com.sjoqvist.wigell_mc_rental.repository;

import com.sjoqvist.wigell_mc_rental.entity.Booking;
import com.sjoqvist.wigell_mc_rental.entity.BookingStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBikeIdAndStatusIn(Long bikeId, Set<BookingStatus> bookingStatuses);

    List<Booking> findAllByBikeIdAndFromDateIsAfter(Long id, LocalDate fromDate);
}
