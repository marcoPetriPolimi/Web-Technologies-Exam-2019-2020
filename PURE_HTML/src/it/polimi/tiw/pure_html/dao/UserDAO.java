package it.polimi.tiw.pure_html.dao;

import it.polimi.tiw.pure_html.beans.Account;
import it.polimi.tiw.pure_html.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends GeneralDAO {
	public UserDAO(Connection conn) {
		super(conn);
	}
	public UserDAO(Connection conn, String language, String country) {
		super(conn,language,country);
	}

	/* **************************
	 *							*
	 * 		MAIN METHODS		*
	 * 							*
	 ****************************/
	public User findUser(int code) throws IllegalArgumentException, SQLException {
		/* used variables */
		String query;
		PreparedStatement preparedStatement;
		ResultSet result;

		/* exception handling */
		if (code < 0) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbUserInvalidCode"));
		}

		/* main code */
		query = "SELECT * FROM User WHERE Id=?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1,code);
		result = preparedStatement.executeQuery();
		if (result.first()) {
			return new User(result.getInt("Id"),result.getString("Name"),result.getString("Password"));
		} else {
			return null;
		}
	}

	public List<Account> findAccounts(int userCode) throws IllegalArgumentException, SQLException {
		/* used variables */
		String accountQuery;
		PreparedStatement accountStatement;
		ResultSet accountResult;
		User owner;
		List<Account> accountsList;

		/* exception handling */
		if (userCode < 0) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbUserInvalidCode"));
		} else {
			owner = findUser(userCode);
			if (owner == null) {
				throw new IllegalArgumentException(selectedLanguage.getString("dbUserNotExist"));
			}
		}

		/* main code */
		accountsList = new ArrayList<>();
		accountQuery = "SELECT * FROM Account WHERE Owner=?";
		accountStatement = conn.prepareStatement(accountQuery);
		accountStatement.setInt(1,userCode);
		accountResult = accountStatement.executeQuery();
		while (accountResult.next()) {
			accountsList.add(new Account(accountResult.getInt("Id"),owner,accountResult.getInt("Balance")));
		}
		return accountsList;
	}
}
