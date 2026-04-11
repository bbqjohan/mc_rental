package com.sjoqvist.wigell_mc_rental.mapper;

import com.sjoqvist.wigell_mc_rental.dto.AppUserDtoCreate;
import com.sjoqvist.wigell_mc_rental.security.AppUser;

public final class AppUserMapper {
    private AppUserMapper() {}

    public static AppUser fromCreateDto(AppUserDtoCreate dto) {
        return new AppUser(dto.username(), dto.password());
    }
}
