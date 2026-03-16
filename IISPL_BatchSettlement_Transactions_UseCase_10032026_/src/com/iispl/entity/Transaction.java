package com.iispl.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import com.iispl.enums.Channel;
import com.iispl.enums.CreditOrDebit;
import com.iispl.enums.Status;

public final class Transaction {

	private final String txnId;
	private final String senderBank;
	private final String receiverBank;
	private final Channel channel;
	private final BigDecimal amount;
	private final Instant txnTime;
	private final CreditOrDebit creditordebit;
	private final Status status;

	public Transaction(String txnId, String senderBank, String receiverBank, Channel channel, BigDecimal amount,
			Instant txnTime, CreditOrDebit creditordebit, Status status) {

		this.txnId = requireNonBlank(txnId, "txnId");
		this.senderBank = requireNonBlank(senderBank, "senderBank");
		this.receiverBank = requireNonBlank(receiverBank, "receiverBank");
		this.channel = Objects.requireNonNull(channel, "channel");
		this.amount = requireNonNullAmount(amount, "amount");
		this.txnTime = Objects.requireNonNull(txnTime, "txnTime");
		this.creditordebit = Objects.requireNonNull(creditordebit, "drCr");
		this.status = Objects.requireNonNull(status, "status");
	}

	public String getTxnId() {
		return txnId;
	}

	public String getSenderBank() {
		return senderBank;
	}

	public String getReceiverBank() {
		return receiverBank;
	}

	public Channel getChannel() {
		return channel;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Instant getTxnTime() {
		return txnTime;
	}

	public CreditOrDebit getDrCr() {
		return creditordebit;
	}

	public Status getStatus() {
		return status;
	}

	private static String requireNonBlank(String value, String field) {
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException(field + " must not be blank");
		}
		return value.trim();
	}

	private static BigDecimal requireNonNullAmount(BigDecimal value, String field) {
		Objects.requireNonNull(value, field);

		if (value.signum() <= 0) {
			throw new IllegalArgumentException(field + " must be greater than 0");
		}

		return value;
	}
}