package com.sjoqvist.wigell_mc_rental.exception;

import com.sjoqvist.wigell_mc_rental.dto.ApiErrorDto;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = CustomerNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleCustomerNotFoundException(
            CustomerNotFoundException e, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiErrorDto(
                        e.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        req.getRequestURI(),
                        HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AddressNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleAddressNotFoundException(
            AddressNotFoundException e, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiErrorDto(
                        e.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        req.getRequestURI(),
                        HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UserExistsException.class)
    public ResponseEntity<ApiErrorDto> handleUserExistsException(
            UserExistsException e, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiErrorDto(
                        e.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        req.getRequestURI(),
                        HttpStatus.CONFLICT.value()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BikeExistsException.class)
    public ResponseEntity<ApiErrorDto> handleBikeExistsException(
            BikeExistsException e, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiErrorDto(
                        e.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        req.getRequestURI(),
                        HttpStatus.CONFLICT.value()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BikeNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleBikeNotFoundException(
            BikeNotFoundException e, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiErrorDto(
                        e.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        req.getRequestURI(),
                        HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BikeNotAvailableException.class)
    public ResponseEntity<ApiErrorDto> handleBikeNotAvailableException(
            BikeNotAvailableException e, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiErrorDto(
                        e.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        req.getRequestURI(),
                        HttpStatus.CONFLICT.value()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BookingNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleBookingNotFoundException(
            BookingNotFoundException e, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiErrorDto(
                        e.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        req.getRequestURI(),
                        HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidBookingDateException.class)
    public ResponseEntity<ApiErrorDto> handleInvalidBookingDateException(
            InvalidBookingDateException e, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiErrorDto(
                        e.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        req.getRequestURI(),
                        HttpStatus.CONFLICT.value()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiErrorDto> handleAccessDeniedException(
            AccessDeniedException e, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiErrorDto(
                        e.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.FORBIDDEN.getReasonPhrase(),
                        req.getRequestURI(),
                        HttpStatus.FORBIDDEN.value()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<ApiErrorDto> handleIllegalStateException(
            IllegalStateException e, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiErrorDto(
                        e.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        req.getRequestURI(),
                        HttpStatus.CONFLICT.value()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ApiErrorDto> handleIllegalArgumentException(
            IllegalArgumentException e, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiErrorDto(
                        e.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        req.getRequestURI(),
                        HttpStatus.CONFLICT.value()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiErrorDto> handleException(Exception e, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiErrorDto(
                        e.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        req.getRequestURI(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
