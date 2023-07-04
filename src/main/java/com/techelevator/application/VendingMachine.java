package com.techelevator.application;

import com.techelevator.ui.UserInput;
import com.techelevator.ui.UserOutput;

import com.techelevator.models.*;
import com.techelevator.utilities.Audit;
import com.techelevator.utilities.InventoryBuilder;
import com.techelevator.utilities.MoneyHandler;
import com.techelevator.utilities.SalesLog;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VendingMachine {

    private Audit auditFile = new Audit("audit.txt");
    private List<Item> itemList = new ArrayList<>();
    private BigDecimal totalSales = new BigDecimal("0.00");
    private boolean isOn = true;
    private boolean exitLoop = true;
    private MoneyHandler funds = new MoneyHandler();
    private static int transactionCounter = 1;
    private InventoryBuilder inventoryBuilder;
    

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


    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public MoneyHandler getFunds() {
        return funds;
    }

    public void setFunds(MoneyHandler funds) {
        this.funds = funds;
    }


    public void run() {

        inventoryBuilder = new InventoryBuilder(itemList);

        while(isOn()) {
            UserOutput.displayHomeScreen();
            String choice = UserInput.getHomeScreenOption();
            if(choice.equals("display")) {
                UserOutput.displayAvailableItems(itemList);
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
                        auditFile.logEntryToAuditFile(auditFile,funds.getChangeDue(),funds.getMachineBalance(),"CHANGE GIVEN:", "");
                        break;
                    } else {
                        break;
                    }
                }
                exitLoop = true;
            } else if (choice.equalsIgnoreCase("sales")) {
                SalesLog salesLog = new SalesLog(itemList, totalSales);
            }
            else if(choice.equals("exit")) {
                break;
            }
        }
    }

    public boolean isOn() {
        return isOn;
    }

//    public void inventoryBuilder(List<Item> itemList) {
//
//        File sourceFile = new File("catering1.csv");
//        try (Scanner inputFile = new Scanner(sourceFile)) {
//            while (inputFile.hasNextLine()) {
//                String line = inputFile.nextLine();
//                String[] lineArr = line.split(",");
//                String name = lineArr[3];
//
//                if(name.equalsIgnoreCase("gum")) {
//                    itemList.add(new Gum(lineArr[1],new BigDecimal(lineArr[2]),lineArr[0]));
//                } else if(name.equalsIgnoreCase("drink")) {
//                    itemList.add(new Drink(lineArr[1],new BigDecimal(lineArr[2]),lineArr[0]));
//                } else if(name.equalsIgnoreCase("candy")) {
//                    itemList.add(new Candy(lineArr[1],new BigDecimal(lineArr[2]),lineArr[0]));
//                } else if(name.equalsIgnoreCase("Munchy")) {
//                    itemList.add(new Munchy(lineArr[1],new BigDecimal(lineArr[2]),lineArr[0]));
//                }
//            }
//        } catch(FileNotFoundException e) {
//            System.out.println("File Not Found.");
//        }
//    }

//    public static void displayAvailableItems(List<Item> itemList) {
//        System.out.format("%-20s %-6s %-8s %-8s%n", "Item Name", "Slot", "Price", "Qty");
//        System.out.println("-----------------------------------------");
//        for(Item currentItem : itemList) {
//            System.out.format("%-20s %-6s %-8s %-8s%n", currentItem.getItemName(),
//                    currentItem.getSlotLocation(),
//                    "$" + currentItem.getPurchasePrice(),
//                    (currentItem.getItemQuantity() > 0) ? currentItem.getItemQuantity() : "NO LONGER AVAILABLE");
//        }
//    }

    public static void selectItem(List<Item> itemList, MoneyHandler moneyHandler, String slotInput) {
        BigDecimal discount = moneyHandler.getDISCOUNT();
        boolean doesSlotExist = false;

        for (Item currentItem : itemList) {
            if (currentItem.getSlotLocation().equalsIgnoreCase(slotInput)) {
                doesSlotExist = true;
                if (!currentItem.isAvailable()) {
                    UserOutput.displayMessage("Item is no longer available.");
                } else if (moneyHandler.getMachineBalance().compareTo(currentItem.getPurchasePrice().subtract(discount)) >= 0 && isDiscounted(transactionCounter)) {
                    completeTransaction(moneyHandler, currentItem, isDiscounted(transactionCounter));
                } else if (moneyHandler.getMachineBalance().compareTo(currentItem.getPurchasePrice()) >= 0) {
                    completeTransaction(moneyHandler, currentItem, isDiscounted(transactionCounter));
                } else {
                    System.out.println("Insufficient funds.");
                }
            }
        }
        if (!doesSlotExist) {
            UserOutput.displayMessage("Slot ID does not exist.");
        }
    }

    public static void dispenseItem(Item item, MoneyHandler funds){
        item.setItemQuantity(item.getItemQuantity() - 1);
        System.out.println("Item name: " + item.getItemName() + " | Item price: $" +
                (((transactionCounter % 2 == 0) ? item.getPurchasePrice().subtract(new BigDecimal(1)) : item.getPurchasePrice()) + " | Remaining Balance: $" + funds.getMachineBalance()));
        item.itemJingle();
        transactionCounter++;
    }


    public static void completeTransaction(MoneyHandler moneyHandler, Item currentItem, boolean isDiscounted){
        String transactionType = currentItem.getItemName();
        BigDecimal discount = moneyHandler.getDISCOUNT();
        if (isDiscounted) {
            moneyHandler.removeFunds(applyDiscount(currentItem.getPurchasePrice(), discount, currentItem));
            currentItem.setDiscountedSold(currentItem.getDiscountedSold()+1);
            System.out.println("You saved $" + discount);
            dispenseItem(currentItem, moneyHandler);
            logEntryToAuditFile(auditFile, moneyHandler.getMachineBalance().add(currentItem.getPurchasePrice().subtract(discount)), moneyHandler.getMachineBalance(), transactionType, currentItem.getSlotLocation());
            totalSales = totalSales.add(applyDiscount(currentItem.getPurchasePrice(), discount, currentItem));
        } else {
            moneyHandler.removeFunds(currentItem.getPurchasePrice());
            currentItem.setFullPriceSold(currentItem.getFullPriceSold() + 1);
            dispenseItem(currentItem, moneyHandler);
            logEntryToAuditFile(auditFile, moneyHandler.getMachineBalance().add(currentItem.getPurchasePrice()), moneyHandler.getMachineBalance(), transactionType, currentItem.getSlotLocation());
            totalSales = totalSales.add(currentItem.getPurchasePrice());
        }

    }

    public void feedMenuAction(String option, MoneyHandler funds) {
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

//    public static void logEntryToAuditFile(Audit auditFile, BigDecimal addedAmount, BigDecimal machineBalance, String transactionType, String slotId) {
//        try {
//            String formattedEntry = String.format("%-20s %-16s %2s %7s %7s", entryTime, transactionType, slotId, "$" + addedAmount, "$" + machineBalance);
//            auditFile.write(formattedEntry);
//        } catch (Exception e) {
//            System.out.println("Logging error.");
//        }
//    }

    public static BigDecimal applyDiscount(BigDecimal purchasePrice, BigDecimal discount, Item item) {
        BigDecimal discountedPrice = purchasePrice.subtract(discount);
        return discountedPrice;
    }

    public static boolean isDiscounted(int transactionQuantity){
        boolean isDiscounted = transactionQuantity % 2 == 0;
        return isDiscounted;
    }

//    public static void createSalesLog(List<Item> itemList){
//
//        String pathName = String.format("%s%s",salesLogDateTimeFormatter.format(LocalDateTime.now()),"-SaleLog.txt");
//        SalesLog salesLog = new SalesLog(pathName);
//        salesLog.write("Taste Elevator Sales Report");
//        for (Item item : itemList) {
//            try {
//                String formattedEntry = String.format("%s|%s|%s", item.getItemName(), item.getFullPriceSold() , item.getDiscountedSold());
//                salesLog.write(formattedEntry);
//            } catch (Exception e) {
//                System.out.println("Logging error.");
//            }
//        }
//        salesLog.write("TOTAL SALES " + totalSales);
//
//    }





}
