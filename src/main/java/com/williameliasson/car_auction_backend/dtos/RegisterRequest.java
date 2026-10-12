package com.williameliasson.car_auction_backend.dtos;

public record RegisterRequest(
    String username,
    String password,
    String email
){   
}
