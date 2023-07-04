package com.techelevator.ui;

import com.techelevator.models.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * Responsibilities: This class should handle formatting and displaying ALL
 * messages to the user
 * 
 * Dependencies: None
 */
public class UserOutput
{

    public static void displayMessage(String message)
    {
        System.out.println();
        System.out.println(message);
        System.out.println();
    }

    public static void displayHomeScreen()
    {
        System.out.println();
        System.out.println("***************************************************");
        System.out.println("                      Home");
        System.out.println("***************************************************");
        System.out.println();
    }

    public static void displayAvailableItems(List<Item> itemList) {
        System.out.format("%-20s %-6s %-8s %-8s%n", "Item Name", "Slot", "Price", "Qty");
        System.out.println("-----------------------------------------");
        for(Item currentItem : itemList) {
            System.out.format("%-20s %-6s %-8s %-8s%n", currentItem.getItemName(),
                    currentItem.getSlotLocation(),
                    "$" + currentItem.getPurchasePrice(),
                    (currentItem.getItemQuantity() > 0) ? currentItem.getItemQuantity() : "NO LONGER AVAILABLE");
        }
    }

}
