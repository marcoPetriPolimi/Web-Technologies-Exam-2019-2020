package it.polimi.tiw.ria.utils;

import it.polimi.tiw.ria.beans.Account;
import it.polimi.tiw.ria.beans.AddressBook;
import it.polimi.tiw.ria.beans.Transfer;

import java.util.List;
import java.util.ResourceBundle;

public class AccountMessage extends GeneralMessage {
	private Account accountInfo;
	private List<Transfer> ingoingTransfers;
	private List<Transfer> outgoingTransfers;
	private int ingoingPages;
	private int outgoingPages;
	private AddressBook addressBook;

	public AccountMessage(ResourceBundle language) {
		super(language);
	}
	public AccountMessage(ResourceBundle language, Account accountInfo, List<Transfer> ingoing, List<Transfer> outgoing, int ingoingPages, int outgoingPages, AddressBook addressBook) {
		super(language);
		this.accountInfo = accountInfo;
		this.ingoingTransfers = ingoing;
		this.outgoingTransfers = outgoing;
		this.ingoingPages = ingoingPages;
		this.outgoingPages = outgoingPages;
		this.addressBook = addressBook;
	}
}
