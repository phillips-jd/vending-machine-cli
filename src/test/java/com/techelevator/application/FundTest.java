package com.techelevator.application;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class FundTest {
    private Fund sut;
    @Before
    public void setUp() throws Exception {
        sut = new Fund();
    }

    @Test
    public void addFunds_adds_20_to_machine_balance_of_zero() {
        sut.addFunds(new BigDecimal("20.00"));

        BigDecimal actual = sut.getMachineBalance();
        Assert.assertEquals(new BigDecimal("20.00"), actual);
    }

    @Test
    public void addFunds_adds_20_to_machine_balance_of_20() {
        sut.setMachineBalance(new BigDecimal("20.00"));
        sut.addFunds(new BigDecimal("20.00"));

        BigDecimal actual = sut.getMachineBalance();
        Assert.assertEquals(new BigDecimal("40.00"), actual);
    }

    @Test
    public void removeFunds_removes_20_from_balance_of_20() {
        sut.setMachineBalance(new BigDecimal("20.00"));
        sut.removeFunds(new BigDecimal("20.00"));

        BigDecimal actual = sut.getMachineBalance();
        Assert.assertEquals(new BigDecimal("0.00"), actual);
    }
    @Test
    public void removeFunds_removes_21_from_balance_of_20() {
        sut.setMachineBalance(new BigDecimal("20.00"));
        sut.removeFunds(new BigDecimal("21.00"));

        BigDecimal actual = sut.getMachineBalance();

        Assert.assertEquals(new BigDecimal("0.00"), actual);
    }

    @Test
    public void removeFunds_removes_20_from_balance_of_22() {
        sut.setMachineBalance(new BigDecimal("22.00"));
        sut.removeFunds(new BigDecimal("20.00"));

        BigDecimal actual = sut.getMachineBalance();
        Assert.assertEquals(new BigDecimal("2.00"), actual);
    }

    @Test
    public void makeChange_returns_1_65_and_coin_quantities() {
        sut.setMachineBalance(new BigDecimal("1.65"));
        BigDecimal changeDue = sut.getMachineBalance();
        String actual = sut.makeChange();


        String expected = String.format("Dollar bills: %s%nQuarters : %s%nDimes : %s%nNickels : %s%nTotal change returned: $%s%n",
                "1","2","1","1",changeDue);

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void makeChange_returns_5_35_and_coin_quantities() {
        sut.setMachineBalance(new BigDecimal("5.35"));
        BigDecimal changeDue = sut.getMachineBalance();
        String actual = sut.makeChange();


        String expected = String.format("Dollar bills: %s%nQuarters : %s%nDimes : %s%nNickels : %s%nTotal change returned: $%s%n",
                "5","1","1","0",changeDue);

        Assert.assertEquals(expected,actual);
    }

}