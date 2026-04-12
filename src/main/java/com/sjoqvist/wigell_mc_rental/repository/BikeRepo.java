package com.sjoqvist.wigell_mc_rental.repository;

import com.sjoqvist.wigell_mc_rental.entity.Bike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeRepo extends JpaRepository<Bike, Long> {
    boolean existsByModelAndManufacturer(String model, String manufacturer);
}
