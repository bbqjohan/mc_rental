package com.sjoqvist.wigell_mc_rental.mapper;

import com.sjoqvist.wigell_mc_rental.dto.*;
import com.sjoqvist.wigell_mc_rental.entity.Bike;

public final class BikeMapper {
    private BikeMapper() {}

    public static BikeDto toBikeDto(Bike entity) {
        return new BikeDto(
                entity.getId(),
                entity.getModel(),
                entity.getManufacturer(),
                entity.getDailyRateSek());
    }

    public static Bike fromBikeDtoCreate(BikeCreateDto dto) {
        return new Bike(dto.model(), dto.manufacturer(), dto.dailyRateSek());
    }

    public static Bike update(Bike entity, BikeUpdateDto dto) {
        entity.setModel(dto.model());
        entity.setManufacturer(dto.manufacturer());
        entity.setDailyRateSek(dto.dailyRateSek());
        return entity;
    }
}
