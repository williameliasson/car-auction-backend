package com.williameliasson.car_auction_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderCreateRequest(
    @NotBlank Integer userId,
    @Positive @NotNull Integer amountSek
) {
}
