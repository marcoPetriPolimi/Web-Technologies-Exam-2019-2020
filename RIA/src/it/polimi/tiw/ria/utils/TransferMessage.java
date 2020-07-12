package it.polimi.tiw.ria.utils;

import it.polimi.tiw.ria.beans.Transfer;

import java.util.List;
import java.util.ResourceBundle;

public class TransferMessage extends GeneralMessage {
	private List<Transfer> transfers;
	private int transferPages;
	private int transferPage;

	public TransferMessage(ResourceBundle language) {
		super(language);
	}
	public TransferMessage(ResourceBundle language, List<Transfer> transfers, int transferPages, int transferPage) {
		super(language);
		this.transfers = transfers;
		this.transferPages = transferPages;
		this.transferPage = transferPage;
	}
}