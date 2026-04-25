package com.missinglink.backend.module.booking;

import com.missinglink.backend.entity.Booking;
import com.missinglink.backend.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByRideId(UUID rideId);

    // All bookings made by a rider
    List<Booking> findByRiderIdOrderByRequestedAtDesc(UUID riderId);

    // Check if rider already booked this ride
    Optional<Booking> findByRideIdAndRiderId(UUID rideId, UUID riderId);

    // Count approved bookings for a ride
    int countByRideIdAndStatus(UUID rideId, BookingStatus status);
}
