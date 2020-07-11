package it.polimi.tiw.ria.dao;

import it.polimi.tiw.ria.beans.Account;
import it.polimi.tiw.ria.beans.AddressBook;
import it.polimi.tiw.ria.beans.Recipient;
import it.polimi.tiw.ria.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipientDAO extends GeneralDAO {
	public RecipientDAO(Connection conn) {
		super(conn);
	}
	public RecipientDAO(Connection conn, String language, String country) {
		super(conn,language,country);
	}

	public Recipient findRecipient(int recipientId) throws IllegalArgumentException, SQLException {
		/* used variables */
		String query;
		PreparedStatement preparedStatement;
		ResultSet queryResult;
		Recipient recipient = null;
		AddressBookDAO addressBookDAO = new AddressBookDAO(conn,selectedLanguage.getLocale().getLanguage(),selectedLanguage.getLocale().getCountry());

		/* exception handling */
		if (recipientId < 0) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbRecipientInvalidCode"));
		}

		/* main code */
		query = "SELECT * FROM Recipient WHERE Id=?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1,recipientId);
		queryResult = preparedStatement.executeQuery();
		if (queryResult.first()) {
			recipient = new Recipient(queryResult.getInt("Id"),addressBookDAO.findAddressBook(queryResult.getInt("AddressBook")),queryResult.getInt("AccountId"),queryResult.getInt("RecipientId"));
		}
		return recipient;
	}

	public void createRecipient(int addressBookId, int accountId, int recipientId) throws IllegalArgumentException, SQLException {
		/* used variables */
		String query;
		PreparedStatement preparedStatement;
		ResultSet queryResult;
		AddressBook addressBook;
		Account account;
		User user;
		AddressBookDAO addressBookDAO = new AddressBookDAO(conn,selectedLanguage.getLocale().getLanguage(),selectedLanguage.getLocale().getCountry());
		AccountDAO accountDAO = new AccountDAO(conn,selectedLanguage.getLocale().getLanguage(),selectedLanguage.getLocale().getCountry());
		UserDAO userDAO = new UserDAO(conn,selectedLanguage.getLocale().getLanguage(),selectedLanguage.getLocale().getCountry());

		/* exception handling */
		if (addressBookId < 0 || accountId < 0 || recipientId < 0) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbRecipientWrongParams"));
		} else {
			addressBook = addressBookDAO.findAddressBook(addressBookId);
			if (addressBook == null) {
				throw new IllegalArgumentException(selectedLanguage.getString("dbRecipientWrongParams"));
			}

			account = accountDAO.findAccount(accountId);
			if (account == null) {
				throw new IllegalArgumentException(selectedLanguage.getString("dbRecipientWrongParams"));
			}

			user = userDAO.findUser(recipientId);
			if (user == null) {
				throw new IllegalArgumentException(selectedLanguage.getString("dbRecipientWrongParams"));
			}
		}

		/* main code */
		query = "INSERT INTO Recipient(AddressBook,RecipientId,AccountId) VALUES (?,?,?)";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1,addressBookId);
		preparedStatement.setInt(1,accountId);
		preparedStatement.setInt(1,recipientId);
		preparedStatement.executeUpdate();
	}
}
