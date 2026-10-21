package com.williameliasson.car_auction_backend.dtos;

public record BidDisplay(
    Integer auctionId,
    Integer bidId,
    // todo: bidder pseudonym string. e.g. "Bidder 1"
    Integer amount
) {
}
