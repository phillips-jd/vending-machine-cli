package com.techelevator.models;

import java.math.BigDecimal;

public class Gum extends Item{
    public Gum(String itemName, BigDecimal purchasePrice, String slotLocation) {
        super(itemName, purchasePrice, slotLocation);
    }

    @Override
    public void itemJingle(){
        System.out.println("Chewy, Chewy, Lots O Bubbles!");
    }
}
