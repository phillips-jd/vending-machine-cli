package com.techelevator.ui;

import com.techelevator.application.Fund;
import com.techelevator.application.VendingMachine;
import com.techelevator.models.Item;

import java.util.List;
import java.util.Scanner;

/**
 * Responsibilities: This class should handle receiving ALL input from the User
 * 
 * Dependencies: None
 */
public class UserInput {

    private static Scanner scanner = new Scanner(System.in);

    public static void setScanner(Scanner scanner) {
        UserInput.scanner = scanner;
    }

    public static String getHomeScreenOption()  {
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println();

        System.out.println("D) Display Vending Machine Items");
        System.out.println("P) Purchase");
        System.out.println("E) Exit");

        System.out.println();
        System.out.print("Please select an option: ");

        String selectedOption = scanner.nextLine();
        String option = selectedOption.trim().toLowerCase();
        System.out.println();

        if (option.equals("d"))
        {
            return "display";
        }
        else if (option.equals("p"))
        {
            return "purchase";
        }
        else if (option.equals("e"))
        {
            return "exit";
        }
        else if (option.equals("s")) {
            return "sales";
        }
        else
        {
            return "";
        }

    }

    public static String getPurchaseScreenOption(Fund funds) {
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println();

        System.out.println("M) Feed Money");
        System.out.println("S) Select Item");
        System.out.println("F) Finish Transaction");
        System.out.println();
        System.out.println("Current Money Provided: $" + funds.getMachineBalance());

        System.out.println();
        System.out.print("Please select an option: ");

        String selectedOption = scanner.nextLine();
        String option = selectedOption.trim().toLowerCase();

        if (option.equals("m")) {
            return "feed";
        }
        else if (option.equals("s")) {
            return "select";
        }
        else if (option.equals("f")) {
            return "finish";
        }
        else {
            return "";
        }
    }

    public static String feedMoneyMenu(Fund fund) {
        System.out.println();
        System.out.println("How much money would you like to add?");
        System.out.println();

        System.out.println("1) $1");
        System.out.println("5) $5");
        System.out.println("10) $10");
        System.out.println("20) $20");
        System.out.println("E) Exit");
        System.out.println();
        System.out.println("Current Money Provided: $" + fund.getMachineBalance());
        System.out.println();
        System.out.print("Please select an option: ");

        String selectedOption = scanner.nextLine();
        String option = selectedOption.trim().toLowerCase();

        if (option.equals("1")) {
            return "1";
        } else if (option.equals("5")) {
            return "5";
        } else if (option.equals("10")) {
            return "10";
        } else if (option.equals("20")) {
            return "20";
        } else if (option.equals("e")) {
            return "e";
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String purchaseSelect(List<Item> itemList){
        System.out.println();
        UserOutput.displayAvailableItems(itemList);
        UserOutput.displayMessage("Enter a slot number to purchase an item: ");
        String selectedOption = UserInput.scanner.nextLine();
        String slotInput = selectedOption.trim().toLowerCase();
        return slotInput;
    }

}

