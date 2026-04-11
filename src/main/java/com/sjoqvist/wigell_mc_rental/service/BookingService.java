package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingDto create(BookingCreateDto dto);

    Page<BookingDto> findAll(Pageable pageable);

    BookingDto findById(Long id);

    BookingDto update(Long id, BookingUpdateDto dto);

    BookingDto changeOngoingBooking(Long id, BookingPatchDatesDto dto);

    //    void delete(Long id);
}
