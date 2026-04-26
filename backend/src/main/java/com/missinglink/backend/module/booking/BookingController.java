package com.missinglink.backend.module.booking;

import com.missinglink.backend.common.response.ApiResponse;
import com.missinglink.backend.module.booking.dto.BookingRequest;
import com.missinglink.backend.module.booking.dto.BookingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> requestBooking(
            @Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.requestBooking(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Booking request sent successfully"));
    }

    // PATCH /api/bookings/{id}/approve — driver approves
    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<BookingResponse>> approveBooking(
            @PathVariable UUID id) {
        BookingResponse response = bookingService.approveBooking(id);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Booking approved successfully"));
    }

    // PATCH /api/bookings/{id}/reject — driver rejects
    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<BookingResponse>> rejectBooking(
            @PathVariable UUID id) {
        BookingResponse response = bookingService.rejectBooking(id);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Booking rejected"));
    }

    // PATCH /api/bookings/{id}/cancel — rider cancels
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @PathVariable UUID id) {
        BookingResponse response = bookingService.cancelBooking(id);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Booking cancelled successfully"));
    }

    // GET /api/v1/bookings/ride/{rideId} — driver sees all requests for a ride
    @GetMapping("/ride/{rideId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsForRide(
            @PathVariable UUID rideId) {
        List<BookingResponse> bookings = bookingService.getBookingsForRide(rideId);
        return ResponseEntity.ok(
                ApiResponse.success(bookings, "Bookings fetched successfully"));
    }

    // GET /api/bookings/my-bookings — rider sees their own bookings
    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings() {
        List<BookingResponse> bookings = bookingService.getMyBookings();
        return ResponseEntity.ok(
                ApiResponse.success(bookings, "Your bookings fetched successfully"));
    }

}
