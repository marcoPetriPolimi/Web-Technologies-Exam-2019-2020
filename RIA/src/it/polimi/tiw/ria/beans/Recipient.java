package it.polimi.tiw.ria.beans;

public class Recipient {
	private final int recipientCode;
	private final AddressBook addressBook;
	private final int accountId;
	private final int userId;

	public Recipient(int recipientCode, AddressBook addressBook, int accountId, int userId) {
		this.recipientCode = recipientCode;
		this.addressBook = addressBook;
		this.accountId = accountId;
		this.userId = userId;
	}
	public Recipient(int recipientCode, int accountId, int userId) {
		this.recipientCode = recipientCode;
		this.addressBook = null;
		this.accountId = accountId;
		this.userId = userId;
	}
	private Recipient(Recipient other, AddressBook addressBook) {
		this.recipientCode = other.getRecipientCode();
		this.addressBook = addressBook;
		this.accountId = other.getAccountId();
		this.userId = other.getUserId();
	}

	public int getRecipientCode() {
		return recipientCode;
	}
	public AddressBook getAddressBook() {
		return addressBook;
	}
	public int getAccountId() {
		return accountId;
	}
	public int getUserId() {
		return userId;
	}

	public Recipient setAddressBook(AddressBook addressBook) {
		return new Recipient(this,addressBook);
	}
}
