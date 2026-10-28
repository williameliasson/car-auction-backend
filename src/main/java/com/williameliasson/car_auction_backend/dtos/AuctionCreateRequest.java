package com.williameliasson.car_auction_backend.dtos;

import java.time.Instant;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

public record AuctionCreateRequest(
    @NotBlank String title,
    String description,
    String plate,
    @Future Instant deadline,
    Integer make_id,
    Integer modelYear,
    Integer odometerKm
) {
}
