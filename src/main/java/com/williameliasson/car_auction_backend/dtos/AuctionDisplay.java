package com.williameliasson.car_auction_backend.dtos;

import java.time.Instant;
import java.util.List;

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
    Integer valuationMin,
    Integer valuationMax,
    boolean requestingUserIsOwner,
    List<BidDisplay> bids
) {
}
