package com.missinglink.backend.module.vehicle;


import com.missinglink.backend.common.response.ApiResponse;
import com.missinglink.backend.module.vehicle.dto.VehicleRequest;
import com.missinglink.backend.module.vehicle.dto.VehicleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleServices vehicleService;

    // POST /api/vehicles — add a vehicle
    @PostMapping
    public ResponseEntity<ApiResponse<VehicleResponse>> addVehicle(
            @Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.addVehicle(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Vehicle added successfully"));
    }

    // GET /api/vehicles/my-vehicles — get all my vehicles
    @GetMapping("/my-vehicles")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getMyVehicles() {
        List<VehicleResponse> vehicles = vehicleService.getMyVehicles();
        return ResponseEntity.ok(
                ApiResponse.success(vehicles, "Vehicles fetched successfully"));
    }

    // PUT /api/vehicles/{id} — update a vehicle
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(
            @PathVariable UUID id,
            @Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.updateVehicle(id, request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Vehicle updated successfully"));
    }

    // DELETE /api/vehicles/{id} — delete a vehicle
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteVehicle(
            @PathVariable UUID id) {
        String message = vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(
                ApiResponse.success(message, "Vehicle deleted successfully"));
    }
}