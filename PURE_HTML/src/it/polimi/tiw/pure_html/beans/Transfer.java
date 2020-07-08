package it.polimi.tiw.pure_html.beans;

import java.sql.Timestamp;

public class Transfer {
	private final int id;
	private final Account outgoing;
	private final Account ingoing;
	private final Timestamp datetime;
	private final int amount;
	private final String reason;

	public Transfer(int id, Account outgoing, Account ingoing, Timestamp datetime, int amount, String reason) {
		this.id = id;
		this.outgoing = outgoing;
		this.ingoing = ingoing;
		this.datetime = datetime;
		this.amount = amount;
		this.reason = reason;
	}

	public int getId() {
		return id;
	}
	public Account getOutgoing() {
		return outgoing;
	}
	public Account getIngoing() {
		return ingoing;
	}
	public Timestamp getDatetime() {
		return datetime;
	}
	public int getAmount() {
		return amount;
	}
	public String getReason() {
		return reason;
	}
}
