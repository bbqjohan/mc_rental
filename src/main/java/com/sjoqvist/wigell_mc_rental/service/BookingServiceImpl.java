package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.BookingCreateDto;
import com.sjoqvist.wigell_mc_rental.dto.BookingDto;
import com.sjoqvist.wigell_mc_rental.dto.BookingPatchDatesDto;
import com.sjoqvist.wigell_mc_rental.dto.BookingUpdateDto;
import com.sjoqvist.wigell_mc_rental.entity.BookingStatus;
import com.sjoqvist.wigell_mc_rental.exception.*;
import com.sjoqvist.wigell_mc_rental.mapper.BookingMapper;
import com.sjoqvist.wigell_mc_rental.repository.AppUserRepo;
import com.sjoqvist.wigell_mc_rental.repository.BikeRepo;
import com.sjoqvist.wigell_mc_rental.repository.BookingRepo;
import com.sjoqvist.wigell_mc_rental.repository.CustomerRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class BookingServiceImpl implements BookingService {
    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);
    private final BikeRepo bikeRepo;
    private final CustomerRepo customerRepo;
    private final BookingRepo bookingRepo;
    private final BookingPriceCalculator bookingPriceCalculator;
    private final AppUserRepo appUserRepo;

    public BookingServiceImpl(
            BikeRepo bikeRepo,
            CustomerRepo customerRepo,
            BookingRepo bookingRepo,
            BookingPriceCalculator bookingPriceCalculator,
            AppUserRepo appUserRepo) {
        this.bikeRepo = bikeRepo;
        this.customerRepo = customerRepo;
        this.bookingRepo = bookingRepo;
        this.bookingPriceCalculator = bookingPriceCalculator;
        this.appUserRepo = appUserRepo;
    }

    @Transactional
    public BookingDto create(BookingCreateDto dto) {
        try {
            log.info("Creating a booking. payload={} ", dto);

            var customer =
                    customerRepo
                            .findById(dto.customerId())
                            .orElseThrow(() -> new CustomerNotFoundException(dto.customerId()));

            var bike =
                    bikeRepo.findById(dto.bikeId())
                            .orElseThrow(() -> new BikeNotFoundException(dto.bikeId()));

            assertFromDateNotBeforeToday(dto.from());
            assertToDateNotBeforeFromDate(dto.to(), dto.from());

            var bookings =
                    bookingRepo.findAllByBikeIdAndStatusIn(
                            dto.bikeId(), Set.of(BookingStatus.ACTIVE, BookingStatus.RESERVED));

            bookings.forEach(
                    booking -> {
                        if (!dto.from().isBefore(booking.getFromDate())
                                && !dto.from().isAfter(booking.getToDate())) {
                            throw new InvalidBookingDateException(
                                    String.format(
                                            "The 'from' date %s is unavailable for bike %d",
                                            dto.from(), booking.getBike().getId()),
                                    dto.from());
                        }

                        if (!dto.to().isBefore(booking.getFromDate())
                                && !dto.to().isAfter(booking.getToDate())) {
                            throw new InvalidBookingDateException(
                                    String.format(
                                            "The 'to' date %s is unavailable for bike %d",
                                            dto.to(), booking.getBike().getId()),
                                    dto.to());
                        }
                    });

            var bookingStatus =
                    dto.from().isEqual(LocalDate.now())
                            ? BookingStatus.ACTIVE
                            : BookingStatus.RESERVED;

            var priceTotal = bookingPriceCalculator.calculateTotalPrice(dto.from(), dto.to(), bike);

            var booking =
                    BookingMapper.fromBookingDtoCreate(
                            dto, bike, customer, priceTotal, bookingStatus);

            bikeRepo.save(bike);
            booking = bookingRepo.save(booking);

            log.info("Successfully created a booking for customer. payload={} ", dto);

            return BookingMapper.toBookingDto(booking);
        } catch (Exception e) {
            log.error("Failed to create a booking. payload={}, error={}", dto, e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<BookingDto> findAll(Pageable pageable) {
        return bookingRepo.findAll(pageable).map(BookingMapper::toBookingDto);
    }

    @Transactional(readOnly = true)
    public BookingDto findById(Long id) {
        var entity = bookingRepo.findById(id).orElseThrow(() -> new BookingNotFoundException(id));

        return BookingMapper.toBookingDto(entity);
    }

    @Transactional(readOnly = true)
    public List<BookingDto> findAllByCustomerId(Long customerId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        var username = securityContext.getAuthentication().getName();

        var user =
                appUserRepo
                        .findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));

        if (!user.getCustomer().getId().equals(customerId)) {
            throw new AccessDeniedException("You cannot view other customers' bookings");
        }

        return bookingRepo.findAllByCustomerId(customerId).stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Transactional
    public BookingDto update(Long id, BookingUpdateDto dto) {
        try {
            log.info("Updating a booking. id={}, payload={} ", id, dto);

            var customer =
                    customerRepo
                            .findById(dto.customerId())
                            .orElseThrow(() -> new CustomerNotFoundException(dto.customerId()));

            var bike =
                    bikeRepo.findById(dto.bikeId())
                            .orElseThrow(() -> new BikeNotFoundException(dto.bikeId()));

            var entity =
                    bookingRepo.findById(id).orElseThrow(() -> new BookingNotFoundException(id));

            entity = bookingRepo.save(BookingMapper.update(entity, dto, bike, customer));

            log.info("Successfully updated a booking. id={}, payload={} ", id, dto);

            return BookingMapper.toBookingDto(entity);
        } catch (Exception e) {
            log.error(
                    "Failed to update booking. id={}, payload={}, error={}",
                    id,
                    dto,
                    e.getMessage());

            throw e;
        }
    }

    @Transactional
    public BookingDto changeOngoingBooking(Long id, BookingPatchDatesDto dto) {
        try {
            log.info("Updating booking dates. id={}, payload={} ", id, dto);

            var booking =
                    bookingRepo.findById(id).orElseThrow(() -> new BookingNotFoundException(id));

            if (booking.getStatus().equals(BookingStatus.COMPLETED)) {
                throw new RuntimeException("Booking is already completed.");
            }

            var bike =
                    dto.bikeId() != null
                            ? bikeRepo.findById(dto.bikeId())
                                    .orElseThrow(() -> new BikeNotFoundException(dto.bikeId()))
                            : booking.getBike();

            var nextBookings =
                    bookingRepo.findAllByBikeIdAndFromDateIsAfter(bike.getId(), LocalDate.now());

            assertFromDateNotBeforeToday(dto.from());

            nextBookings.forEach(
                    nextBooking -> {
                        if (isDateWithinRange(
                                dto.from(), nextBooking.getFromDate(), nextBooking.getToDate())) {
                            throw new InvalidBookingDateException(
                                    String.format(
                                            "The 'from' date %s is unavailable for bike %d",
                                            dto.from(), nextBooking.getBike().getId()),
                                    dto.from());
                        }

                        if (isDateWithinRange(
                                dto.to(), nextBooking.getFromDate(), nextBooking.getToDate())) {
                            throw new InvalidBookingDateException(
                                    String.format(
                                            "The 'to' date %s is unavailable for bike %d",
                                            dto.to(), nextBooking.getBike().getId()),
                                    dto.to());
                        }
                    });

            var priceTotal = bookingPriceCalculator.calculateTotalPrice(dto.from(), dto.to(), bike);
            var bookingStatus =
                    dto.from().isEqual(LocalDate.now())
                            ? BookingStatus.ACTIVE
                            : BookingStatus.RESERVED;

            booking.setFromDate(dto.from());
            booking.setToDate(dto.to());
            booking.setBike(bike);
            booking.setPriceTotalSek(priceTotal);
            booking.setStatus(bookingStatus);
            booking = bookingRepo.save(booking);

            log.info("Successfully updated booking dates. id={}, payload={} ", id, dto);

            return BookingMapper.toBookingDto(booking);
        } catch (Exception e) {
            log.error(
                    "Failed to update booking. id={}, payload={}, error={}",
                    id,
                    dto,
                    e.getMessage());

            throw e;
        }
    }

    private void assertFromDateNotBeforeToday(LocalDate from) {
        if (from.isBefore(LocalDate.now())) {
            throw new InvalidBookingDateException(
                    String.format(
                            "The 'from' %s date cannot be before today's date %s",
                            from, LocalDate.now()),
                    from);
        }
    }

    private void assertToDateNotBeforeFromDate(LocalDate to, LocalDate from) {
        if (to.isBefore(from)) {
            throw new InvalidBookingDateException(
                    String.format("The 'to' date %s cannot be before the 'from' date %s", to, from),
                    to);
        }
    }

    private boolean isDateWithinRange(LocalDate date, LocalDate from, LocalDate to) {
        return !date.isBefore(from) && !date.isAfter(to);
    }
}
