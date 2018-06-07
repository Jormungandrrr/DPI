/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Jorrit
 */
public class Auction {
    
    public UUID uuid;
    public ArrayList<Card> cards;
    public ArrayList<Bid> bids;
    public Date start;
    public Date end;
    public Seller seller;

    public Auction(ArrayList<Card> cards, Date start, Date end, Seller seller) {
        this.uuid = UUID.randomUUID();
        this.cards = cards;
        this.start = start;
        this.end = end;
        this.seller = seller;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Bid> getBids() {
        return bids;
    }

    public void setBids(ArrayList<Bid> bids) {
        this.bids = bids;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
    
    
    
}
