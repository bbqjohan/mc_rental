package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.CustomerDto;
import com.sjoqvist.wigell_mc_rental.dto.CustomerDtoCreate;
import com.sjoqvist.wigell_mc_rental.dto.CustomerDtoUpdate;
import com.sjoqvist.wigell_mc_rental.entity.AppUser;
import com.sjoqvist.wigell_mc_rental.exception.AddressNotFoundException;
import com.sjoqvist.wigell_mc_rental.exception.CustomerNotFoundException;
import com.sjoqvist.wigell_mc_rental.exception.UserExistsException;
import com.sjoqvist.wigell_mc_rental.mapper.CustomerMapper;
import com.sjoqvist.wigell_mc_rental.repository.AddressRepo;
import com.sjoqvist.wigell_mc_rental.repository.AppUserRepo;
import com.sjoqvist.wigell_mc_rental.repository.CustomerRepo;
import com.sjoqvist.wigell_mc_rental.security.Role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;
    private final AddressRepo addressRepo;
    private final AppUserRepo appUserRepo;

    public CustomerService(
            CustomerRepo customerRepo, AddressRepo addressRepo, AppUserRepo appUserRepo) {
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
        this.appUserRepo = appUserRepo;
    }

    @Transactional
    public CustomerDto create(CustomerDtoCreate dto) {
        var address =
                addressRepo
                        .findById(dto.addressId())
                        .orElseThrow(
                                () -> AddressNotFoundException.withNotFoundMsg(dto.addressId()));

        if (appUserRepo.existsByUsername(dto.user().username())) {
            throw new UserExistsException(
                    String.format(
                            "A user with this username already " + "exists: %s",
                            dto.user().username()),
                    dto.user().username());
        }

        var customer = CustomerMapper.fromCustomerDtoCreate(dto, address);
        customer.setAddress(address);

        var newUser = new AppUser(dto.user().username(), dto.user().password(), Set.of(Role.USER));
        newUser.setCustomer(customer);

        customer = customerRepo.save(customer);
        appUserRepo.save(newUser);
        addressRepo.save(address);

        return CustomerMapper.toCustomerDto(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> findAll() {
        return customerRepo.findAll().stream().map(CustomerMapper::toCustomerDto).toList();
    }

    @Transactional(readOnly = true)
    public CustomerDto findById(Long id) {
        var entity =
                customerRepo
                        .findById(id)
                        .orElseThrow(() -> CustomerNotFoundException.withNotFoundMsg(id));

        return CustomerMapper.toCustomerDto(entity);
    }

    @Transactional
    public CustomerDto update(Long id, CustomerDtoUpdate dto) {
        var entity =
                customerRepo
                        .findById(id)
                        .orElseThrow(() -> CustomerNotFoundException.withNotFoundMsg(id));

        CustomerMapper.update(entity, dto);
        return CustomerMapper.toCustomerDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!customerRepo.existsById(id)) {
            throw CustomerNotFoundException.withNotFoundMsg(id);
        }

        customerRepo.deleteById(id);
    }
}
