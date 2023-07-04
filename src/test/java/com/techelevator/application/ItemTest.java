package com.techelevator.application;

import com.techelevator.models.Gum;
import com.techelevator.models.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class ItemTest {

    private Item sut;

    @Before
    public void setup() {
        sut = new Gum("testGum", new BigDecimal("1.65"), "A1");
    }

    @Test
    public void isAvailable_returns_true_if_item_quantity_greater_than_0() {
        sut.setItemQuantity(1);
        boolean actual = sut.isAvailable();
        Assert.assertTrue(actual);
    }

    @Test
    public void isAvailable_returns_false_if_item_quantity_equals_0() {
        sut.setItemQuantity(0);
        boolean actual = sut.isAvailable();
        Assert.assertFalse(actual);
    }

    @Test
    public void isAvailable_returns_false_if_item_quantity_is_less_than_0() {
        sut.setItemQuantity(-1);
        boolean actual = sut.isAvailable();
        Assert.assertFalse(actual);
    }

}
