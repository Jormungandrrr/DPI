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
public class Card {
    public UUID uuid;
    public String name;
    public String color;
    public String type;
    public String cmc;
    public Seller owner;

    public Card(String name, String color, String type, String cmc, Seller owner) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.color = color;
        this.type = type;
        this.cmc = cmc;
        this.owner = owner;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCmc() {
        return cmc;
    }

    public void setCmc(String cmc) {
        this.cmc = cmc;
    }

    public Seller getOwner() {
        return owner;
    }

    public void setOwner(Seller owner) {
        this.owner = owner;
    }
    
    
}
