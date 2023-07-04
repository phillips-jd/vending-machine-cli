package com.techelevator.models;

import java.math.BigDecimal;

public class Drink extends Item{
    public Drink(String itemName, BigDecimal purchasePrice, String slotLocation) {
        super(itemName, purchasePrice, slotLocation);
    }
    @Override
    public void itemJingle(){
        System.out.println("Drinky, Drinky, Slurp Slurp!");
    }
}
