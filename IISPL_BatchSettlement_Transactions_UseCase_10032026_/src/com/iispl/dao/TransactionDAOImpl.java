package com.iispl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.iispl.connectionpool.ConnectionPool;
import com.iispl.entity.Transaction;
import com.iispl.enums.Channel;
import com.iispl.enums.CreditOrDebit;
import com.iispl.enums.Status;

public class TransactionDAOImpl implements TransactionDAO{

    // -----------------------------
    // Save transaction (not used for batch creation normally)
    // -----------------------------
    public void saveTransaction(Transaction txn, String batchId) {

        String sql = "INSERT INTO transactions(txn_id, sender_bank, receiver_bank, channel, amount, txn_time, status, batch_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            DataSource ds = ConnectionPool.getDataSource();

            try (Connection con = ds.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, txn.getTxnId());
                ps.setString(2, txn.getSenderBank());
                ps.setString(3, txn.getReceiverBank());
                ps.setString(4, txn.getChannel().name());
                ps.setBigDecimal(5, txn.getAmount());
                ps.setTimestamp(6, java.sql.Timestamp.from(txn.getTxnTime()));
                ps.setString(7, txn.getStatus().name());
                ps.setString(8, batchId);

                ps.executeUpdate();

                System.out.println("Transaction saved successfully");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------
    // Fetch FIRST 5 SUCCESS transactions
    // -----------------------------
    public List<Transaction> fetchFirst5Transactions() {

        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT txn_id, sender_bank, receiver_bank, channel, amount, txn_time, status "
                   + "FROM transactions "
                   + "WHERE batch_id IS NULL AND status='SUCCESS' "
                   + "ORDER BY txn_time "
                   + "LIMIT 5";

        try (Connection con = ConnectionPool.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Transaction txn = new Transaction(
                        rs.getString("txn_id"),
                        rs.getString("sender_bank"),
                        rs.getString("receiver_bank"),
                        Channel.valueOf(rs.getString("channel")),
                        rs.getBigDecimal("amount"),
                        rs.getTimestamp("txn_time").toInstant(),
                        CreditOrDebit.DEBIT,
                        Status.valueOf(rs.getString("status"))
                );

                transactions.add(txn);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }
    // -----------------------------
    // Update batch id after batch creation
    // -----------------------------
    public void updateBatchId(String txnId, String batchId) {

        String sql = "UPDATE transactions SET batch_id = ? WHERE txn_id = ?";

        try (Connection con = ConnectionPool.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, batchId);
            ps.setString(2, txnId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}