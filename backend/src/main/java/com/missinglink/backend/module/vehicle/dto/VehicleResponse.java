package com.missinglink.backend.module.vehicle.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class VehicleResponse {

    private UUID id;
    private UUID userId;
    private String carName;
    private String carNumber;
    private String carType;
    private int totalSeats;
    private LocalDateTime createdAt;
}