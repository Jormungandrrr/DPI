/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Jorrit
 */
public class Seller {
    
    public UUID uuid;
    public String name;
    public double money;
    public ArrayList<Card> cards;
    public ArrayList<Auction> auctions = new ArrayList<Auction>();

    public Seller(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.money = 0;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    
}
