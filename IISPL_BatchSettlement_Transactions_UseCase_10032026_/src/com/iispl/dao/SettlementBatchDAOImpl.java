package com.iispl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import com.iispl.connectionpool.ConnectionPool;
import com.iispl.entity.SettlementBatch;

public class SettlementBatchDAOImpl implements SettlementBatchDAO {

    private static final String INSERT_BATCH_SQL =
            "INSERT INTO settlement_batch(batch_id, batch_date) VALUES (?, ?)";

    // -----------------------------
    // Save batch information
    // -----------------------------
    @Override
    public void saveBatch(SettlementBatch batch) {

        if (batch == null) {
            throw new IllegalArgumentException("Batch cannot be null");
        }

        try (Connection con = ConnectionPool.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_BATCH_SQL)) {

            ps.setString(1, batch.getBatchId());
            ps.setDate(2, java.sql.Date.valueOf(batch.getDate()));

            ps.executeUpdate();

            System.out.println("Batch created successfully : " + batch.getBatchId());

        } catch (Exception e) {
            System.err.println("Error saving batch: " + e.getMessage());
            e.printStackTrace();
        }
    }
}