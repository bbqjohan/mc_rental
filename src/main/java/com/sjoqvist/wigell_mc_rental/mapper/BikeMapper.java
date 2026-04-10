package com.sjoqvist.wigell_mc_rental.mapper;

import com.sjoqvist.wigell_mc_rental.dto.*;
import com.sjoqvist.wigell_mc_rental.entity.Bike;
import com.sjoqvist.wigell_mc_rental.entity.BikeStatus;

public final class BikeMapper {
    private BikeMapper() {}

    public static BikeDto toBikeDto(Bike entity) {
        return new BikeDto(
                entity.getId(),
                entity.getModel(),
                entity.getManufacturer(),
                entity.getDailyRateSek());
    }

    public static Bike fromBikeDtoCreate(BikeDtoCreate dto) {
        return new Bike(dto.model(), dto.manufacturer(), dto.dailyRateSek(), BikeStatus.AVAILABLE);
    }

    public static Bike update(Bike entity, BikeDtoUpdate dto) {
        entity.setModel(dto.model());
        entity.setManufacturer(dto.manufacturer());
        entity.setDailyRateSek(dto.dailyRateSek());
        entity.setStatus(dto.status());
        return entity;
    }
}
