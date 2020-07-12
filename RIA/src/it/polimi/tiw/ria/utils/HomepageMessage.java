package it.polimi.tiw.ria.utils;

import it.polimi.tiw.ria.beans.Account;
import it.polimi.tiw.ria.beans.UserSecurity;

import java.util.List;
import java.util.ResourceBundle;

public class HomepageMessage extends GeneralMessage {
	private UserSecurity userInfo;
	private List<Account> accounts;

	public HomepageMessage(ResourceBundle language) {
		super(language);
	}
	public HomepageMessage(ResourceBundle language, UserSecurity userInfo, List<Account> accounts) {
		super(language);
		this.userInfo = userInfo;
		this.accounts = accounts;
	}
}
