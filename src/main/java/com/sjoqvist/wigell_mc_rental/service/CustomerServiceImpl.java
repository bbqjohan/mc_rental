package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.CustomerDto;
import com.sjoqvist.wigell_mc_rental.dto.CustomerDtoCreate;
import com.sjoqvist.wigell_mc_rental.dto.CustomerDtoUpdate;
import com.sjoqvist.wigell_mc_rental.exception.AddressNotFoundException;
import com.sjoqvist.wigell_mc_rental.exception.CustomerNotFoundException;
import com.sjoqvist.wigell_mc_rental.exception.UserExistsException;
import com.sjoqvist.wigell_mc_rental.mapper.CustomerMapper;
import com.sjoqvist.wigell_mc_rental.repository.AddressRepo;
import com.sjoqvist.wigell_mc_rental.repository.AppUserRepo;
import com.sjoqvist.wigell_mc_rental.repository.CustomerRepo;
import com.sjoqvist.wigell_mc_rental.security.AppUser;
import com.sjoqvist.wigell_mc_rental.security.Role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerRepo customerRepo;
    private final AddressRepo addressRepo;
    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(
            CustomerRepo customerRepo,
            AddressRepo addressRepo,
            AppUserRepo appUserRepo,
            PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public CustomerDto create(CustomerDtoCreate dto) {
        CustomerDto result;

        try {
            log.info("Creating customer.");

            var address =
                    addressRepo
                            .findById(dto.addressId())
                            .orElseThrow(
                                    () ->
                                            AddressNotFoundException.withNotFoundMsg(
                                                    dto.addressId()));

            if (appUserRepo.existsByUsername(dto.user().username())) {
                throw new UserExistsException(
                        String.format(
                                "A user with this username already " + "exists: %s",
                                dto.user().username()),
                        dto.user().username());
            }

            var entity = CustomerMapper.fromCustomerDtoCreate(dto, address);
            entity.setAddress(address);

            var newUser =
                    new AppUser(
                            dto.user().username(),
                            passwordEncoder.encode(dto.user().password()),
                            Set.of(Role.USER));
            newUser.setCustomer(entity);

            entity = customerRepo.save(entity);
            appUserRepo.save(newUser);
            addressRepo.save(address);

            result = CustomerMapper.toCustomerDto(entity);

            log.info("Customer successfully created. id={}", entity.getId());
        } catch (Exception e) {
            log.error("Failed to create customer. payload={}, error={}", dto, e.getMessage());
            throw e;
        }

        return result;
    }

    @Transactional(readOnly = true)
    public Page<CustomerDto> findAll(Pageable pageable) {
        return customerRepo.findAll(pageable).map(CustomerMapper::toCustomerDto);
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
        try {
            log.info("Updating customer. id={}", id);

            var entity =
                    customerRepo
                            .findById(id)
                            .orElseThrow(() -> CustomerNotFoundException.withNotFoundMsg(id));

            CustomerMapper.update(entity, dto);
            log.info("Customer successfully updated. id={}", id);

            return CustomerMapper.toCustomerDto(entity);
        } catch (Exception e) {
            log.error("Failed to update customer. payload={}, error={}", dto, e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            log.info("Deleting customer. id={}", id);

            if (!customerRepo.existsById(id)) {
                throw CustomerNotFoundException.withNotFoundMsg(id);
            }

            customerRepo.deleteById(id);

            log.info("Customer successfully deleted. id={}", id);
        } catch (Exception e) {
            log.error("Failed to delete customer. payload={}", id);
            throw e;
        }
    }
}
