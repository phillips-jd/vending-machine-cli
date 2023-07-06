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
    private MoneyHandler moneyHandler = new MoneyHandler();
    private static int transactionCounter = 1;
    private InventoryBuilder inventoryBuilder;

    public VendingMachine() {

    }

    public boolean isOn() {
        return isOn;
    }
    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }
    public static void setTransactionCounter(int transactionCounter) {
        VendingMachine.transactionCounter = transactionCounter;
    }

    public void run(VendingMachine vendingMachine) {
        inventoryBuilder = new InventoryBuilder(itemList);
        while(isOn()) {
            UserOutput.displayHomeScreen();
            String choice = UserInput.getHomeScreenOption();
            if(choice.equals("display")) {
                UserOutput.displayAvailableItems(itemList);
            }
            else if(choice.equals("purchase")) {
                do {
                    while(exitLoop) {
                        String choice2 = UserInput.getPurchaseScreenOption(moneyHandler);
                        if (choice2.equalsIgnoreCase("feed")) {
                            moneyHandler.feedMoney(UserInput.feedMoneyMenu(moneyHandler), moneyHandler, auditFile);
                        } else if (choice2.equalsIgnoreCase("select")) {
                            selectItem(vendingMachine, itemList, moneyHandler, UserInput.purchaseSelect(itemList));
                        } else if (choice2.equalsIgnoreCase("finish")) {
                            System.out.println(moneyHandler.makeChange());
                            auditFile.logEntryToAuditFile(auditFile, moneyHandler.getChangeDue(), moneyHandler.getMachineBalance(), "CHANGE GIVEN:", "");
                            break;
                        } else {
                            break;
                        }
                    }
                } while(moneyHandler.getMachineBalance().compareTo(new BigDecimal("0")) > 0);
                exitLoop = true;
            } else if(choice.equalsIgnoreCase("sales")) {
                SalesLog salesLog = new SalesLog(itemList, totalSales);
            }
            else if(choice.equals("exit")) {
                break;
            }
        }
    }

    public void selectItem(VendingMachine vendingMachine, List<Item> itemList, MoneyHandler moneyHandler, String slotInput) {
        BigDecimal discount = moneyHandler.getDISCOUNT();
        boolean doesSlotExist = false;
        for (Item currentItem : itemList) {
            if(currentItem.getSlotLocation().equalsIgnoreCase(slotInput)) {
                doesSlotExist = true;
                if(!currentItem.isAvailable()) {
                    UserOutput.displayMessage("Item is no longer available.");
                } else if(moneyHandler.getMachineBalance().compareTo(currentItem.getPurchasePrice().subtract(discount)) >= 0 && moneyHandler.isDiscounted(transactionCounter)) {
                    moneyHandler.completeTransaction(vendingMachine, moneyHandler, currentItem, moneyHandler.isDiscounted(transactionCounter), auditFile, totalSales);
                } else if(moneyHandler.getMachineBalance().compareTo(currentItem.getPurchasePrice()) >= 0) {
                    moneyHandler.completeTransaction(vendingMachine, moneyHandler, currentItem, moneyHandler.isDiscounted(transactionCounter), auditFile, totalSales);
                } else {
                    System.out.println("Insufficient funds.");
                }
            }
        }
        if (!doesSlotExist) {
            UserOutput.displayMessage("Slot ID does not exist.");
        }
    }

    public void dispenseItem(Item item, MoneyHandler moneyHandler){
        item.setItemQuantity(item.getItemQuantity() - 1);
        System.out.println("Item name: " + item.getItemName() + " | Item price: $" +
                (((transactionCounter % 2 == 0) ? item.getPurchasePrice().subtract(new BigDecimal(1)) : item.getPurchasePrice()) + " | Remaining Balance: $" + moneyHandler.getMachineBalance()));
        item.itemJingle();
        transactionCounter++;
    }
}
