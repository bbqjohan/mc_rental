package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.dto.*;
import com.sjoqvist.wigell_mc_rental.entity.BookingStatus;
import com.sjoqvist.wigell_mc_rental.exception.*;
import com.sjoqvist.wigell_mc_rental.mapper.BookingMapper;
import com.sjoqvist.wigell_mc_rental.repository.BikeRepo;
import com.sjoqvist.wigell_mc_rental.repository.BookingRepo;
import com.sjoqvist.wigell_mc_rental.repository.CustomerRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);
    private final BikeRepo bikeRepo;
    private final CustomerRepo customerRepo;
    private final BookingRepo bookingRepo;
    private final BookingPriceCalculator bookingPriceCalculator;
    private final UserService userService;

    public BookingServiceImpl(
            BikeRepo bikeRepo,
            CustomerRepo customerRepo,
            BookingRepo bookingRepo,
            BookingPriceCalculator bookingPriceCalculator,
            UserService userService) {
        this.bikeRepo = bikeRepo;
        this.customerRepo = customerRepo;
        this.bookingRepo = bookingRepo;
        this.bookingPriceCalculator = bookingPriceCalculator;
        this.userService = userService;
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

            if (!userService.hasAccess(customer.getId())) {
                throw new AccessDeniedException("You cannot create a booking for another customer");
            }

            if (dto.from().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "The from-date cannot be before the today's date.");
            }

            if (dto.to().isBefore(dto.from())) {
                throw new IllegalArgumentException("The to-date cannot be before the from-date.");
            }

            boolean hasConflict =
                    !bookingRepo
                            .findConflictingBookings(bike.getId(), -1L, dto.from(), dto.to())
                            .isEmpty();

            if (hasConflict) {
                throw new IllegalStateException("Bike is not available for the selected dates");
            }

            var priceTotal = bookingPriceCalculator.calculateTotalPrice(dto.from(), dto.to(), bike);

            var booking = BookingMapper.fromBookingDtoCreate(dto, bike, customer, priceTotal);

            bikeRepo.save(bike);
            booking.setStatus(BookingStatus.ACTIVE);
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
        if (!userService.hasAccess(customerId)) {
            throw new AccessDeniedException("You cannot view other customers' bookings");
        }

        return bookingRepo.findAllByCustomerId(customerId).stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Transactional
    public BookingDto updateBooking(Long id, BookingUpdateDto dto) {
        try {
            log.info("Updating a booking. id={}, payload={} ", id, dto);

            var newCustomer =
                    customerRepo
                            .findById(dto.customerId())
                            .orElseThrow(() -> new CustomerNotFoundException(dto.customerId()));

            var newBike =
                    bikeRepo.findById(dto.bikeId())
                            .orElseThrow(() -> new BikeNotFoundException(dto.bikeId()));

            var booking =
                    bookingRepo.findById(id).orElseThrow(() -> new BookingNotFoundException(id));

            LocalDate newFrom = dto.from();
            LocalDate newTo = dto.to();

            if (newTo.isBefore(newFrom)) {
                throw new IllegalArgumentException("The to-date cannot be before the from-date.");
            }

            boolean hasConflict =
                    !bookingRepo
                            .findConflictingBookings(
                                    newBike.getId(), booking.getId(), newFrom, newTo)
                            .isEmpty();

            if (hasConflict) {
                throw new IllegalStateException("Bike is not available for the selected dates");
            }

            booking.setBike(newBike);
            booking.setCustomer(newCustomer);
            booking.setFromDate(newFrom);
            booking.setToDate(newTo);
            booking.setPriceTotalSek(
                    bookingPriceCalculator.calculateTotalPrice(newFrom, newTo, newBike));
            booking = bookingRepo.save(booking);

            log.info("Successfully updated a booking. id={}, payload={} ", id, dto);

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

    @Transactional
    public BookingDto patchBooking(Long bookingId, BookingPatchDto dto) {
        try {
            log.info("Patching booking. id={}", bookingId);

            var booking =
                    bookingRepo
                            .findById(bookingId)
                            .orElseThrow(() -> new BookingNotFoundException(bookingId));

            boolean isAdmin = userService.isAdmin();

            if (!isAdmin && !userService.hasAccess(booking.getCustomer().getId())) {
                throw new AccessDeniedException("You cannot change other customers' bookings");
            }

            // Only admin can update the status of the booking.
            // Admins should not be able to change anything else.
            if (isAdmin) {
                if (dto.status() != null) {
                    booking.setStatus(dto.status());
                    booking = bookingRepo.save(booking);
                }

                return BookingMapper.toBookingDto(booking);
            }

            var newBike = booking.getBike();

            if (dto.bikeId() != null) {
                newBike =
                        bikeRepo.findById(dto.bikeId())
                                .orElseThrow(() -> new BikeNotFoundException(dto.bikeId()));
            }

            LocalDate newFrom = dto.from() != null ? dto.from() : booking.getFromDate();
            LocalDate newTo = dto.to() != null ? dto.to() : booking.getToDate();

            if (newTo.isBefore(newFrom)) {
                throw new IllegalArgumentException("The to-date cannot be before the from-date.");
            }

            boolean hasConflict =
                    !bookingRepo
                            .findConflictingBookings(
                                    newBike.getId(), booking.getId(), newFrom, newTo)
                            .isEmpty();

            if (hasConflict) {
                throw new IllegalStateException("Bike is not available for the selected dates");
            }

            booking.setBike(newBike);
            booking.setFromDate(newFrom);
            booking.setToDate(newTo);
            booking.setPriceTotalSek(
                    bookingPriceCalculator.calculateTotalPrice(newFrom, newTo, newBike));
            booking = bookingRepo.save(booking);

            log.info("Successfully patched booking. id={}", bookingId);

            return BookingMapper.toBookingDto(booking);
        } catch (Exception e) {
            log.error("Failed to patch booking. id={}, error={}", bookingId, e.getMessage());

            throw e;
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            log.info("Deleting booking. id={}", id);

            if (!bookingRepo.existsById(id)) {
                throw new BookingNotFoundException(id);
            }

            bookingRepo.deleteById(id);

            log.info("Booking successfully deleted. id={}", id);
        } catch (Exception e) {
            log.error("Failed to delete booking. payload={}", id);
            throw e;
        }
    }
}
