package com.missinglink.backend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationMessage {
    private String rideId;
    private Double lat;
    private Double lng;
    private String driverName;
    private String timestamp;
}
