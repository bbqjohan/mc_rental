package com.sjoqvist.wigell_mc_rental.mapper;

import com.sjoqvist.wigell_mc_rental.dto.AddressDto;
import com.sjoqvist.wigell_mc_rental.entity.Address;

public final class AddressMapper {
    private AddressMapper() {}

    public static AddressDto toAddressDto(Address address) {
        return new AddressDto(address.getId(), address.getStreet(), address.getCity(), address.getPostalCode());
    }

//    public static Customer fromCustomerDtoCreate(CustomerDtoCreate dto, Address address) {
//        return new Customer(dto.name(), address);
//    }
//
//    public static Customer update(Customer entity, CustomerDtoUpdate dto) {
//        entity.setName(dto.name());
//        return entity;
//    }
}
