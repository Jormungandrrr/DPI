/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.UUID;

/**
 *
 * @author Jorrit
 */
public class Bid {
    
     public UUID uuid;
     public Buyer bidder;
     public double amount;
     public Auction auction;

    public Bid(Buyer bidder, double amount, Auction auction) {
        this.uuid = UUID.randomUUID();
        this.bidder = bidder;
        this.amount = amount;
        this.auction = auction;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Buyer getBidder() {
        return bidder;
    }

    public void setBidder(Buyer bidder) {
        this.bidder = bidder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }
    
}
