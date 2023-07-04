package com.techelevator.application;

import com.techelevator.ui.UserInput;
import com.techelevator.ui.UserOutput;

import com.techelevator.models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
    private static DateTimeFormatter salesDateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yy-hhmmssa");
    private static String entryTime = dateTimeFormatter.format(LocalDateTime.now());
    private static Audit auditFile = new Audit("audit.txt");
    private List<Item> itemList = new ArrayList<>();
    private static BigDecimal totalSales = new BigDecimal("0.00");
    private boolean isOn = true;
    private boolean exitLoop = true;
    private Fund funds = new Fund();
    private static int transactionCounter = 1;
    

    public VendingMachine() {

    }

    public static int getTransactionCounter() {
        return transactionCounter;
    }

    public static void setTransactionCounter(int transactionCounter) {
        VendingMachine.transactionCounter = transactionCounter;
    }

    public List<Item> getItemList() {
        return this.itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }


    public static BigDecimal getTotalSales() {
        return totalSales;
    }

    public static void setTotalSales(BigDecimal totalSales) {
        VendingMachine.totalSales = totalSales;
    }

    public Fund getFunds() {
        return funds;
    }

    public void setFunds(Fund funds) {
        this.funds = funds;
    }


    public void run() {

        inventoryBuilder(itemList);

        while(isOn()) {

            UserOutput.displayHomeScreen();
            String choice = UserInput.getHomeScreenOption();

            if(choice.equals("display")) {
                displayAvailableItems(itemList);
            }
            else if(choice.equals("purchase")) {
                while (exitLoop) {
                    String choice2 = UserInput.getPurchaseScreenOption(funds);
                    if (choice2.equalsIgnoreCase("feed")) {
                        feedMenuAction(UserInput.feedMoneyMenu(funds), funds);
                    } else if (choice2.equalsIgnoreCase("select")) {
                        selectItem(itemList, funds, UserInput.purchaseSelect(itemList));
                    } else if (choice2.equalsIgnoreCase("finish")) {
                        System.out.println(funds.makeChange());
                        logEntryToAuditFile(auditFile,funds.getChangeDue(),funds.getMachineBalance(),"CHANGE GIVEN:", "");
                        break;
                    } else {
                        break;
                    }
                }
                exitLoop = true;
            } else if (choice.equalsIgnoreCase("sales")) {
                createSalesReport(itemList);
            }

            else if(choice.equals("exit")) {
                break;
            }
        }
    }

    public boolean isOn() {
        return isOn;
    }

    public void inventoryBuilder(List<Item> itemList) {

        File sourceFile = new File("catering1.csv");
        try (Scanner inputFile = new Scanner(sourceFile)) {
            while (inputFile.hasNextLine()) {
                String line = inputFile.nextLine();
                String[] lineArr = line.split(",");
                String name = lineArr[3];

                if(name.equalsIgnoreCase("gum")) {
                    itemList.add(new Gum(lineArr[1],new BigDecimal(lineArr[2]),lineArr[0]));
                } else if(name.equalsIgnoreCase("drink")) {
                    itemList.add(new Drink(lineArr[1],new BigDecimal(lineArr[2]),lineArr[0]));
                } else if(name.equalsIgnoreCase("candy")) {
                    itemList.add(new Candy(lineArr[1],new BigDecimal(lineArr[2]),lineArr[0]));
                } else if(name.equalsIgnoreCase("Munchy")) {
                    itemList.add(new Munchy(lineArr[1],new BigDecimal(lineArr[2]),lineArr[0]));
                }
            }
        } catch(FileNotFoundException e) {
            System.out.println("File Not Found.");
        }
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

    public static void selectItem(List<Item> itemList, Fund fund, String slotInput) {
        BigDecimal discount = fund.getDISCOUNT();
        boolean doesSlotExist = false;

        for (Item currentItem : itemList) {
            if (currentItem.getSlotLocation().equalsIgnoreCase(slotInput)) {
                doesSlotExist = true;
                if (!currentItem.isAvailable()) {
                    UserOutput.displayMessage("Item is no longer available.");
                } else if (fund.getMachineBalance().compareTo(currentItem.getPurchasePrice().subtract(discount)) >= 0 && isDiscounted(transactionCounter)) {
                    completeTransaction(fund, currentItem, isDiscounted(transactionCounter));
                } else if (fund.getMachineBalance().compareTo(currentItem.getPurchasePrice()) >= 0) {
                    completeTransaction(fund, currentItem, isDiscounted(transactionCounter));
                } else {
                    System.out.println("Insufficient funds.");
                }
            }
        }
        if (!doesSlotExist) {
            UserOutput.displayMessage("Slot ID does not exist.");
        }
    }

    public static void dispenseItem(Item item, Fund funds){
        item.setItemQuantity(item.getItemQuantity() - 1);
        System.out.println("Item name: " + item.getItemName() + " | Item price: $" +
                (((transactionCounter % 2 == 0) ? item.getPurchasePrice().subtract(new BigDecimal(1)) : item.getPurchasePrice()) + " | Remaining Balance: $" + funds.getMachineBalance()));
        item.itemJingle();
        transactionCounter++;
    }


    public static void completeTransaction(Fund fund, Item currentItem, boolean isDiscounted){
        String transactionType = currentItem.getItemName();
        BigDecimal discount = fund.getDISCOUNT();
        if (isDiscounted) {
            fund.removeFunds(applyDiscount(currentItem.getPurchasePrice(), discount, currentItem));
            currentItem.setDiscountedSold(currentItem.getDiscountedSold()+1);
            System.out.println("You saved $" + discount);
            dispenseItem(currentItem, fund);
            // in logEntry: subbed fund.getMachineBalance().add(currentItem.getPurchasePrice().subtract(discount))
            // for currentItem.getPurchasePrice().subtract(discount) to match readme example
            logEntryToAuditFile(auditFile, fund.getMachineBalance().add(currentItem.getPurchasePrice().subtract(discount)), fund.getMachineBalance(), transactionType, currentItem.getSlotLocation());
            totalSales = totalSales.add(applyDiscount(currentItem.getPurchasePrice(), discount, currentItem));
        } else {
            fund.removeFunds(currentItem.getPurchasePrice());
            currentItem.setFullPriceSold(currentItem.getFullPriceSold() + 1);
            dispenseItem(currentItem, fund);
            // in logEntry: subbed fund.getMachineBalance().add(currentItem.getPurchasePrice())
            // for currentItem.getPurchasePrice() to match readme example
            logEntryToAuditFile(auditFile, fund.getMachineBalance().add(currentItem.getPurchasePrice()), fund.getMachineBalance(), transactionType, currentItem.getSlotLocation());
            totalSales = totalSales.add(currentItem.getPurchasePrice());
        }

    }

    public void feedMenuAction(String option, Fund funds) {
        String transactionType = "MONEY FED:";
        if (option.equals("1")) {
            funds.addFunds(funds.getONE_DOLLAR_BILL());
            logEntryToAuditFile(auditFile, funds.getONE_DOLLAR_BILL(), funds.getMachineBalance(), transactionType, "");
        } else if (option.equals("5")) {
            funds.addFunds(funds.getFIVE_DOLLAR_BILL());
            logEntryToAuditFile(auditFile, funds.getFIVE_DOLLAR_BILL(), funds.getMachineBalance(), transactionType, "");
        } else if (option.equals("10")) {
            funds.addFunds(funds.getTEN_DOLLAR_BILL());
            logEntryToAuditFile(auditFile, funds.getTEN_DOLLAR_BILL(), funds.getMachineBalance(), transactionType, "");
        } else if (option.equals("20")) {
            funds.addFunds(funds.getTWENTY_DOLLAR_BILL());
            logEntryToAuditFile(auditFile, funds.getTWENTY_DOLLAR_BILL(), funds.getMachineBalance(), transactionType, "");
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static void logEntryToAuditFile(Audit auditFile, BigDecimal addedAmount, BigDecimal machineBalance, String transactionType, String slotId) {
        try {
            String formattedEntry = String.format("%-20s %-16s %2s %7s %7s", entryTime, transactionType, slotId, "$" + addedAmount, "$" + machineBalance);
            auditFile.write(formattedEntry);
        } catch (Exception e) {
            System.out.println("Logging error.");
        }
    }

    public static BigDecimal applyDiscount(BigDecimal purchasePrice, BigDecimal discount, Item item) {
        BigDecimal discountedPrice = purchasePrice.subtract(discount);
        return discountedPrice;
    }

    public static boolean isDiscounted(int transactionQuantity){
        boolean isDiscounted = transactionQuantity % 2 == 0;
        return isDiscounted;
    }

    public static void createSalesReport(List<Item> itemList){

        String pathName = String.format("%s%s",salesDateTimeFormatter.format(LocalDateTime.now()),"-SaleLog.txt");
        SalesLog salesLog = new SalesLog(pathName);
        salesLog.write("Taste Elevator Sales Report");
        for (Item item : itemList) {
            try {
                String formattedEntry = String.format("%s|%s|%s", item.getItemName(), item.getFullPriceSold() , item.getDiscountedSold());
                salesLog.write(formattedEntry);
            } catch (Exception e) {
                System.out.println("Logging error.");
            }
        }
        salesLog.write("TOTAL SALES " + totalSales);

    }





}
