package com.missinglink.backend.module.ride;

import com.missinglink.backend.common.response.ApiResponse;
import com.missinglink.backend.module.ride.dto.RideRequest;
import com.missinglink.backend.module.ride.dto.RideResponse;
import com.missinglink.backend.module.ride.dto.RideSearchRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RideController {

    private final RideService rideService;

    @PostMapping
    public ResponseEntity<ApiResponse<RideResponse>> createRide(
            @Valid @RequestBody RideRequest request) {
        RideResponse response = rideService.createRide(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Ride created successfully"));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<RideResponse>>> getAllUpcomingRides() {
        List<RideResponse> rides = rideService.getAllUpcomingRides();
        return ResponseEntity.ok(
                ApiResponse.success(rides, "Upcoming rides fetched successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<RideResponse>>> searchRides(
            @ModelAttribute RideSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RideResponse> rides = rideService.searchRides(request, page, size);
        return ResponseEntity.ok(
                ApiResponse.success(rides, "Rides fetched successfully"));
    }

    @GetMapping("/my-rides")
    public ResponseEntity<ApiResponse<List<RideResponse>>> getMyRides() {
        List<RideResponse> rides = rideService.getMyRides();
        return ResponseEntity.ok(
                ApiResponse.success(rides, "Your rides fetched successfully"));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RideResponse>> getRideById(
            @PathVariable UUID id) {
        RideResponse response = rideService.getRideById(id);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Ride fetched successfully"));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<RideResponse>> cancelRide(
            @PathVariable UUID id) {
        RideResponse response = rideService.cancelRide(id);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Ride cancelled successfully"));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<RideResponse>> completeRide(
            @PathVariable UUID id) {
        RideResponse response = rideService.completeRide(id);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Ride marked as completed"));
    }

}
