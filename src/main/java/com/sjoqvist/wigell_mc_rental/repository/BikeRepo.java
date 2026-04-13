package com.sjoqvist.wigell_mc_rental.repository;

import com.sjoqvist.wigell_mc_rental.entity.Bike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BikeRepo extends JpaRepository<Bike, Long> {
    @Query(
"""
    SELECT b FROM Bike b
    WHERE b.id NOT IN (
        SELECT bk.bike.id FROM Booking bk
        WHERE bk.fromDate <= :toDate
        AND bk.toDate >= :fromDate
    )
""")
    List<Bike> findAvailableBikes(
            @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
