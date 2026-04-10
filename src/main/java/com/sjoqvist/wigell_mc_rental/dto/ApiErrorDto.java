package com.sjoqvist.wigell_mc_rental.dto;

import java.time.LocalDateTime;

public record ApiErrorDto(
        String message, LocalDateTime timestamp, String error, String path, int status) {}
