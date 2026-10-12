package com.williameliasson.car_auction_backend;

import org.springframework.boot.SpringApplication;

public class TestCarAuctionBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(CarAuctionBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
