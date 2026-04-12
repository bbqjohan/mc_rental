package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.BikeCreateDto;
import com.sjoqvist.wigell_mc_rental.dto.BikeDto;
import com.sjoqvist.wigell_mc_rental.dto.BikeUpdateDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BikeService {
    BikeDto create(BikeCreateDto dto);

    Page<BikeDto> findAll(Pageable pageable);

    BikeDto findById(Long id);

    BikeDto update(Long id, BikeUpdateDto dto);

    void delete(Long id);
}
