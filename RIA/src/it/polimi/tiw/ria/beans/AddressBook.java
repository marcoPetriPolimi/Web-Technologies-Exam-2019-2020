package it.polimi.tiw.ria.beans;

import java.util.ArrayList;
import java.util.List;

public class AddressBook {
	private final int addressBookId;
	private final User owner;
	private final List<Recipient> recipientList;

	public AddressBook(int addressBookId, User owner, List<Recipient> recipientList) {
		this.addressBookId = addressBookId;
		this.owner = owner;
		this.recipientList = new ArrayList<>(recipientList);
	}
	public AddressBook(int addressBookId, User owner) {
		this.addressBookId = addressBookId;
		this.owner = owner;
		this.recipientList = new ArrayList<>();
	}
	private AddressBook(AddressBook other, List<Recipient> recipientList) {
		this.addressBookId = other.getAddressBookId();
		this.owner = other.getOwner();
		this.recipientList = new ArrayList<>(recipientList);
	}

	public int getAddressBookId() {
		return addressBookId;
	}
	public User getOwner() {
		return owner;
	}
	public List<Recipient> getRecipientList() {
		return new ArrayList<>(recipientList);
	}

	public AddressBook setRecipients(List<Recipient> recipientList) {
		return new AddressBook(this,recipientList);
	}
}
