package it.polimi.tiw.pure_html.dao;

import it.polimi.tiw.pure_html.beans.Account;
import it.polimi.tiw.pure_html.beans.Transfer;
import it.polimi.tiw.pure_html.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends GeneralDAO {
	public AccountDAO(Connection conn) {
		super(conn);
	}
	public AccountDAO(Connection conn, String language, String country) {
		super(conn,language,country);
	}

	/* **************************
	 *							*
	 * 		MAIN METHODS		*
	 * 							*
	 ****************************/
	public Account findAccount(int accountCode) throws IllegalArgumentException, SQLException {
		/* used variables */
		String query;
		PreparedStatement preparedStatement;
		ResultSet result;
		User owner;
		UserDAO userDAO = new UserDAO(conn);

		/* exception handling */
		if (accountCode < 0) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbAccountInvalidCode"));
		}

		/* main code */
		query = "SELECT * FROM Account WHERE Id=?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1,accountCode);
		result = preparedStatement.executeQuery();
		if (result.first()) {
			owner = userDAO.findUser(result.getInt("Owner"));
			return new Account(result.getInt("Id"),owner,result.getInt("Balance"));
		} else {
			return null;
		}
	}

	public List<Transfer> findTransfers(int accountCode, boolean outgoing) throws IllegalArgumentException, SQLException {
		/* used variables */
		String query;
		PreparedStatement preparedStatement;
		ResultSet outTransfers;
		List<Transfer> transfersList;
		Account account, otherAccount;

		/* exception handling */
		account = findAccount(accountCode);
		if (account == null) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbAccountNotExist"));
		}

		/* main code */
		if (outgoing) {
			query = "SELECT * FROM Transfer WHERE OutgoingAccount=?";
		} else {
			query = "SELECT * FROM Transfer WHERE IngoingAccount=?";
		}
		transfersList = new ArrayList<>();
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1,accountCode);
		outTransfers = preparedStatement.executeQuery();
		while (outTransfers.next()) {
			if (outgoing) {
				otherAccount = findAccount(outTransfers.getInt("IngoingAccount"));
				transfersList.add(new Transfer(outTransfers.getInt("Id"),account,otherAccount,outTransfers.getTimestamp("Date"),outTransfers.getInt("Amount"),outTransfers.getString("Reason")));
			} else {
				otherAccount = findAccount(outTransfers.getInt("OutgoingAccount"));
				transfersList.add(new Transfer(outTransfers.getInt("Id"),otherAccount,account,outTransfers.getTimestamp("Date"),outTransfers.getInt("Amount"),outTransfers.getString("Reason")));
			}
		}
		return transfersList;
	}

	public void changeAmount(int accountCode, int amount, boolean add) throws IllegalArgumentException, SQLException {
		/* used variables */
		String query;
		PreparedStatement preparedStatement;
		Account account;
		int newBalance;

		/* exception handling */
		account = findAccount(accountCode);
		if (account == null) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbAccountNotExist"));
		}

		/* main code */
		query = "UPDATE Account SET Balance=? WHERE Id=?";
		if (add) {
			newBalance = account.getBalance()+amount;
		} else {
			newBalance = account.getBalance()-amount;
		}
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1,newBalance);
		preparedStatement.setInt(2,accountCode);
		preparedStatement.executeUpdate();
	}
}
