package com.sjoqvist.wigell_mc_rental.mapper;

import com.sjoqvist.wigell_mc_rental.dto.AppUserCreateDto;
import com.sjoqvist.wigell_mc_rental.security.AppUser;

public final class AppUserMapper {
    private AppUserMapper() {}

    public static AppUser fromCreateDto(AppUserCreateDto dto) {
        return new AppUser(dto.username(), dto.password());
    }
}
