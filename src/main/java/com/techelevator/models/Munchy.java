package com.techelevator.models;

import java.math.BigDecimal;

public class Munchy extends Item{
    public Munchy(String itemName, BigDecimal purchasePrice, String slotLocation) {
        super(itemName, purchasePrice, slotLocation);
    }

    @Override
    public void itemJingle(){
        System.out.println("Munchy, Munchy, so Good!");
    }

}

