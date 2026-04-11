package com.sjoqvist.wigell_mc_rental.mapper;

import com.sjoqvist.wigell_mc_rental.dto.*;
import com.sjoqvist.wigell_mc_rental.entity.Bike;
import com.sjoqvist.wigell_mc_rental.entity.Booking;
import com.sjoqvist.wigell_mc_rental.entity.BookingStatus;
import com.sjoqvist.wigell_mc_rental.entity.Customer;
import com.sjoqvist.wigell_mc_rental.lib.MoneyConverter;

public final class BookingMapper {
    private BookingMapper() {}

    public static BookingDto toBookingDto(Booking entity) {
        return new BookingDto(
                entity.getId(),
                entity.getCustomer().getId(),
                entity.getBike().getId(),
                entity.getFromDate(),
                entity.getToDate(),
                entity.getPriceTotalSek(),
                MoneyConverter.sekToGbp(entity.getPriceTotalSek()),
                entity.getStatus());
    }

    public static Booking fromBookingDtoCreate(
            BookingCreateDto dto,
            Bike bike,
            Customer customer,
            Double priceTotal,
            BookingStatus bookingStatus) {
        return new Booking(bike, customer, dto.from(), dto.to(), priceTotal, bookingStatus);
    }

    public static Booking update(
            Booking entity, BookingUpdateDto dto, Bike bike, Customer customer) {
        entity.setFromDate(dto.from());
        entity.setFromDate(dto.to());
        entity.setStatus(dto.status());
        entity.setBike(bike);
        entity.setCustomer(customer);

        return entity;
    }
}
