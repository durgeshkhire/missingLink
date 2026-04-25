package com.missinglink.backend.module.ride;

import com.missinglink.backend.entity.Ride;
import com.missinglink.backend.entity.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RideRepository extends JpaRepository<Ride, UUID> {
    @Query("""
        SELECT r FROM Ride r
        WHERE LOWER(r.originCity) = LOWER(:origin)
        AND LOWER(r.destinationCity) = LOWER(:destination)
        AND r.departureTime >= :startOfDay
        AND r.departureTime < :endOfDay
        AND r.availableSeats >= :seats
        AND r.status = :status
        ORDER BY r.departureTime ASC
    """)
    Page<Ride> searchRides(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("seats") int seats,
            @Param("status") RideStatus status,
            Pageable pageable
    );

    // Get all rides posted by a driver
    List<Ride> findByDriverIdOrderByDepartureTimeDesc(UUID driverId);

    // Get upcoming rides for a driver
    List<Ride> findByDriverIdAndStatus(UUID driverId, RideStatus status);

    List<Ride> findByStatusOrderByDepartureTimeAsc(RideStatus status);
}
