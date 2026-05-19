package com.missinglink.backend.module.vehicle;

import com.missinglink.backend.entity.User;
import com.missinglink.backend.entity.Vehicle;
import com.missinglink.backend.entity.enums.RideStatus;
import com.missinglink.backend.module.ride.RideRepository;
import com.missinglink.backend.module.user.UserRepository;
import com.missinglink.backend.module.vehicle.dto.VehicleRequest;
import com.missinglink.backend.module.vehicle.dto.VehicleResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServices {


    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final RideRepository rideRepository;


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Add a new vehicle
    @Transactional
    public VehicleResponse addVehicle(VehicleRequest request) {
        User user = getCurrentUser();

        if (vehicleRepository.existsByCarNumber(request.getCarNumber())) {
            throw new RuntimeException("Vehicle with this number already exists");
        }

        Vehicle vehicle = Vehicle.builder()
                .user(user)
                .carName(request.getCarName())
                .carNumber(request.getCarNumber())
                .carType(request.getCarType())
                .totalSeats(request.getTotalSeats())
                .build();

        vehicleRepository.save(vehicle);
        return mapToResponse(vehicle);
    }

    // Get all my vehicles
    public List<VehicleResponse> getMyVehicles() {
        User user = getCurrentUser();
        return vehicleRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update a vehicle
    @Transactional
    public VehicleResponse updateVehicle(UUID vehicleId, VehicleRequest request) {
        User user = getCurrentUser();

        Vehicle vehicle = vehicleRepository.findByIdAndUserId(vehicleId, user.getId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        vehicle.setCarName(request.getCarName());
        vehicle.setCarNumber(request.getCarNumber());
        vehicle.setCarType(request.getCarType());
        vehicle.setTotalSeats(request.getTotalSeats());


        vehicleRepository.save(vehicle);
        return mapToResponse(vehicle);
    }

    // Delete a vehicle
    @Transactional
    public String deleteVehicle(UUID vehicleId) {
        User user = getCurrentUser();

        Vehicle vehicle = vehicleRepository.findByIdAndUserId(vehicleId, user.getId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        boolean hasActiveRides = rideRepository
                .existsByVehicleIdAndStatusIn(
                        vehicleId,
                        List.of(RideStatus.UPCOMING, RideStatus.ONGOING)
                );

        if (hasActiveRides) {
            throw new RuntimeException(
                    "Cannot delete vehicle — it has active or upcoming rides. " +
                            "Cancel those rides first before deleting this vehicle.");
        }

        vehicleRepository.delete(vehicle);
        return "Vehicle deleted successfully";
    }

    // Map entity to response
    public VehicleResponse mapToResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .userId(vehicle.getUser().getId())
                .carName(vehicle.getCarName())
                .carNumber(vehicle.getCarNumber())
                .carType(vehicle.getCarType())
                .totalSeats(vehicle.getTotalSeats())
                .createdAt(vehicle.getCreatedAt())
                .build();
    }


}
