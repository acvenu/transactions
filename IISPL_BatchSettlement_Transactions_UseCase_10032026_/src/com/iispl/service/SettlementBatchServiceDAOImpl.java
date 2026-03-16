package com.iispl.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import com.iispl.connectionpool.ConnectionPool;
import com.iispl.dao.SettlementBatchDAO;
import com.iispl.dao.SettlementBatchDAOImpl;
import com.iispl.dao.SettlementBatchServiceDAO;
import com.iispl.dao.TransactionDAO;
import com.iispl.dao.TransactionDAOImpl;
import com.iispl.entity.Builder;
import com.iispl.entity.SettlementBatch;
import com.iispl.entity.Transaction;
import com.iispl.service.SettlementEngine.SettlementEngine;


public class SettlementBatchServiceDAOImpl implements SettlementBatchServiceDAO {

    private SettlementBatchDAO batchDAO = new SettlementBatchDAOImpl();
    private TransactionDAO transactionDAO = new TransactionDAOImpl();

    private SettlementBatch lastCreatedBatch;

    // -----------------------------
    // Create ONE batch (first 5 transactions)
    // -----------------------------
    public void createBatch() {

        List<Transaction> transactions = transactionDAO.fetchFirst5Transactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions available for batching.");
            return;
        }

        String batchId = "BATCH-" + System.currentTimeMillis();

        Builder builder = new Builder(batchId, LocalDate.now());

        for (Transaction txn : transactions) {
            builder.add(txn);
        }

        SettlementBatch batch = builder.build();

        lastCreatedBatch = batch;

        System.out.println("\n==============================");
        System.out.println("Batch Created Successfully");
        System.out.println("Batch ID : " + batch.getBatchId());
        System.out.println("Batch Date : " + batch.getDate());
        System.out.println("Total Transactions : " + batch.getRecordCount());
        System.out.println("Total Amount : " + batch.getTotalAmount());
        System.out.println("==============================\n");

        // Save batch header
        batchDAO.saveBatch(batch);

        // Update transactions with batch_id
        for (Transaction txn : transactions) {
            transactionDAO.updateBatchId(txn.getTxnId(), batchId);
        }

        // Run settlement
        SettlementEngine engine = new SettlementEngine();
        engine.generateSettlement(batchId);

        // Print settlement report
        SettlementReport report = new SettlementReport();
        report.printReport(batchId);
    }

    // -----------------------------
    // Get last created batch
    // -----------------------------
    public SettlementBatch getLastCreatedBatch() {
        return lastCreatedBatch;
    }
    public void viewBatchTransactions(String batchId) {

        try {

            DataSource ds = ConnectionPool.getDataSource();
            Connection con = ds.getConnection();

            String sql = "SELECT txn_id, sender_bank, receiver_bank, channel, amount, status "
                       + "FROM transactions WHERE batch_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, batchId);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n============= Batch Transactions =============");

            System.out.printf("%-15s %-10s %-10s %-10s %-10s %-10s\n",
                    "Txn ID", "Sender", "Receiver", "Channel", "Amount", "Status");

            System.out.println("--------------------------------------------------------------");

            while (rs.next()) {

                System.out.printf("%-15s %-10s %-10s %-10s %-10.2f %-10s\n",
                        rs.getString("txn_id"),
                        rs.getString("sender_bank"),
                        rs.getString("receiver_bank"),
                        rs.getString("channel"),
                        rs.getDouble("amount"),
                        rs.getString("status"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
