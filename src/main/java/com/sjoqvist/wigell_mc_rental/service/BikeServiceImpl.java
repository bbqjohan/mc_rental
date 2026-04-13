package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.*;
import com.sjoqvist.wigell_mc_rental.exception.BikeExistsException;
import com.sjoqvist.wigell_mc_rental.exception.BikeNotFoundException;
import com.sjoqvist.wigell_mc_rental.mapper.BikeMapper;
import com.sjoqvist.wigell_mc_rental.repository.BikeRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BikeServiceImpl implements BikeService {
    private static final Logger log = LoggerFactory.getLogger(BikeServiceImpl.class);
    private final BikeRepo bikeRepo;

    public BikeServiceImpl(
            BikeRepo bikeRepo) {
        this.bikeRepo = bikeRepo;
    }

    @Transactional
    public BikeDto create(BikeCreateDto dto) {
        try {
            log.info("Creating bike.");

            if (bikeRepo.existsByModelAndManufacturer(dto.model(), dto.manufacturer())) {
                throw new BikeExistsException(dto.model(), dto.manufacturer());
            }

            var entity = BikeMapper.fromBikeDtoCreate(dto);
            entity = bikeRepo.save(entity);
            log.info("Bike successfully created. id={}", entity.getId());

            return BikeMapper.toBikeDto(entity);
        } catch (Exception e) {
            log.error("Failed to create bike. payload={}, error={}", dto, e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<BikeDto> findAll(Pageable pageable) {
        return bikeRepo.findAll(pageable).map(BikeMapper::toBikeDto);
    }

    @Transactional(readOnly = true)
    public BikeDto findById(Long id) {
        return BikeMapper.toBikeDto(
                bikeRepo.findById(id).orElseThrow(() -> new BikeNotFoundException(id)));
    }

    @Transactional
    public BikeDto update(Long id, BikeUpdateDto dto) {
        try {
            log.info("Updating bike. id={}", id);
            var entity = bikeRepo.findById(id).orElseThrow(() -> new BikeNotFoundException(id));

            BikeMapper.update(entity, dto);
            log.info("Bike successfully updated. id={}", id);

            return BikeMapper.toBikeDto(entity);
        } catch (Exception e) {
            log.error("Failed to update bike. id={}, error={}", dto, e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            log.info("Deleting bike. id={}", id);

            if (!bikeRepo.existsById(id)) {
                throw new BikeNotFoundException(id);
            }

            bikeRepo.deleteById(id);

            log.info("Bike successfully deleted. id={}", id);
        } catch (Exception e) {
            log.error("Failed to delete bike. id={}, error={}", id, e.getMessage());
            throw e;
        }
    }
}
