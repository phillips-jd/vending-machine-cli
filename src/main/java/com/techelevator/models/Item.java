package com.techelevator.models;

import java.math.BigDecimal;

public abstract class Item {
    private String itemName;
    private BigDecimal purchasePrice;
    private String slotLocation;
    private int itemQuantity = 6;
    private int discountedSold = 0;
    private int fullPriceSold = 0;



    public Item(String itemName, BigDecimal purchasePrice, String slotLocation) {
        this.itemName = itemName;
        this.purchasePrice = purchasePrice;
        this.slotLocation = slotLocation;
    }

    public int getDiscountedSold() {
        return discountedSold;
    }

    public void setDiscountedSold(int discountedSold) {
        this.discountedSold = discountedSold;
    }

    public int getFullPriceSold() {
        return fullPriceSold;
    }

    public void setFullPriceSold(int fullPriceSold) {
        this.fullPriceSold = fullPriceSold;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getSlotLocation() {
        return slotLocation;
    }

    public void setSlotLocation(String slotLocation) {
        this.slotLocation = slotLocation;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public void itemJingle(){

    }

    public boolean isAvailable() {
        return itemQuantity > 0;
    }

}
