package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public final class SettlementBatch {

    private final String batchId;
    private final LocalDate date;
    private final List<Transaction> transactions;

    public SettlementBatch(String batchId, LocalDate date, List<Transaction> transactions) {

        this.batchId = batchId;
        this.date = date;
        this.transactions = List.copyOf(transactions);

        validateUniqueTxnIds(this.transactions);
    }

   

	public String getBatchId() {
        return batchId;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public int getRecordCount() {
        return transactions.size();
    }

    public BigDecimal getTotalAmount() {

        BigDecimal total = BigDecimal.ZERO;

        for(Transaction t : transactions) {
            total = total.add(t.getAmount());
        }

        return total;
    }

    private void validateUniqueTxnIds(List<Transaction> txns) {

        Set<String> set = new HashSet<>();

        for(Transaction t : txns) {

            if(!set.add(t.getTxnId())) {
                throw new IllegalArgumentException("Duplicate txnId: " + t.getTxnId());
            }
        }
    }
    public static Builder builder(String batchId, LocalDate date) {
        return new Builder(batchId, date);
    }
}