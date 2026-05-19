package com.missinglink.backend.module.vehicle;

import com.missinglink.backend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    // Get all vehicles of a user
    List<Vehicle> findByUserIdOrderByCreatedAtDesc(UUID userId);

    // Check if car number already exists
    boolean existsByCarNumber(String carNumber);

    // Get vehicle by id and user id (ownership check)
    Optional<Vehicle> findByIdAndUserId(UUID id, UUID userId);

}
