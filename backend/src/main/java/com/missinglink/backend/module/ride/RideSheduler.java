package com.missinglink.backend.module.ride;

import com.missinglink.backend.entity.Ride;
import com.missinglink.backend.entity.enums.BookingStatus;
import com.missinglink.backend.entity.enums.RideStatus;
import com.missinglink.backend.module.booking.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RideSheduler {

    private final RideRepository rideRepository;
    private final BookingRepository bookingRepository;

    // Runs every 10 minutes
    @Scheduled(fixedRate = 600000)
    @Transactional
    public void cancelExpiredRides() {
        LocalDateTime now = LocalDateTime.now();

        List<Ride> expiredRides = rideRepository.findExpiredRides(
                RideStatus.UPCOMING, now);

        if (expiredRides.isEmpty()) {
            return;
        }

        log.info("Found {} expired rides — auto cancelling", expiredRides.size());

        for (Ride ride : expiredRides) {
            // Cancel the ride
            ride.setStatus(RideStatus.CANCELLED);
            rideRepository.save(ride);

            // Also cancel all pending bookings for this ride
            bookingRepository.findByRideId(ride.getId())
                    .stream()
                    .filter(b -> b.getStatus() == BookingStatus.PENDING)
                    .forEach(b -> {
                        b.setStatus(BookingStatus.CANCELLED);
                        bookingRepository.save(b);
                    });

            log.info("Auto cancelled expired ride: {} ({} → {})",
                    ride.getId(),
                    ride.getOriginCity(),
                    ride.getDestinationCity());
        }
    }

}
