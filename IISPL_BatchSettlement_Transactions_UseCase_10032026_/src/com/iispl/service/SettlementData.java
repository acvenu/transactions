package com.iispl.service;

import java.math.BigDecimal;

public class SettlementData {

    public int itemsDelivered = 0;
    public int itemsReceived = 0;

    public BigDecimal amountToReceive = BigDecimal.ZERO;
    public BigDecimal amountToPay = BigDecimal.ZERO;

    public int getItemsDelivered() {
        return itemsDelivered;
    }

    public int getItemsReceived() {
        return itemsReceived;
    }

    public BigDecimal getAmountToReceive() {
        return amountToReceive;
    }

    public BigDecimal getAmountToPay() {
        return amountToPay;
    }

    // --------- Update Methods ---------

    public void addDelivered(BigDecimal amount) {
        itemsDelivered++;
        amountToPay = amountToPay.add(amount);
    }

    public void addReceived(BigDecimal amount) {
        itemsReceived++;
        amountToReceive = amountToReceive.add(amount);
    }
}