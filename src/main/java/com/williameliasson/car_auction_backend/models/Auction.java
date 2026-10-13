package com.williameliasson.car_auction_backend.models;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
@NoArgsConstructor 
@Table(name="auctions")
public class Auction {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    private String plate;

    @Column(nullable = true)
    private Instant deadline;

    @ManyToOne
    @JoinColumn(name="carmake_id")
    private CarMake make;

    @Column(nullable = true)
    private Integer modelYear;

    @Column(nullable = true)
    private Integer odometerKm;

    // The two valuation fields will be populated via the OpenAI integration
    @Column(nullable = true)
    private Integer valuationMin;
    
    @Column(nullable = true)
    private Integer valuationMax;

    @OneToMany(mappedBy = "auction")
    private List<Bid> bids;

    @Column(nullable = false)
    private boolean winnerHasPaid;
}
