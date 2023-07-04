package com.techelevator.utilities;

import com.techelevator.application.VendingMachine;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyHandler {

    private final BigDecimal DISCOUNT = new BigDecimal("1.00");
    private BigDecimal machineBalance = new BigDecimal("0.00");
    private BigDecimal changeDue = new BigDecimal("0.00");

    private final BigDecimal NICKEL_COIN = new BigDecimal("0.05");
    private final BigDecimal DIME_COIN = new BigDecimal("0.10");
    private final BigDecimal QUARTER_COIN = new BigDecimal("0.25");
    private final BigDecimal ONE_DOLLAR_BILL = new BigDecimal("1.00");
    private final BigDecimal FIVE_DOLLAR_BILL = new BigDecimal("5.00");
    private final BigDecimal TEN_DOLLAR_BILL = new BigDecimal("10.00");
    private final BigDecimal TWENTY_DOLLAR_BILL = new BigDecimal("20.00");

    //Map<String,Integer> wallet = new HashMap<>();

    public MoneyHandler() {

    }

    public BigDecimal getDISCOUNT() {
        return DISCOUNT;
    }

    public BigDecimal getNICKEL_COIN() {
        return NICKEL_COIN;
    }

    public BigDecimal getDIME_COIN() {
        return DIME_COIN;
    }

    public BigDecimal getQUARTER_COIN() {
        return QUARTER_COIN;
    }

    public BigDecimal getONE_DOLLAR_BILL() {
        return ONE_DOLLAR_BILL;
    }

    public BigDecimal getFIVE_DOLLAR_BILL() {
        return FIVE_DOLLAR_BILL;
    }

    public BigDecimal getTEN_DOLLAR_BILL() {
        return TEN_DOLLAR_BILL;
    }

    public BigDecimal getTWENTY_DOLLAR_BILL() {
        return TWENTY_DOLLAR_BILL;
    }

    public BigDecimal getMachineBalance() {
        return machineBalance;
    }

    public void setMachineBalance(BigDecimal machineBalance) {
        this.machineBalance = machineBalance;
    }

    public BigDecimal getChangeDue() {
        return this.changeDue;
    }

    public void setChangeDue(BigDecimal changeDue) {
        this.changeDue = changeDue;
    }

    public void addFunds(BigDecimal amount){
        machineBalance =  machineBalance.add(amount);
        machineBalance = machineBalance.setScale(2, RoundingMode.HALF_UP);
    }

    public void removeFunds(BigDecimal amount ){
        if (getMachineBalance().compareTo(amount) < 0) {
            setMachineBalance(new BigDecimal("0.00"));
        } else {
            machineBalance = machineBalance.subtract(amount);
            machineBalance = machineBalance.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public String makeChange() {
        this.changeDue = machineBalance;
        int nickelQuantity = 0;
        int dimeQuantity = 0;
        int quarterQuantity = 0;
        int oneDollarBillQuantity = 0;
        int fiveDollarBillQuantity;
        int tenDollarBillQuantity;
        int twentyDollarBillQuantity;

        while(machineBalance.compareTo(BigDecimal.ZERO)>0) {

            if(machineBalance.compareTo(ONE_DOLLAR_BILL)>-1) {
                removeFunds(ONE_DOLLAR_BILL);
                oneDollarBillQuantity++;
            } else if(machineBalance.compareTo(QUARTER_COIN)>-1) {
                 removeFunds(QUARTER_COIN);
                quarterQuantity++;
            } else if(machineBalance.compareTo(DIME_COIN)>-1) {
                 removeFunds(DIME_COIN);
                dimeQuantity++;
            } else if(machineBalance.compareTo(NICKEL_COIN)>-1) {
                 removeFunds(NICKEL_COIN);
                nickelQuantity++;
            }
        }

        VendingMachine.setTransactionCounter(1);
        System.out.println();
        return String.format("Dollar bills: %s%nQuarters : %s%nDimes : %s%nNickels : %s%nTotal change returned: $%s%n",
                oneDollarBillQuantity,quarterQuantity,dimeQuantity,nickelQuantity,changeDue);

    }

}
