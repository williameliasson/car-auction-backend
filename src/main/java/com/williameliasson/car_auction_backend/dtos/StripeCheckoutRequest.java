package com.williameliasson.car_auction_backend.dtos;

public record StripeCheckoutRequest(
    Integer orderId
) {
}
