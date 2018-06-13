/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Jorrit
 */
public class Auction implements Serializable {

    public UUID uuid;
    public Card card;
    public long end;
    public Seller seller;
    public double price;

    public ArrayList<Bid> bids = new ArrayList<>();

    public Auction(Card card, long end, double price, Seller seller) {
        this.uuid = UUID.randomUUID();
        this.card = card;
        this.end = end;
        this.seller = seller;
        this.price = price;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public ArrayList<Bid> getBids() {
        return bids;
    }

    public void setBids(ArrayList<Bid> bids) {
        this.bids = bids;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public Seller getSeller() {
        return seller;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public String getTimeLeft() {
        long diff = end - new Date().getTime();
        long totalSeconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = (totalSeconds % 3600) % 60;
        return String.valueOf(hours) + ":" +String.valueOf(minutes) + ":" + String.valueOf(seconds);
    }

    @Override
    public String toString() {
        return this.card.getName() + " €" + this.price + " Time left: " + this.getTimeLeft();
    }

}
