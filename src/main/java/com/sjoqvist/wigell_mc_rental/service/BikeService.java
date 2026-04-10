package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.*;
import com.sjoqvist.wigell_mc_rental.exception.BikeExistsException;
import com.sjoqvist.wigell_mc_rental.exception.BikeNotFoundException;
import com.sjoqvist.wigell_mc_rental.mapper.BikeMapper;
import com.sjoqvist.wigell_mc_rental.repository.AddressRepo;
import com.sjoqvist.wigell_mc_rental.repository.AppUserRepo;
import com.sjoqvist.wigell_mc_rental.repository.BikeRepo;
import com.sjoqvist.wigell_mc_rental.repository.CustomerRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BikeService {
    private final CustomerRepo customerRepo;
    private final AddressRepo addressRepo;
    private final AppUserRepo appUserRepo;
    private final BikeRepo bikeRepo;

    public BikeService(
            CustomerRepo customerRepo,
            AddressRepo addressRepo,
            AppUserRepo appUserRepo,
            BikeRepo bikeRepo) {
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
        this.appUserRepo = appUserRepo;
        this.bikeRepo = bikeRepo;
    }

    @Transactional
    public BikeDto create(BikeDtoCreate dto) {
        if (bikeRepo.existsByModelAndManufacturer(dto.model(), dto.manufacturer())) {
            throw new BikeExistsException(dto.model(), dto.manufacturer());
        }

        var entity = BikeMapper.fromBikeDtoCreate(dto);

        return BikeMapper.toBikeDto(bikeRepo.save(entity));
    }

    @Transactional(readOnly = true)
    public List<BikeDto> findAll() {
        return bikeRepo.findAll().stream().map(BikeMapper::toBikeDto).toList();
    }

    @Transactional(readOnly = true)
    public BikeDto findById(Long id) {
        return BikeMapper.toBikeDto(
                bikeRepo.findById(id).orElseThrow(() -> new BikeNotFoundException(id)));
    }

    @Transactional
    public BikeDto update(Long id, BikeDtoUpdate dto) {
        var entity = bikeRepo.findById(id).orElseThrow(() -> new BikeNotFoundException(id));

        BikeMapper.update(entity, dto);

        return BikeMapper.toBikeDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!bikeRepo.existsById(id)) {
            throw new BikeNotFoundException(id);
        }

        bikeRepo.deleteById(id);
    }
}
