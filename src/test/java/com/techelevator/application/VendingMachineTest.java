package com.techelevator.application;

import com.techelevator.application.VendingMachine;
import com.techelevator.models.Audit;
import com.techelevator.models.Drink;
import com.techelevator.models.Gum;
import com.techelevator.models.Item;
import com.techelevator.ui.UserInput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class VendingMachineTest {

    private VendingMachine sut;
    private PrintWriter testWriter;
    private Scanner testScanner;

    @Before
    public void setUp() throws Exception {
        sut = new VendingMachine();
    }

    @Test
    public void inventoryBuilder_makes_list_of_correct_size() {
        List<Item> actual = new ArrayList<>();
        sut.inventoryBuilder(actual);

        assertEquals(16, actual.size());
    }

    @Test
    public void inventoryBuilder_has_list_with_matching() {
        List<Item> actual = new ArrayList<>();
        sut.inventoryBuilder(actual);

        assertEquals("U-Chews", actual.get(0).getItemName());
    }

    @Test
    public void displayAvailableItems() {
        List<Item> actual = new ArrayList<>();
        sut.inventoryBuilder(actual);

        assertEquals("U-Chews", actual.get(0).getItemName());
    }

    @Test
    public void selectItem_prints_item_no_longer_available_message_if_quantity_is_0() {
        List<Item> testList = new ArrayList<>();
        Fund testFunds = new Fund();
        testList.add(new Gum("testGum1", new BigDecimal("1.65"), "A1"));
        testList.add(new Gum("testGum2", new BigDecimal("2.65"), "A2"));
        testList.add(new Drink("testDrink1", new BigDecimal("1.65"), "B1"));
        testList.add(new Gum("testDrink2", new BigDecimal("2.65"), "B2"));
        for (Item currentItem : testList) {
            currentItem.setItemQuantity(0);
        }
        VendingMachine.selectItem(testList, testFunds, "A1");
    }

    @Test
    public void selectItem_prints_insufficient_funds_if_balance_0_always_passes() {
        List<Item> testList = new ArrayList<>();
        Fund testFunds = new Fund();
        testList.add(new Gum("testGum1", new BigDecimal("1.65"), "A1"));
        testList.add(new Gum("testGum2", new BigDecimal("2.65"), "A2"));
        testList.add(new Drink("testDrink1", new BigDecimal("1.65"), "B1"));
        testList.add(new Gum("testDrink2", new BigDecimal("2.65"), "B2"));
        for (Item currentItem : testList) {
            currentItem.setItemQuantity(6);
        }

        VendingMachine.selectItem(testList, testFunds,"B1");
    }

    @Test
    public void selectItem_prints_item_name_and_price_and_balance_and_jingle_with_valid_slot_and_funds_always_passes() {
        List<Item> testList = new ArrayList<>();
        Fund testFunds = new Fund();
        testFunds.setMachineBalance(new BigDecimal("5"));
        testList.add(new Gum("testGum1", new BigDecimal("1.65"), "A1"));
        testList.add(new Gum("testGum2", new BigDecimal("2.65"), "A2"));
        testList.add(new Drink("testDrink1", new BigDecimal("1.65"), "B1"));
        testList.add(new Gum("testDrink2", new BigDecimal("2.65"), "B2"));
        for (Item currentItem : testList) {
            currentItem.setItemQuantity(6);
        }

        VendingMachine.selectItem(testList, testFunds,"B1");
    }

    @Test
    public void selectItem_prints_slot_id_error_if_slot_input_is_invalid_always_passes() {
        List<Item> testList = new ArrayList<>();
        Fund testFunds = new Fund();
        testFunds.setMachineBalance(new BigDecimal("5"));
        testList.add(new Gum("testGum1", new BigDecimal("1.65"), "A1"));
        testList.add(new Gum("testGum2", new BigDecimal("2.65"), "A2"));
        testList.add(new Drink("testDrink1", new BigDecimal("1.65"), "B1"));
        testList.add(new Gum("testDrink2", new BigDecimal("2.65"), "B2"));
        for (Item currentItem : testList) {
            currentItem.setItemQuantity(6);
        }

        VendingMachine.selectItem(testList, testFunds,"C1");
    }

    @Test
    public void dispenseItem_decrements_item_quantity_by_1() {
        Fund testFunds = new Fund();
        Item testItem = new Gum("testGum", new BigDecimal("1.65"), "a1");
        int initialQuantity = 6;
        testItem.setItemQuantity(initialQuantity);
        VendingMachine.dispenseItem(testItem, testFunds);
        Assert.assertEquals(5, testItem.getItemQuantity());
    }

    @Test
    public void dispenseItem_prints_item_name_and_gum_jingle_and_balance_always_passes() {
        Fund testFunds = new Fund();
        Item testItem = new Gum("testGum", new BigDecimal("1.65"), "a1");
        VendingMachine.dispenseItem(testItem, testFunds);
    }

    @Test
    public void dispenseItem_prints_item_name_and_drink_jingle_and_balance_always_passes() {
        Fund testFunds = new Fund();
        Item testItem = new Drink("testDrink", new BigDecimal("1.65"), "a1");
        VendingMachine.dispenseItem(testItem, testFunds);
    }

    @Test
    public void dispenseItem_prints_full_item_price_if_transaction_counter_not_even_always_passes() {
        Fund testFunds = new Fund();
        Item testItem = new Drink("testDrink", new BigDecimal("1.65"), "a1");
        VendingMachine.setTransactionCounter(1);
        VendingMachine.dispenseItem(testItem, testFunds);
    }

    @Test
    public void dispenseItem_prints_discounted_item_price_if_transaction_counter_is_even_always_passes() {
        Fund testFunds = new Fund();
        Item testItem = new Drink("testDrink", new BigDecimal("1.65"), "a1");
        VendingMachine.setTransactionCounter(2);
        VendingMachine.dispenseItem(testItem, testFunds);
    }

    @Test
    public void completeTransaction_should_remove_full_item_price_from_machine_balance_if_no_discount() {
        Fund testFund = new Fund();
        BigDecimal initialBalance = new BigDecimal("5");
        testFund.setMachineBalance(initialBalance);
        Item testItem = new Gum("testGum", new BigDecimal("1.65"), "a1");
        boolean isDiscounted = false;
        BigDecimal expected = initialBalance.subtract(testItem.getPurchasePrice());
        VendingMachine.completeTransaction(testFund, testItem, isDiscounted);
        Assert.assertEquals(expected, testFund.getMachineBalance());
    }

    @Test
    public void completeTransaction_should_remove_item_price_less_1_from_machine_balance_if_discounted() {
        Fund testFund = new Fund();
        BigDecimal discount = new BigDecimal("1");
        BigDecimal initialBalance = new BigDecimal("5");
        testFund.setMachineBalance(initialBalance);
        Item testItem = new Gum("testGum", new BigDecimal("1.65"), "a1");
        boolean isDiscounted = true;
        BigDecimal expected = initialBalance.subtract(testItem.getPurchasePrice().subtract(discount));
        VendingMachine.completeTransaction(testFund, testItem, isDiscounted);
        Assert.assertEquals(expected, testFund.getMachineBalance());
    }

    @Test
    public void feedMenuAction_should_increase_balance_by_1_if_input_is_1() {
        String testInput = "1";
        Fund testFund = new Fund();
        BigDecimal initialBalance = testFund.getMachineBalance();
        sut.feedMenuAction(testInput, testFund);
        BigDecimal actual = testFund.getMachineBalance();
        Assert.assertEquals(initialBalance.add(testFund.getONE_DOLLAR_BILL()), actual);
    }

    @Test
    public void feedMenuAction_should_increase_balance_by_5_if_input_is_5() {
        String testInput = "5";
        Fund testFund = new Fund();
        BigDecimal initialBalance = testFund.getMachineBalance();
        sut.feedMenuAction(testInput, testFund);
        BigDecimal actual = testFund.getMachineBalance();
        Assert.assertEquals(initialBalance.add(testFund.getFIVE_DOLLAR_BILL()), actual);
    }

    @Test
    public void feedMenuAction_should_increase_balance_by_10_if_input_is_10() {
        String testInput = "10";
        Fund testFund = new Fund();
        BigDecimal initialBalance = testFund.getMachineBalance();
        sut.feedMenuAction(testInput, testFund);
        BigDecimal actual = testFund.getMachineBalance();
        Assert.assertEquals(initialBalance.add(testFund.getTEN_DOLLAR_BILL()), actual);
    }

    @Test
    public void feedMenuAction_should_increase_balance_by_20_if_input_is_20() {
        String testInput = "20";
        Fund testFund = new Fund();
        BigDecimal initialBalance = testFund.getMachineBalance();
        sut.feedMenuAction(testInput, testFund);
        BigDecimal actual = testFund.getMachineBalance();
        Assert.assertEquals(initialBalance.add(testFund.getTWENTY_DOLLAR_BILL()), actual);
    }

    @Test
    public void feedMenuAction_should_throw_exception_for_invalid_input() {
        String testInput = "e";
        Fund testFund = new Fund();
        try {
            sut.feedMenuAction(testInput, testFund);
        } catch (IllegalArgumentException e) {
            System.out.println("Whoops");
        }
    }

    @Test
    public void logEntryToAuditFile_should_log_entry_to_test_file() {
        Audit auditTestFile = new Audit("audittest.txt");
        BigDecimal addedAmount = new BigDecimal(5);
        BigDecimal machineBalance = new BigDecimal(10);
        String transactionType = "Test1";
        String slotId = "Test2";
        boolean doesAuditTestFileHaveLine = false;
        try {
            testWriter = new PrintWriter(new FileWriter("audittest.txt", true));
            testScanner = new Scanner("audittest.txt");
            VendingMachine.logEntryToAuditFile(auditTestFile, addedAmount, machineBalance, transactionType, slotId);
            if (testScanner.hasNextLine()) {
                doesAuditTestFileHaveLine = true;
            }
        } catch (IOException e) {
            System.out.println("Whoops");
        }
        Assert.assertTrue(doesAuditTestFileHaveLine);
    }

    @Test
    public void logEntryToAuditFile_should_append_not_overwrite() {
        Audit auditTestFile = new Audit("audittest.txt");
        BigDecimal addedAmount = new BigDecimal(5);
        BigDecimal machineBalance = new BigDecimal(10);
        String transactionType = "Test1";
        String slotId = "Test2";
        int lineCounter = 0;
        boolean doesAuditTestFileHaveMultipleLines;
        try {
            for (int i = 0; i < 2; i++) {
                testWriter = new PrintWriter(new FileWriter("audittest.txt", true));
                testScanner = new Scanner("audittest.txt");
                VendingMachine.logEntryToAuditFile(auditTestFile, addedAmount, machineBalance, transactionType, slotId);
                if (testScanner.hasNextLine()) {
                    lineCounter++;
                }
            }
        } catch(IOException e) {
                System.out.println("Whoops");
        }
        doesAuditTestFileHaveMultipleLines = lineCounter > 1;
        Assert.assertTrue(doesAuditTestFileHaveMultipleLines);
    }

    @Test
    public void applyDiscount_should_subtract_1_from_item_purchase_price() {
        BigDecimal purchasePrice = new BigDecimal(1.65);
        BigDecimal discount = new BigDecimal(1);
        BigDecimal expected = purchasePrice.subtract(discount);
        Item testItem = new Gum("testGum", purchasePrice, "a1");
        BigDecimal actual = VendingMachine.applyDiscount(testItem.getPurchasePrice(), discount, testItem);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void isDiscounted_returns_false_if_counter_odd() {
        int transactionQuantity = 1;
        boolean actual = VendingMachine.isDiscounted(transactionQuantity);
        Assert.assertFalse(actual);
    }

    @Test
    public void isDiscounted_returns_true_if_counter_even() {
        int transactionQuantity = 4;
        boolean actual = VendingMachine.isDiscounted(transactionQuantity);
        Assert.assertTrue(actual);
    }

}