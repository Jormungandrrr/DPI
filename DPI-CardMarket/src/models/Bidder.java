/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Jorrit
 */
public class Bidder implements Serializable {
    private UUID uuid;
    private String name;
    private ArrayList<String> colors;

    public Bidder(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
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

    public ArrayList<String> getColors() {
        return colors;
    }
    
    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }
    
    
}
