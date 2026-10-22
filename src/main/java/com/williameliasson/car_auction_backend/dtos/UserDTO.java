package com.williameliasson.car_auction_backend.dtos;

public record UserDTO(String username, String email, Integer balance, Integer availableBalance) {
}
