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
public class Sale {
    
    public UUID uuid;
    public Seller seller;
    public Buyer buyer;
    public double amount;

    public Sale(Seller seller, Buyer buyer, double amount) {
        this.uuid = UUID.randomUUID();
        this.seller = seller;
        this.buyer = buyer;
        this.amount = amount;
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

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    
}
