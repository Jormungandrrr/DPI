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

    public UUID uuid;
    public Seller seller;
    public Card card;
    public Bidder buyer;
    public double amount;

    public Sale(Seller seller, Bidder buyer, Card card, double amount) {
        this.uuid = UUID.randomUUID();
        this.seller = seller;
        this.buyer = buyer;
        this.amount = amount;
        this.card = card;
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

    @Override
    public String toString() {
        return this.buyer.name + " Bought " + this.card.name + " for €" + this.amount;
    }

}
