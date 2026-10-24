package com.williameliasson.car_auction_backend.mappers;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.williameliasson.car_auction_backend.dtos.BidDisplay;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.Bid;
import com.williameliasson.car_auction_backend.models.User;

@Component 
public class BidMapper {
    

    BidMapper(){
    }

    public BidDisplay toBidDisplay(Bid bid){
        return new BidDisplay(
            bid.getAuction().getId(),
            bid.getId(),
            getBidderHash(bid),
            bid.getAmount()
        );
    }

    public String getBidderHash(Bid bid){
        Auction auction = bid.getAuction();
        User user = bid.getUser();

        return getBidderHash(auction, user);
    }
    
    public String getBidderHash(Auction auction, User user){
        // src: https://www.baeldung.com/java-objects-hash-vs-objects-hashcode
        String hash = String.valueOf(Objects.hash(auction.getId().toString() + "-" + user.getId().toString()));
        return hash;
    }
}
