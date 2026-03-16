package com.iispl.dao;

import java.util.List;

import com.iispl.entity.Transaction;

public interface TransactionDAO {
	
	public void saveTransaction(Transaction txn, String batchId);
	
	  public List<Transaction> fetchFirst5Transactions();
	  
	  public void updateBatchId(String txnId, String batchId);
	
}
