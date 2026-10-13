package com.williameliasson.car_auction_backend.models;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// the plan
// Bid
// user: User
// createdAt: Instant
// auction: Auction
// amount: int


@Entity 
@Getter 
@Setter 
@NoArgsConstructor 
@Table(name = "bids")
public class Bid {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @ManyToOne(optional = false)
    @JoinColumn(name="auction_id")
    private Auction auction;

    @Column(nullable = false)
    private Integer amount;
}
