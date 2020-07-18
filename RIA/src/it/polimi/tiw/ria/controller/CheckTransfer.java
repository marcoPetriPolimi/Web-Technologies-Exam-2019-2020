package it.polimi.tiw.ria.controller;

import com.google.gson.Gson;
import it.polimi.tiw.ria.beans.Account;
import it.polimi.tiw.ria.beans.User;
import it.polimi.tiw.ria.dao.AccountDAO;
import it.polimi.tiw.ria.dao.TransferDAO;
import it.polimi.tiw.ria.dao.UserDAO;
import it.polimi.tiw.ria.utils.ErrorMessage;
import it.polimi.tiw.ria.utils.TransferSuccess;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

@WebServlet("/orderTransfer")
@MultipartConfig
public class CheckTransfer extends HttpServletDBConnected {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = HttpServletThymeleaf.findLanguage(req);
		UserDAO userDAO = new UserDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		AccountDAO accountDAO = new AccountDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		TransferDAO transferDAO = new TransferDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		User user = (User) req.getSession().getAttribute("user"), recipient;
		Account account, recipientAccount;
		ErrorMessage transferMessage;
		TransferSuccess successMessage;
		Gson gson = new Gson();
		String jsonResponse;
		int amount;
		boolean userAccountMatchError = false, invalidAmount = false, sameAccount = false;

		String accountCode = req.getParameter("accountCode");
		String recipientCode = req.getParameter("recipient");
		String recipientAccountCode = req.getParameter("recipientAccount");
		String amountInserted = req.getParameter("amount");
		String reason = req.getParameter("reason");

		if (accountCode != null && recipientCode != null && recipientAccountCode != null && amountInserted != null && reason != null && reason.length() <= 200) {
			try {
				account = accountDAO.findAccount(Integer.parseInt(accountCode));
				recipientAccount = accountDAO.findAccount(Integer.parseInt(recipientAccountCode));
				recipient = userDAO.findUser(Integer.parseInt(recipientCode));
				amount = Integer.parseInt(amountInserted);

				if (recipientAccount == null || recipient == null || !recipientAccount.getOwner().equals(recipient)) {
					userAccountMatchError = true;
				}
				if (account.equals(recipientAccount)) {
					sameAccount = true;
				}
				if (amount <= 0 || amount > account.getBalance()) {
					invalidAmount = true;
				}
				if (!userAccountMatchError && !sameAccount && !invalidAmount) {
					// the amount can be transferred
					accountDAO.changeAmount(account.getCode(), amount, false);
					accountDAO.changeAmount(recipientAccount.getCode(), amount, true);
					transferDAO.createTransfer(account.getCode(), recipientAccount.getCode(), reason, amount);
				}

				if (userAccountMatchError) {
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					transferMessage = new ErrorMessage(lang,lang.getString("errorOrderMatch"));
					jsonResponse = gson.toJson(transferMessage);
					resp.getWriter().write(jsonResponse);
				} else if (sameAccount) {
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					transferMessage = new ErrorMessage(lang,lang.getString("errorOrderAutoTransfer"));
					jsonResponse = gson.toJson(transferMessage);
					resp.getWriter().write(jsonResponse);
				} else if (invalidAmount) {
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					transferMessage = new ErrorMessage(lang,lang.getString("errorOrderAmount"));
					jsonResponse = gson.toJson(transferMessage);
					resp.getWriter().write(jsonResponse);
				} else {
					account = accountDAO.findAccount(account.getCode());
					recipientAccount = accountDAO.findAccount(recipientAccount.getCode());

					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");
					resp.setStatus(HttpServletResponse.SC_OK);
					successMessage = new TransferSuccess(lang,account,recipientAccount);
					jsonResponse = gson.toJson(successMessage);
					resp.getWriter().write(jsonResponse);
				}
			} catch (NumberFormatException e) {
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				transferMessage = new ErrorMessage(lang,lang.getString("errorManipulation"));
				jsonResponse = gson.toJson(transferMessage);
				resp.getWriter().write(jsonResponse);
			} catch (SQLException e) {
				e.printStackTrace();
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				transferMessage = new ErrorMessage(lang);
				jsonResponse = gson.toJson(transferMessage);
				resp.getWriter().write(jsonResponse);
			}
		} else {
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			transferMessage = new ErrorMessage(lang,lang.getString("errorManipulation"));
			jsonResponse = gson.toJson(transferMessage);
			resp.getWriter().write(jsonResponse);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}
}
