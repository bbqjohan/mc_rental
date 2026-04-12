package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerDto create(CustomerDtoCreate dto);

    Page<CustomerDto> findAll(Pageable pageable);

    CustomerDto findById(Long id);

    CustomerDto update(Long id, CustomerDtoUpdate dto);

    void delete(Long id);

    AddressDto addAddressToCustomer(Long customerId, AddressCreateDto addressDto);

    void removeAddressFromCustomer(Long customerId, Long addressId);
}
