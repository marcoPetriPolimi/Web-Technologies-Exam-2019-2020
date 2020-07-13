package it.polimi.tiw.ria.utils;

import it.polimi.tiw.ria.beans.AddressBook;

import java.util.ResourceBundle;

public class AddressBookMessage extends GeneralMessage {
	private AddressBook addressBook;

	public AddressBookMessage(ResourceBundle language) {
		super(language);
	}
	public AddressBookMessage(ResourceBundle language, AddressBook addressBook) {
		super(language);
		this.addressBook = addressBook;
	}
}
