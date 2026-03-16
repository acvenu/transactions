package com.iispl.service.SettlementEngine;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.iispl.connectionpool.ConnectionPool;
import com.iispl.service.SettlementData;

public class SettlementEngine {

	public void generateSettlement(String batchId) {

	    String sql = "SELECT sender_bank, receiver_bank, amount FROM transactions WHERE batch_id = ?";

	    try (Connection con = ConnectionPool.getDataSource().getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, batchId);

	        try (ResultSet rs = ps.executeQuery()) {

	            Map<String, SettlementData> map = new HashMap<>();

	            while (rs.next()) {

	                String sender = rs.getString("sender_bank");
	                String receiver = rs.getString("receiver_bank");
	                BigDecimal amount = rs.getBigDecimal("amount");

	                SettlementData senderData = map.getOrDefault(sender, new SettlementData());
	                senderData.itemsDelivered++;
	                senderData.amountToPay = senderData.amountToPay.add(amount);
	                map.put(sender, senderData);

	                SettlementData receiverData = map.getOrDefault(receiver, new SettlementData());
	                receiverData.itemsReceived++;
	                receiverData.amountToReceive = receiverData.amountToReceive.add(amount);
	                map.put(receiver, receiverData);
	            }

	            insertSettlementSummary(con, batchId, map);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
    private void insertSettlementSummary(Connection con, String batchId, Map<String, SettlementData> map) throws Exception {

        String sql = "INSERT INTO settlement_summary "
                + "(batch_id, bank_code, items_delivered, amount_to_receive, items_received, amount_to_pay, net_amount) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        for (Map.Entry<String, SettlementData> entry : map.entrySet()) {

            String bankCode = entry.getKey();
            SettlementData data = entry.getValue();

            BigDecimal net = data.amountToReceive.subtract(data.amountToPay);

            ps.setString(1, batchId);
            ps.setString(2, bankCode);
            ps.setInt(3, data.itemsDelivered);
            ps.setBigDecimal(4, data.amountToReceive);
            ps.setInt(5, data.itemsReceived);
            ps.setBigDecimal(6, data.amountToPay);
            ps.setBigDecimal(7, net);

            ps.executeUpdate();
        }

        System.out.println("Settlement summary generated successfully!");
    }
}