package com.missinglink.backend.module.booking;

import com.missinglink.backend.entity.Booking;
import com.missinglink.backend.entity.Ride;
import com.missinglink.backend.entity.User;
import com.missinglink.backend.entity.enums.BookingStatus;
import com.missinglink.backend.entity.enums.RideStatus;
import com.missinglink.backend.module.booking.dto.BookingRequest;
import com.missinglink.backend.module.booking.dto.BookingResponse;
import com.missinglink.backend.module.ride.RideRepository;
import com.missinglink.backend.module.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    // Get currently logged-in user
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Request to join a ride
    @Transactional
    public BookingResponse requestBooking(BookingRequest request) {
        User rider = getCurrentUser();

        Ride ride = rideRepository.findById(request.getRideId())
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        // Cannot book your own ride
        if (ride.getDriver().getId().equals(rider.getId())) {
            throw new RuntimeException("You cannot book your own ride");
        }

        // Ride must be upcoming
        if (ride.getStatus() != RideStatus.UPCOMING) {
            throw new RuntimeException("This ride is no longer available");
        }

        // Check if already booked
        bookingRepository.findByRideIdAndRiderId(ride.getId(), rider.getId())
                .ifPresent(b -> {
                    throw new RuntimeException("You have already requested this ride");
                });

        // Check seat availability
        if (ride.getAvailableSeats() < request.getSeatsRequested()) {
            throw new RuntimeException("Not enough seats available");
        }

        Booking booking = Booking.builder()
                .ride(ride)
                .rider(rider)
                .seatsRequested(request.getSeatsRequested())
                .pickupNote(request.getPickupNote())
                .build();

        // If instant booking — auto approve and deduct seats
        if (ride.isInstantBooking()) {
            booking.setStatus(BookingStatus.APPROVED);
            booking.setRespondedAt(LocalDateTime.now());
            ride.setAvailableSeats(ride.getAvailableSeats() - request.getSeatsRequested());
            rideRepository.save(ride);
        }

        bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    // Approve a booking (only driver of that ride)
    @Transactional
    public BookingResponse approveBooking(UUID bookingId) {
        User currentUser = getCurrentUser();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Only the driver of the ride can approve
        if (!booking.getRide().getDriver().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only the driver can approve bookings");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is already " + booking.getStatus());
        }

        // Check seats still available
        Ride ride = booking.getRide();
        if (ride.getAvailableSeats() < booking.getSeatsRequested()) {
            throw new RuntimeException("Not enough seats available");
        }

        // Deduct seats
        ride.setAvailableSeats(ride.getAvailableSeats() - booking.getSeatsRequested());
        rideRepository.save(ride);

        booking.setStatus(BookingStatus.APPROVED);
        booking.setRespondedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        return mapToResponse(booking);
    }

    // Reject a booking (only driver of that ride)
    @Transactional
    public BookingResponse rejectBooking(UUID bookingId) {
        User currentUser = getCurrentUser();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getRide().getDriver().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only the driver can reject bookings");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is already " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.REJECTED);
        booking.setRespondedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        return mapToResponse(booking);
    }

    // Cancel a booking (only the rider who made it)
    @Transactional
    public BookingResponse cancelBooking(UUID bookingId) {
        User currentUser = getCurrentUser();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getRider().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only cancel your own bookings");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        // Restore seats if booking was approved
        if (booking.getStatus() == BookingStatus.APPROVED) {
            Ride ride = booking.getRide();
            ride.setAvailableSeats(ride.getAvailableSeats() + booking.getSeatsRequested());
            rideRepository.save(ride);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setRespondedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        return mapToResponse(booking);
    }

    // Get all bookings for a ride (driver views requests)
    public List<BookingResponse> getBookingsForRide(UUID rideId) {
        User currentUser = getCurrentUser();

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (!ride.getDriver().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only the driver can view ride bookings");
        }

        return bookingRepository.findByRideId(rideId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get my bookings (rider views their requests)
    public List<BookingResponse> getMyBookings() {
        User currentUser = getCurrentUser();
        return bookingRepository
                .findByRiderIdOrderByRequestedAtDesc(currentUser.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Map entity to response DTO
    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .rideId(booking.getRide().getId())
                .originCity(booking.getRide().getOriginCity())
                .destinationCity(booking.getRide().getDestinationCity())
                .departureTime(booking.getRide().getDepartureTime())
                .carName(booking.getRide().getCarName())
                .carNumber(booking.getRide().getCarNumber())
                .riderId(booking.getRider().getId())
                .riderName(booking.getRider().getName())
                .riderEmail(booking.getRider().getEmail())
                .seatsRequested(booking.getSeatsRequested())
                .status(booking.getStatus().name())
                .pickupNote(booking.getPickupNote())
                .requestedAt(booking.getRequestedAt())
                .respondedAt(booking.getRespondedAt())
                .build();
    }
}
