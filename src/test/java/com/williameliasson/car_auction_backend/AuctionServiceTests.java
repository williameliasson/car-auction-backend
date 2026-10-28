package com.williameliasson.car_auction_backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.williameliasson.car_auction_backend.dtos.AuctionCreateRequest;
import com.williameliasson.car_auction_backend.dtos.ValuationResult;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.CarMake;
import com.williameliasson.car_auction_backend.models.User;
import com.williameliasson.car_auction_backend.repositories.AuctionRepository;
import com.williameliasson.car_auction_backend.repositories.UserRepository;
import com.williameliasson.car_auction_backend.services.AuctionService;
import com.williameliasson.car_auction_backend.services.BidService;
import com.williameliasson.car_auction_backend.services.CarMakeService;
import com.williameliasson.car_auction_backend.services.OpenAIService;

@ExtendWith(MockitoExtension.class)
public class AuctionServiceTests {
    @Mock 
    UserRepository userRepository;

    @Mock
    BidService bidService;

    @Mock 
    OpenAIService openAIService;

    @Mock 
    CarMakeService carMakeService;

    @Mock 
    AuctionRepository auctionRepository;
    
    @InjectMocks  
    AuctionService auctionService;

    @Test 
    void shouldCreateAuctionWithValidRequest(){
        // Arrange
        // variables
        
        CarMake carMake = new CarMake();
        User user = new User();
        Instant tomorrow = Instant.now().plusSeconds(86400L);

        when(carMakeService.getCarMakeById(any())).thenReturn(carMake);
        when(openAIService.getValuation(any())).thenReturn(new ValuationResult(0, 100_000));

        AuctionCreateRequest auctionCreateRequest = new AuctionCreateRequest(
            "Some title",
            "Description", 
            "ABC123", 
            tomorrow, 
            1, 
            2000, 
            10_000
        );

        // Act
        Auction createdAuction = auctionService.createAuction(user, auctionCreateRequest);
        
        // Assert
        assertNotNull(createdAuction);
        assertEquals(auctionCreateRequest.title(), createdAuction.getTitle());
        assertEquals(auctionCreateRequest.description(), createdAuction.getDescription());
        assertEquals(user, createdAuction.getOwner());
        
    }

    @Test 
    void shouldNotCreateAuctionWithDeadlineBeforeNow(){
        // Arrange
        // variables
        
        // CarMake carMake = new CarMake();
        User user = new User();
        Instant yesterday = Instant.now().minusSeconds(86400L);

        // mocked service calls. Not needed because the validity check (including the deadline before now check) happens before calling any other services
        // when(carMakeService.getCarMakeById(any())).thenReturn(carMake);
        // when(openAIService.getValuation(any())).thenReturn(new ValuationResult(0, 100_000));

        AuctionCreateRequest auctionCreateRequest = new AuctionCreateRequest(
            "Some title",
            "Description", 
            "ABC123", 
            yesterday, 
            1, 
            2000, 
            10_000
        );

        // Act
        // Assert
        
        assertThrows(IllegalArgumentException.class, () -> {
            auctionService.createAuction(user, auctionCreateRequest);
        });
    }
    
}
