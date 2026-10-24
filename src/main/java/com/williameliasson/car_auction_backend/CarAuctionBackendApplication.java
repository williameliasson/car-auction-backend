package com.williameliasson.car_auction_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
public class CarAuctionBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarAuctionBackendApplication.class, args);
	}

}
