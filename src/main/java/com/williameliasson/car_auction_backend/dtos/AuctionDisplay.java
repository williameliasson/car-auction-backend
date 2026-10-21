package com.williameliasson.car_auction_backend.dtos;

import java.time.Instant;

public record AuctionDisplay(
    Integer id,
    String title,
    String description,
    String plate,
    Instant deadline,
    // Integer make_id,
    String makeName,
    Integer modelYear,
    Integer odometerKm,
    // bids list?
    Integer valuationMin,
    Integer valuationMax
) {
}
