package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.iispl.enums.Status;

public final class Builder {

    private static final int MAX_BATCH_SIZE = 5;

    private final String batchId;
    private final LocalDate date;

    private final List<Transaction> transactions = new ArrayList<>();
    private final Set<String> txnIds = new HashSet<>();

    private BigDecimal runningTotal = BigDecimal.ZERO;

    public Builder(String batchId, LocalDate date) {
        this.batchId = batchId;
        this.date = date;
    }

    public Builder add(Transaction txn) {

        if (txn == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }

        // Prevent FAILED transactions
        if (txn.getStatus() == Status.FAILED) {
            throw new IllegalArgumentException("FAILED transactions cannot be added to batch");
        }

        // Prevent duplicate transaction IDs
        if (!txnIds.add(txn.getTxnId())) {
            throw new IllegalArgumentException("Duplicate txnId: " + txn.getTxnId());
        }

        // Enforce batch size limit
        if (transactions.size() >= MAX_BATCH_SIZE) {
            throw new IllegalStateException("Batch size cannot exceed " + MAX_BATCH_SIZE);
        }

        transactions.add(txn);

        runningTotal = runningTotal.add(txn.getAmount());

        return this;
    }

    public SettlementBatch build() {

        if (transactions.isEmpty()) {
            throw new IllegalStateException("Cannot create batch with no transactions");
        }

        return new SettlementBatch(batchId, date, transactions);
    }

    public int previewRecordCount() {
        return transactions.size();
    }

    public BigDecimal previewTotalAmount() {
        return runningTotal;
    }
}