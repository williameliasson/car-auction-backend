package com.williameliasson.car_auction_backend.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderCreateRequest(
    @Positive @NotNull Integer amountSek
) {
}
