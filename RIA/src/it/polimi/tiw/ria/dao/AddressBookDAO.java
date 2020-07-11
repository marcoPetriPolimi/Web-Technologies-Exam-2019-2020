package it.polimi.tiw.ria.dao;

import it.polimi.tiw.ria.beans.AddressBook;
import it.polimi.tiw.ria.beans.Recipient;
import it.polimi.tiw.ria.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDAO extends GeneralDAO {
	public AddressBookDAO(Connection conn) {
		super(conn);
	}
	public AddressBookDAO(Connection conn, String language, String country) {
		super(conn,language,country);
	}

	public AddressBook findAddressBook(int addressBookId) throws IllegalArgumentException, SQLException {
		/* used variables */
		String query;
		PreparedStatement preparedStatement;
		ResultSet queryResult;
		AddressBook addressBook = null;
		UserDAO userDAO = new UserDAO(conn,selectedLanguage.getLocale().getLanguage(),selectedLanguage.getLocale().getCountry());

		/* exception handling */
		if (addressBookId < 0) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbAddressBookNotExist"));
		}

		/* main code */
		query = "SELECT * FROM AddressBook WHERE Id=?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1,addressBookId);
		queryResult = preparedStatement.executeQuery();
		if (queryResult.first()) {
			addressBook = new AddressBook(queryResult.getInt("Id"),userDAO.findUser(queryResult.getInt("Owner")));
		}
		return addressBook;
	}
	public AddressBook findAddressBook(User user) throws IllegalArgumentException, SQLException {
		/* used variables */
		String query;
		PreparedStatement preparedStatement;
		ResultSet queryResult;
		AddressBook addressBook = null;
		UserDAO userDAO = new UserDAO(conn,selectedLanguage.getLocale().getLanguage(),selectedLanguage.getLocale().getCountry());

		/* exception handling */
		if (user == null) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbAddressBookInvalidUserObject"));
		} else {
			user = userDAO.findUser(user.getCode());
			if (user == null) {
				throw new IllegalArgumentException(selectedLanguage.getString("dbAddressBookInvalidUserObject"));
			}
		}

		/* main code */
		query = "SELECT * FROM AddressBook WHERE Owner=?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1,user.getCode());
		queryResult = preparedStatement.executeQuery();
		if (queryResult.first()) {
			addressBook = new AddressBook(queryResult.getInt("Id"),userDAO.findUser(user.getCode()));
		}
		return addressBook;
	}

	public List<Recipient> findRecipients(AddressBook addressBook) throws IllegalArgumentException, SQLException {
		/* used variables */
		String query;
		PreparedStatement preparedStatement;
		ResultSet queryResult;
		List<Recipient> recipientList;

		/* exception handling */
		if (addressBook == null) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbAddressBookWrongParams"));
		}

		/* main code */
		recipientList = new ArrayList<>();
		query = "SELECT * FROM Recipient WHERE AddressBook=?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1,addressBook.getAddressBookId());
		queryResult = preparedStatement.executeQuery();
		while (queryResult.next()) {
			Recipient rec = new Recipient(queryResult.getInt("Id"),addressBook,queryResult.getInt("AccountId"),queryResult.getInt("RecipientId"));
			recipientList.add(rec);
		}
		return recipientList;
	}

	public void createAddressBook(int ownerCode) throws IllegalArgumentException, SQLException {
		/* used variables */
		String query;
		PreparedStatement preparedStatement;
		ResultSet queryResult;
		AddressBook addressBook;
		User user;
		UserDAO userDAO = new UserDAO(conn,selectedLanguage.getLocale().getLanguage(),selectedLanguage.getLocale().getCountry());

		/* exception handling */
		if (ownerCode < 0) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbAddressBookWrongParams"));
		} else {
			user = userDAO.findUser(ownerCode);
			if (user == null) {
				throw new IllegalArgumentException(selectedLanguage.getString("dbAddressBookUserNotExist"));
			}

			addressBook = findAddressBook(user);
			if (addressBook != null) {
				throw new IllegalArgumentException(selectedLanguage.getString("dbAddressBookAlreadyExist"));
			}
		}

		/* main code */
		query = "INSERT INTO AddressBook(Owner) VALUES (?)";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1,user.getCode());
		preparedStatement.executeUpdate();
	}
}
