package com.techelevator.utilities;

import com.techelevator.models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventoryBuilder {

    private List<Item> itemList = new ArrayList<>();
    private String inventoryFilePath;
    private File inventoryFile;


    public InventoryBuilder() {

    }

    public InventoryBuilder(List<Item> itemList) {
        this.itemList = itemList;
        inventoryFilePath = "catering1.csv";
        inventoryFile = new File(inventoryFilePath);
        try(Scanner inventoryFileScanner = new Scanner(inventoryFile)) {
            while(inventoryFileScanner.hasNextLine()) {
                String currentLineItem = inventoryFileScanner.nextLine();
                String[] csvSplitter = currentLineItem.split(",");
                String itemName = csvSplitter[3];
                if(itemName.equalsIgnoreCase("gum")) {
                    itemList.add(new Gum(csvSplitter[1],new BigDecimal(csvSplitter[2]),csvSplitter[0]));
                } else if(itemName.equalsIgnoreCase("drink")) {
                    itemList.add(new Drink(csvSplitter[1],new BigDecimal(csvSplitter[2]),csvSplitter[0]));
                } else if(itemName.equalsIgnoreCase("candy")) {
                    itemList.add(new Candy(csvSplitter[1],new BigDecimal(csvSplitter[2]),csvSplitter[0]));
                } else if(itemName.equalsIgnoreCase("Munchy")) {
                    itemList.add(new Munchy(csvSplitter[1],new BigDecimal(csvSplitter[2]),csvSplitter[0]));
                }
            }
        } catch(FileNotFoundException e) {
            System.out.println("File Not Found.");
        }

    }

}
