package com.iispl.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.iispl.connectionpool.ConnectionPool;

public class SettlementReport {

	public void printReport(String batchId) {

	    String sql = "SELECT m.bank_code, m.bank_name, m.zone, "
	            + "s.items_delivered, s.amount_to_receive, "
	            + "s.items_received, s.amount_to_pay, s.net_amount "
	            + "FROM settlement_summary s "
	            + "JOIN member_bank m ON s.bank_code = m.bank_code "
	            + "WHERE s.batch_id = ?";

	    try (Connection con = ConnectionPool.getDataSource().getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, batchId);

	        try (ResultSet rs = ps.executeQuery()) {

	            System.out.println("\n==============================================");
	            System.out.println("        CLEARING HOUSE SETTLEMENT REPORT      ");
	            System.out.println("Batch ID : " + batchId);
	            System.out.println("==============================================");

	            System.out.printf("%-6s %-25s %-5s %-10s %-12s %-10s %-10s %-10s\n",
	                    "Bank", "Bank Name", "Zone",
	                    "Delivered", "AmtReceive", "Received", "AmtPay", "CR/DR");

	            System.out.println("--------------------------------------------------------------------------------");

	            while (rs.next()) {

	                String bankCode = rs.getString("bank_code");
	                String bankName = rs.getString("bank_name");
	                int zone = rs.getInt("zone");

	                int delivered = rs.getInt("items_delivered");
	                int received = rs.getInt("items_received");

	                double amtReceive = rs.getDouble("amount_to_receive");
	                double amtPay = rs.getDouble("amount_to_pay");

	                double net = rs.getDouble("net_amount");

	                System.out.printf("%-6s %-25s %-5d %-10d %-12.2f %-10d %-10.2f %-10.2f\n",
	                        bankCode, bankName, zone,
	                        delivered, amtReceive, received, amtPay, net);
	            }

	            System.out.println("--------------------------------------------------------------------------------");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void printSettlementHistory() {

	    try {

	        DataSource ds = ConnectionPool.getDataSource();
	        Connection con = ds.getConnection();

	        String sql = "SELECT batch_id, bank_code, items_delivered, items_received, "
	                   + "amount_to_pay, amount_to_receive, net_amount "
	                   + "FROM settlement_summary ORDER BY batch_id";

	        PreparedStatement ps = con.prepareStatement(sql);

	        ResultSet rs = ps.executeQuery();

	        System.out.println("\n==============================================");
	        System.out.println("              SETTLEMENT HISTORY              ");
	        System.out.println("==============================================");

	        System.out.printf("%-15s %-6s %-10s %-10s %-12s %-15s %-10s\n",
	                "Batch ID", "Bank", "Delivered", "Received",
	                "Amt Pay", "Amt Receive", "Net");

	        System.out.println("--------------------------------------------------------------------------");

	        while (rs.next()) {

	            System.out.printf("%-15s %-6s %-10d %-10d %-12.2f %-15.2f %-10.2f\n",
	                    rs.getString("batch_id"),
	                    rs.getString("bank_code"),
	                    rs.getInt("items_delivered"),
	                    rs.getInt("items_received"),
	                    rs.getDouble("amount_to_pay"),
	                    rs.getDouble("amount_to_receive"),
	                    rs.getDouble("net_amount"));
	        }

	        System.out.println("--------------------------------------------------------------------------");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void printDailySettlementSummary(String date) {

	    try {

	        DataSource ds = ConnectionPool.getDataSource();
	        Connection con = ds.getConnection();

	        String sql = "SELECT COUNT(DISTINCT batch_id) AS total_batches, "
	                   + "COUNT(*) AS total_records, "
	                   + "SUM(net_amount) AS total_amount "
	                   + "FROM settlement_summary s "
	                   + "JOIN settlement_batch b ON s.batch_id = b.batch_id "
	                   + "WHERE b.batch_date = ?";

	        PreparedStatement ps = con.prepareStatement(sql);

	        ps.setDate(1, java.sql.Date.valueOf(date));

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {

	            System.out.println("\n=========================================");
	            System.out.println("        DAILY SETTLEMENT SUMMARY");
	            System.out.println("=========================================");

	            System.out.println("Date : " + date);
	            System.out.println("Total Batches        : " + rs.getInt("total_batches"));
	            System.out.println("Total Records        : " + rs.getInt("total_records"));
	            System.out.println("Total Settlement Amt : " + rs.getDouble("total_amount"));

	            System.out.println("=========================================");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}