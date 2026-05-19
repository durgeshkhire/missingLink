package com.missinglink.backend.module.location;

import com.missinglink.backend.common.LocationMessage;
import com.missinglink.backend.entity.Ride;
import com.missinglink.backend.entity.enums.RideStatus;
import com.missinglink.backend.module.ride.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class LocationController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RideRepository rideRepository;

    // Driver sends location → server broadcasts to all passengers
    @MessageMapping("/location/{rideId}")
    public void updateLocation(@DestinationVariable String rideId,
                               @Payload LocationMessage message) {

        Ride ride = rideRepository.findById(UUID.fromString(rideId))
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        // Only broadcast if ride is ongoing
        if (ride.getStatus() != RideStatus.ONGOING) {
            return;
        }

        message.setRideId(rideId);
        message.setTimestamp(String.valueOf(System.currentTimeMillis()));

        // Broadcast to all passengers subscribed to this ride
        messagingTemplate.convertAndSend(
                "/topic/ride/" + rideId + "/location", message);
    }

}
