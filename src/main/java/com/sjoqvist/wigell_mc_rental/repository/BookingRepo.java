package com.sjoqvist.wigell_mc_rental.repository;

import com.sjoqvist.wigell_mc_rental.entity.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    //    List<Booking> findAllByBikeIdAndStatusIn(Long bikeId, Set<BookingStatus> bookingStatuses);

    //    List<Booking> findAllByBikeIdAndFromDateIsAfter(Long id, LocalDate fromDate);

    List<Booking> findAllByCustomerId(Long customerId);

    @Query(
"""
    SELECT b FROM Booking b
    WHERE b.bike.id = :bikeId
      AND b.id <> :bookingId
      AND b.fromDate <= :toDate
      AND b.toDate >= :fromDate
""")
    List<Booking> findConflictingBookings(
            Long bikeId, Long bookingId, LocalDate fromDate, LocalDate toDate);
}
