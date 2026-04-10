package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.BikeDto;
import com.sjoqvist.wigell_mc_rental.dto.BikeDtoCreate;
import com.sjoqvist.wigell_mc_rental.dto.BikeDtoUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BikeService {
    BikeDto create(BikeDtoCreate dto);

    Page<BikeDto> findAll(Pageable pageable);

    BikeDto findById(Long id);

    BikeDto update(Long id, BikeDtoUpdate dto);

    void delete(Long id);
}
