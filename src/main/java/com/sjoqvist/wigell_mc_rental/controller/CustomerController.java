package com.sjoqvist.wigell_mc_rental.controller;

import com.sjoqvist.wigell_mc_rental.dto.*;
import com.sjoqvist.wigell_mc_rental.service.CustomerServiceImpl;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getCustomers(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(customerService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody @Valid CustomerDtoCreate dto) {
        var customer = customerService.create(dto);
        var location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(customer.id())
                        .toUri();

        return ResponseEntity.created(location).body(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable Long id, @RequestBody @Valid CustomerDtoUpdate dto) {
        return ResponseEntity.ok(customerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
    }

    @PostMapping("/{customerId}/addresses")
    public ResponseEntity<AddressDto> addAddressToCustomer(
            @PathVariable Long customerId, @RequestBody @Valid AddressCreateDto addressDto) {
        var saved = customerService.addAddressToCustomer(customerId, addressDto);
        var location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{customerId}")
                        .buildAndExpand(customerId)
                        .toUri();

        return ResponseEntity.created(location).body(saved);
    }
}
