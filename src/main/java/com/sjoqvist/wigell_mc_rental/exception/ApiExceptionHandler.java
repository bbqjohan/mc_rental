package com.sjoqvist.wigell_mc_rental.exception;

import com.sjoqvist.wigell_mc_rental.dto.ApiErrorDto;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                        "Not found",
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
                        "Not found",
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
                        "Conflict",
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
                        "Conflict",
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
                        "Not found",
                        req.getRequestURI(),
                        HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND);
    }
}
