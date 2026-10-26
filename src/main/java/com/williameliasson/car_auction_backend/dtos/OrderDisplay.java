package com.williameliasson.car_auction_backend.dtos;

import java.time.Instant;

public record OrderDisplay(
    Integer id,
    String username,
    Integer amountSek,
    Instant createdAt,
    boolean isPaid
) {
} 