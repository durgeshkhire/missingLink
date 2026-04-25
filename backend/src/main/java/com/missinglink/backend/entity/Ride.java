package com.missinglink.backend.entity;


import com.missinglink.backend.entity.enums.RideStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rides")
@Builder
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @Column(nullable = false)
    private String originCity;

    @Column(nullable = false)
    private String destinationCity;

    private String originAddress;
    private String destinationAddress;

    private Double originLat;
    private Double originLng;

    private Double destinationLat;
    private Double destinationLng;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private int totalSeats;

    @Builder.Default
    private int availableSeats = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerSeat;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RideStatus status = RideStatus.UPCOMING;

    private String description;

    private String carName;
    private String carNumber;

    @Builder.Default
    private boolean instantBooking = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        availableSeats = totalSeats;
    }
}
