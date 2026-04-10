package com.sjoqvist.wigell_mc_rental.mapper;

import com.sjoqvist.wigell_mc_rental.dto.CustomerDto;
import com.sjoqvist.wigell_mc_rental.dto.CustomerDtoCreate;
import com.sjoqvist.wigell_mc_rental.dto.CustomerDtoUpdate;
import com.sjoqvist.wigell_mc_rental.entity.Address;
import com.sjoqvist.wigell_mc_rental.entity.Customer;

public final class CustomerMapper {
    private CustomerMapper() {}

    public static CustomerDto toCustomerDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getName());
    }

    public static Customer fromCustomerDtoCreate(CustomerDtoCreate dto, Address address) {
        return new Customer(dto.name(), address);
    }

    public static Customer update(Customer entity, CustomerDtoUpdate dto) {
        entity.setName(dto.name());
        return entity;
    }
}
