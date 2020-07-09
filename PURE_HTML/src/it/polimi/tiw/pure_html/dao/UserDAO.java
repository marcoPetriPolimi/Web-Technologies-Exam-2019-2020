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

	public void createUser(String name, String password) throws IllegalArgumentException, SQLException {
		/* used variables */
		String userQuery;
		PreparedStatement userPreparedStatement;

		/* exception handling */
		if (name.length() > 30 || password.length() > 30) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbUserWrongParams"));
		}

		/* main code */
		userQuery = "INSERT INTO User(Name,Password) VALUES (?,?)";
		userPreparedStatement = conn.prepareStatement(userQuery);
		userPreparedStatement.setString(1,name);
		userPreparedStatement.setString(2,password);
		userPreparedStatement.executeUpdate();
	}

	public void modifyPassword(int userCode, String password) throws IllegalArgumentException, SQLException {
		/* used variables */
		String userQuery;
		PreparedStatement userPreparedStatement;
		ResultSet userResult;
		User user;

		/* exception handling */
		if (password.length() > 30) {
			throw new IllegalArgumentException(selectedLanguage.getString("dbUserWrongParams"));
		} else {
			user = findUser(userCode);
			if (user == null) {
				throw new IllegalArgumentException(selectedLanguage.getString("dbUserNotExist"));
			}
		}

		/* main code */
		userQuery = "UPDATE User SET Password=? WHERE Id=?";
		userPreparedStatement = conn.prepareStatement(userQuery);
		userPreparedStatement.setString(1,password);
		userPreparedStatement.executeUpdate();
	}
}
