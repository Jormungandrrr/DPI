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
public class Sale implements Serializable {

    private UUID uuid;
    private Seller seller;
    private Card card;
    private Bidder buyer;
    private double amount;
    private UUID auctionUUID;

    public Sale(Seller seller, Bidder buyer, Card card, double amount, UUID auction) {
        this.uuid = UUID.randomUUID();
        this.seller = seller;
        this.buyer = buyer;
        this.amount = amount;
        this.card = card;
        this.auctionUUID = auction;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Bidder getBuyer() {
        return buyer;
    }

    public void setBuyer(Bidder buyer) {
        this.buyer = buyer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public UUID getAuctionUUID() {
        return auctionUUID;
    }

    public void setAuctionUUID(UUID auctionUUID) {
        this.auctionUUID = auctionUUID;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return this.buyer.getName() + " Bought " + this.card.getName() + " for €" + this.amount;
    }

}
