package it.polimi.tiw.ria.controller;

import com.google.gson.Gson;
import it.polimi.tiw.ria.beans.Account;
import it.polimi.tiw.ria.beans.AddressBook;
import it.polimi.tiw.ria.beans.Recipient;
import it.polimi.tiw.ria.beans.User;
import it.polimi.tiw.ria.dao.AccountDAO;
import it.polimi.tiw.ria.dao.AddressBookDAO;
import it.polimi.tiw.ria.dao.RecipientDAO;
import it.polimi.tiw.ria.utils.AddressBookMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet("/addToAddressBook")
public class CheckAddressBookContact extends HttpServletDBConnected {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = HttpServletThymeleaf.findLanguage(req);
		AddressBookDAO addressBookDAO = new AddressBookDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		AccountDAO accountDAO = new AccountDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		RecipientDAO recipientDAO = new RecipientDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		User user = (User) req.getSession().getAttribute("user");
		List<Recipient> recipientList;
		AddressBookMessage addressBookMessage;
		Account recipientAccount;
		AddressBook addressBook;
		Gson gson = new Gson();
		String jsonResponse;
		boolean alreadyPresent = false, formatError = false;

		String userToAdd = req.getParameter("user");
		String accountToAdd = req.getParameter("account");
		int userCode;
		int accountCode;

		if (userToAdd != null && accountToAdd != null) {
			try {
				userCode = Integer.parseInt(userToAdd);
				accountCode = Integer.parseInt(accountToAdd);
				addressBook = addressBookDAO.findAddressBook(user);
				recipientAccount = accountDAO.findAccount(accountCode);

				if (recipientAccount.getOwner().getCode() != userCode) {
					formatError = true;
				} else {
					recipientList = addressBookDAO.findRecipients(addressBook);

					for (int i = 0; i < recipientList.size() && !alreadyPresent; i++) {
						if (recipientList.get(i).getUserId() == userCode && recipientList.get(i).getAccountId() == accountCode) {
							alreadyPresent = true;
						}
					}
				}

				if (formatError) {
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					addressBookMessage = new AddressBookMessage(lang);
					jsonResponse = gson.toJson(addressBookMessage);
					resp.getWriter().write(jsonResponse);
				} else if (alreadyPresent) {
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					addressBookMessage = new AddressBookMessage(lang);
					jsonResponse = gson.toJson(addressBookMessage);
					resp.getWriter().write(jsonResponse);
				} else {
					recipientDAO.createRecipient(addressBook.getAddressBookId(),accountCode,userCode);
					addressBook = addressBookDAO.findAddressBook(user);
					addressBook = addressBook.setRecipients(addressBookDAO.findRecipients(addressBook));

					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");
					resp.setStatus(HttpServletResponse.SC_OK);
					addressBookMessage = new AddressBookMessage(lang, addressBook);
					jsonResponse = gson.toJson(addressBookMessage);
					resp.getWriter().write(jsonResponse);
				}
			} catch (NumberFormatException e) {
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				addressBookMessage = new AddressBookMessage(lang);
				jsonResponse = gson.toJson(addressBookMessage);
				resp.getWriter().write(jsonResponse);
			} catch (SQLException e) {
				e.printStackTrace();
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				addressBookMessage = new AddressBookMessage(lang);
				jsonResponse = gson.toJson(addressBookMessage);
				resp.getWriter().write(jsonResponse);
			}
		} else {
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			addressBookMessage = new AddressBookMessage(lang);
			jsonResponse = gson.toJson(addressBookMessage);
			resp.getWriter().write(jsonResponse);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
