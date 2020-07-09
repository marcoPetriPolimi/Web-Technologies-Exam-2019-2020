package it.polimi.tiw.pure_html.beans;

public class Account {
	private final int code;
	private final User owner;
	private final int balance;

	public Account(int code, User owner, int balance) {
		this.code = code;
		this.owner = owner;
		this.balance = balance;
	}

	public int getCode() {
		return code;
	}
	public User getOwner() {
		return owner;
	}
	public int getBalance() {
		return balance;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Account) {
			Account other = (Account) obj;
			return code == other.code;
		} else {
			return false;
		}
	}
}
