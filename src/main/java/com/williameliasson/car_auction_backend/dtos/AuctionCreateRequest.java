package com.williameliasson.car_auction_backend.dtos;

import java.time.Instant;

public record AuctionCreateRequest(
    String title,
    String description,
    String plate,
    Instant deadline,
    Integer make_id,
    Integer modelYear,
    Integer odometerKm
) {
}
