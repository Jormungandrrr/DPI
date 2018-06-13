/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Jorrit
 */
public class Bid implements Serializable {

    private UUID uuid;
    private UUID auction;
    private Bidder bidder;
    private double amount;

    public Bid(double amount) {
        this.uuid = UUID.randomUUID();
        this.amount = amount;
    }
    
    public Bid(Bidder bidder, UUID auction, double amount) {
        this.bidder = bidder;
        this.auction = auction;
        this.amount = amount;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Bidder getBidder() {
        return bidder;
    }

    public UUID getAuction() {
        return auction;
    }

    public void setAuction(UUID auction) {
        this.auction = auction;
    }  

    public void setBidder(Bidder bidder) {
        this.bidder = bidder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
