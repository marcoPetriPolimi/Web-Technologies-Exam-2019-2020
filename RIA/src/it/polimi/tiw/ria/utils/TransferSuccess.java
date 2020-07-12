package it.polimi.tiw.ria.utils;

import it.polimi.tiw.ria.beans.Account;

import java.util.ResourceBundle;

public class TransferSuccess extends GeneralMessage {
	private Account account;
	private Account recipientAccount;

	public TransferSuccess(ResourceBundle language) {
		super(language);
		this.account = null;
		this.recipientAccount = null;
	}
	public TransferSuccess(ResourceBundle language, Account account, Account recipientAccount) {
		super(language);
		this.account = account;
		this.recipientAccount = recipientAccount;
	}
}
