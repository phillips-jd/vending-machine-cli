package com.techelevator.application;

import com.techelevator.models.Drink;
import com.techelevator.models.Gum;
import com.techelevator.models.Item;
import com.techelevator.ui.UserInput;
import com.techelevator.utilities.MoneyHandler;
import org.junit.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInputTest {

    @Test
    public void getHomeScreenOption_returns_string_display_if_input_string_d() {
        String testInput = "d";
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.getHomeScreenOption();
        Assert.assertEquals("display", actual);
    }

    @Test
    public void getHomeScreenOption_returns_string_purchase_if_input_string_p() {
        String testInput = "p";
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.getHomeScreenOption();
        Assert.assertEquals("purchase", actual);
    }

    @Test
    public void getHomeScreenOption_returns_string_empty_if_input_string_invalid() {
        String testInput = "test";
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.getHomeScreenOption();
        Assert.assertEquals("", actual);
    }

    @Test
    public void getPurchaseScreenOption_returns_string_feed_if_input_string_m() {
        String testInput = "m";
        MoneyHandler testFunds = new MoneyHandler();
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.getPurchaseScreenOption(testFunds);
        Assert.assertEquals("feed", actual);
    }

    @Test
    public void getPurchaseScreenOption_returns_string_select_if_input_string_s() {
        String testInput = "s";
        MoneyHandler testFunds = new MoneyHandler();
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.getPurchaseScreenOption(testFunds);
        Assert.assertEquals("select", actual);
    }

    @Test
    public void getPurchaseScreenOption_returns_string_finish_if_input_string_f() {
        String testInput = "f";
        MoneyHandler testFunds = new MoneyHandler();
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.getPurchaseScreenOption(testFunds);
        Assert.assertEquals("finish", actual);
    }

    @Test
    public void getPurchaseScreenOption_returns_string_empty_if_input_string_invalid() {
        String testInput = "test";
        MoneyHandler testFunds = new MoneyHandler();
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.getPurchaseScreenOption(testFunds);
        Assert.assertEquals("", actual);
    }

    @Test
    public void feedMoneyMenu_returns_string_1_if_input_string_1() {
        String testInput = "1";
        MoneyHandler testFunds = new MoneyHandler();
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.feedMoneyMenu(testFunds);
        Assert.assertEquals("1", actual);
    }

    @Test
    public void feedMoneyMenu_returns_string_5_if_input_string_5() {
        String testInput = "5";
        MoneyHandler testFunds = new MoneyHandler();
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.feedMoneyMenu(testFunds);
        Assert.assertEquals("5", actual);
    }

    @Test
    public void feedMoneyMenu_returns_string_10_if_input_string_10() {
        String testInput = "10";
        MoneyHandler testFunds = new MoneyHandler();
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.feedMoneyMenu(testFunds);
        Assert.assertEquals("10", actual);
    }

    @Test
    public void feedMoneyMenu_returns_string_20_if_input_string_20() {
        String testInput = "20";
        MoneyHandler testFunds = new MoneyHandler();
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.feedMoneyMenu(testFunds);
        Assert.assertEquals("20", actual);
    }

    @Test
    public void feedMoneyMenu_returns_string_e_if_input_string_e() {
        String testInput = "e";
        MoneyHandler testFunds = new MoneyHandler();
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.feedMoneyMenu(testFunds);
        Assert.assertEquals("e", actual);
    }

    @Test (expected = IllegalArgumentException.class)
    public void feedMoneyMenu_returns_illegal_arg_ex_if_input_string_invalid() {
        String testInput = "test";
        MoneyHandler testFunds = new MoneyHandler();
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        UserInput.feedMoneyMenu(testFunds);
    }

    @Test
    public void purchaseSelect_returns_string_a1_if_input_string_a1() {
        List<Item> testList = new ArrayList<>();
        testList.add(new Gum("testGum1", new BigDecimal("1.65"), "A1"));
        testList.add(new Gum("testGum2", new BigDecimal("2.65"), "A2"));
        testList.add(new Drink("testDrink1", new BigDecimal("1.65"), "B1"));
        testList.add(new Gum("testDrink2", new BigDecimal("2.65"), "B2"));
        String testInput = "a1";
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.purchaseSelect(testList);
        Assert.assertEquals("a1", actual);
    }

    @Test
    public void purchaseSelect_returns_string_a1_if_input_string_A1() {
        List<Item> testList = new ArrayList<>();
        testList.add(new Gum("testGum1", new BigDecimal("1.65"), "A1"));
        testList.add(new Gum("testGum2", new BigDecimal("2.65"), "A2"));
        testList.add(new Drink("testDrink1", new BigDecimal("1.65"), "B1"));
        testList.add(new Gum("testDrink2", new BigDecimal("2.65"), "B2"));
        String testInput = "A1";
        Scanner scanner = new Scanner(testInput);
        UserInput.setScanner(scanner);
        String actual = UserInput.purchaseSelect(testList);
        Assert.assertEquals("a1", actual);
    }

}
