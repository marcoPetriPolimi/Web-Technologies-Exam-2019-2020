package it.polimi.tiw.pure_html.beans;

import java.sql.Timestamp;

public class Transfer {
	private final int id;
	private final Account outgoing;
	private final Account ingoing;
	private final Timestamp datetime;
	private final int amount;

	public Transfer(int id, Account outgoing, Account ingoing, Timestamp datetime, int amount) {
		this.id = id;
		this.outgoing = outgoing;
		this.ingoing = ingoing;
		this.datetime = datetime;
		this.amount = amount;
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
}
