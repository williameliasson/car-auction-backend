package com.williameliasson.car_auction_backend.dtos;

public record BidDisplay(
    Integer auctionId,
    Integer bidId,
    String pseudonym, //Thinking of doing a hash of userId and auctionId to get a consistent anonymous userId that we can send to the client
    Integer amount
) {
}
