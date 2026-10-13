package com.williameliasson.car_auction_backend.configs;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.williameliasson.car_auction_backend.models.CarMake;
import com.williameliasson.car_auction_backend.repositories.CarMakeRepository;

// src: https://www.geeksforgeeks.org/advance-java/spring-boot-load-initial-data/
@Component 
public class DataLoader implements CommandLineRunner{

    private final CarMakeRepository carMakeRepository;

    DataLoader(CarMakeRepository carMakeRepository){
        this.carMakeRepository = carMakeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // If there are 0 car makes in the db, add some default ones
        
        List<String> carMakes = List.of(
            // The following list was generated using AI
            "Volvo",
            "Volkswagen",
            "Porsche",
            "BMW",
            "Audi",
            "Mercedes-Benz",
            "Škoda",
            "SEAT",
            "Cupra",
            "Opel",
            "Ford",
            "Toyota",
            "Honda",
            "Mazda",
            "Nissan",
            "Hyundai",
            "Kia",
            "Peugeot",
            "Citroën",
            "Renault",
            "Fiat",
            "Alfa Romeo",
            "Ferrari",
            "Lamborghini",
            "Maserati",
            "Bentley",
            "Rolls-Royce",
            "Jaguar",
            "Land Rover",
            "MINI",
            "Tesla",
            "Polestar",
            "BYD",
            "MG",
            "Subaru",
            "Mitsubishi",
            "Suzuki",
            "Lexus",
            "Infiniti",
            "Genesis",
            "Jeep",
            "Dodge",
            "Chrysler",
            "Chevrolet",
            "Cadillac",
            "GMC",
            "RAM",
            "Lucid",
            "Rivian",
            "Smart",
            "Dacia",
            "Lancia",
            "DS Automobiles",
            "Alpine",
            "Saab",
            "Lynk & Co",
            "Zeekr",
            "NIO",
            "XPeng",
            "Hongqi",
            "Great Wall",
            "Chery",
            "Geely",
            "Leapmotor",
            "Omoda",
            "Jaecoo"
            // End of AI-generated list
        );
        if (carMakeRepository.count() == 0){
            carMakes.forEach(cm -> {
                CarMake carMake = new CarMake();
                carMake.setName(cm);
                carMakeRepository.save(carMake);
            });
        }
    }
    
}
