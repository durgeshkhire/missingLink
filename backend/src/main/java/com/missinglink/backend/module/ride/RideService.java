package com.missinglink.backend.module.ride;

import com.missinglink.backend.entity.Ride;
import com.missinglink.backend.entity.User;
import com.missinglink.backend.entity.Vehicle;
import com.missinglink.backend.entity.enums.RideStatus;
import com.missinglink.backend.module.ride.dto.RideRequest;
import com.missinglink.backend.module.ride.dto.RideResponse;
import com.missinglink.backend.module.ride.dto.RideSearchRequest;
import com.missinglink.backend.module.user.UserRepository;
import com.missinglink.backend.module.vehicle.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    // Get currently logged-in user
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Post a ride
    public RideResponse createRide(RideRequest request) {
        User driver = getCurrentUser();

        Vehicle vehicle = vehicleRepository
                .findByIdAndUserId(request.getVehicleId(), driver.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Vehicle not found or does not belong to you"));

        if (request.getTotalSeats() > vehicle.getTotalSeats()) {
            throw new RuntimeException(
                    "Available seats cannot exceed vehicle capacity of "
                            + vehicle.getTotalSeats());
        }


        Ride ride = Ride.builder()
                .driver(driver)
                .vehicle(vehicle)
                .originCity(request.getOriginCity())
                .destinationCity(request.getDestinationCity())
                .originAddress(request.getOriginAddress())
                .destinationAddress(request.getDestinationAddress())
                .originLat(request.getOriginLat())
                .originLng(request.getOriginLng())
                .destinationLat(request.getDestinationLat())
                .destinationLng(request.getDestinationLng())
                .departureTime(request.getDepartureTime())
                .totalSeats(request.getTotalSeats())
                .pricePerSeat(request.getPricePerSeat())
                .description(request.getDescription())
//                .carName(request.getCarName())
//                .carNumber(request.getCarNumber())
                .instantBooking(request.isInstantBooking())
                .build();

        rideRepository.save(ride);
        return mapToResponse(ride);
    }

    // Search rides
    public Page<RideResponse> searchRides(RideSearchRequest request,
                                          int page, int size) {
        LocalDate date = request.getDepartureDate() != null
                ? request.getDepartureDate()
                : LocalDate.now();

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        Pageable pageable = PageRequest.of(page, size);

        return rideRepository.searchRides(
                request.getOriginCity(),
                request.getDestinationCity(),
                startOfDay,
                endOfDay,
                request.getSeats(),
                RideStatus.UPCOMING,
                pageable
        ).map(this::mapToResponse);
    }

    // Get ride by ID
    public RideResponse getRideById(UUID id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        return mapToResponse(ride);
    }

    // Get my posted rides
    public List<RideResponse> getMyRides() {
        User user = getCurrentUser();
        return rideRepository
                .findByDriverIdOrderByDepartureTimeDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Cancel a ride
    public RideResponse cancelRide(UUID rideId) {
        User user = getCurrentUser();
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (!ride.getDriver().getId().equals(user.getId())) {
            throw new RuntimeException("You can only cancel your own rides");
        }

        ride.setStatus(RideStatus.CANCELLED);
        rideRepository.save(ride);
        return mapToResponse(ride);
    }

    public Page<RideResponse> getAllUpcomingRides(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return rideRepository
                .findByStatusOrderByDepartureTimeAsc(RideStatus.UPCOMING, pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public RideResponse completeRide(UUID rideId) {
        User user = getCurrentUser();
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (!ride.getDriver().getId().equals(user.getId())) {
            throw new RuntimeException("Only the driver can complete the ride");
        }

        if (ride.getStatus() != RideStatus.ONGOING &&
                ride.getStatus() != RideStatus.UPCOMING) {
            throw new RuntimeException("Ride cannot be marked as completed");
        }

        ride.setStatus(RideStatus.COMPLETED);
        rideRepository.save(ride);
        return mapToResponse(ride);
    }

    @Transactional
    public RideResponse startRide(UUID rideId) {
        User user = getCurrentUser();
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (!ride.getDriver().getId().equals(user.getId())) {
            throw new RuntimeException("Only the driver can start the ride");
        }

        if (ride.getStatus() != RideStatus.UPCOMING) {
            throw new RuntimeException("Ride cannot be started");
        }

        ride.setStatus(RideStatus.ONGOING);
        rideRepository.save(ride);
        return mapToResponse(ride);
    }

    // Map entity to response DTO
    private RideResponse mapToResponse(Ride ride) {
        return RideResponse.builder()
                .id(ride.getId())
                .driverId(ride.getDriver().getId())
                .driverName(ride.getDriver().getName())
                .driverAvatarUrl(ride.getDriver().getAvatarUrl())
                .driverRating(ride.getDriver().getAverageRating())
                .vehicleId(ride.getVehicle().getId())
                .carName(ride.getVehicle().getCarName())
                .carNumber(ride.getVehicle().getCarNumber())
                .carType(ride.getVehicle().getCarType())
                .vehicleTotalSeats(ride.getVehicle().getTotalSeats())
                .originCity(ride.getOriginCity())
                .destinationCity(ride.getDestinationCity())
                .originAddress(ride.getOriginAddress())
                .destinationAddress(ride.getDestinationAddress())
                .originLat(ride.getOriginLat())
                .originLng(ride.getOriginLng())
                .destinationLat(ride.getDestinationLat())
                .destinationLng(ride.getDestinationLng())
                .departureTime(ride.getDepartureTime())
                .totalSeats(ride.getTotalSeats())
                .availableSeats(ride.getAvailableSeats())
                .pricePerSeat(ride.getPricePerSeat())
                .status(ride.getStatus().name())
                .description(ride.getDescription())
                .instantBooking(ride.isInstantBooking())
                .createdAt(ride.getCreatedAt())
                .build();
    }
}
