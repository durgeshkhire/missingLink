package com.missinglink.backend.module.ride.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
public class RideSearchRequest {

    private String originCity;
    private String destinationCity;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departureDate;

    private int seats = 1;
}
