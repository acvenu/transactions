package com.iispl.dao;

import java.util.List;

import com.iispl.dao.SettlementBatchDAO;
import com.iispl.dao.TransactionDAO;
import com.iispl.entity.SettlementBatch;
import com.iispl.entity.Transaction;

public interface SettlementBatchServiceDAO {
	
	 public void createBatch();
	 public void viewBatchTransactions(String batchId);
	 
}