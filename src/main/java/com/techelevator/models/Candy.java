package com.techelevator.models;

import java.math.BigDecimal;

public class Candy extends Item{
    public Candy(String itemName, BigDecimal purchasePrice, String slotLocation) {
        super(itemName, purchasePrice, slotLocation);
    }

    @Override
    public void itemJingle(){
        System.out.println("Sugar, Sugar, so Sweet!");
    }
}
