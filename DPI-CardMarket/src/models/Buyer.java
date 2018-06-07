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
public class Buyer {
    public UUID uuid;
    public String name;

    public Buyer(String name) {
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
}
