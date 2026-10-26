package com.williameliasson.car_auction_backend.mappers;

import org.springframework.stereotype.Component;

import com.williameliasson.car_auction_backend.dtos.OrderDisplay;
import com.williameliasson.car_auction_backend.models.Order;

@Component 
public class OrderMapper {
    
    public OrderDisplay toOrderDisplay(Order order){
        return new OrderDisplay(order.getId(), order.getUser().getUsername(), order.getAmountSek(), order.getCreatedAt(), order.isPaid());
    }
}
