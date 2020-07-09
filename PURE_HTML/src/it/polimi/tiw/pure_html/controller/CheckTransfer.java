package it.polimi.tiw.pure_html.controller;

import it.polimi.tiw.pure_html.beans.Account;
import it.polimi.tiw.pure_html.beans.User;
import it.polimi.tiw.pure_html.dao.AccountDAO;
import it.polimi.tiw.pure_html.dao.TransferDAO;
import it.polimi.tiw.pure_html.dao.UserDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

@WebServlet("/CheckTransfer")
public class CheckTransfer extends HttpServletDBConnected {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = findLanguage(req);
		ServletContext context = getServletContext();
		WebContext webContext = new WebContext(req,resp,context);
		String fail = "fail", success = "success";
		UserDAO userDAO = new UserDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		AccountDAO accountDAO = new AccountDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		TransferDAO transferDAO = new TransferDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		User user = (User) req.getSession().getAttribute("user"), recipient;
		Account account, recipientAccount;
		int amount;
		boolean userAccountMatchError = false, invalidAmount = false, sameAccount = false, dataError = false;

		String accountCode = req.getParameter("accountCode");
		String recipientCode = req.getParameter("recipient");
		String recipientAccountCode = req.getParameter("recipientAccount");
		String amountInserted = req.getParameter("amount");
		String reason = req.getParameter("reason");
		String order = req.getParameter("order");

		if (order != null && accountCode != null && recipientCode != null && recipientAccountCode != null && amountInserted != null && (reason == null || reason.length() <= 200)) {
			try {
				account = accountDAO.findAccount(Integer.parseInt(accountCode));
				recipientAccount = accountDAO.findAccount(Integer.parseInt(recipientAccountCode));
				recipient = userDAO.findUser(Integer.parseInt(recipientCode));
				amount = Integer.parseInt(amountInserted);

				if (recipientAccount == null || recipient == null || !recipientAccount.getOwner().equals(recipient)) {
					dataError = true;
					userAccountMatchError = true;
				}
				if (account.equals(recipientAccount)) {
					dataError = true;
					sameAccount = true;
				}
				if (amount <= 0 || amount > account.getBalance()) {
					dataError = true;
					invalidAmount = true;
				}
				if (!userAccountMatchError && !sameAccount && !invalidAmount) {
					// the amount can be transferred
					accountDAO.changeAmount(account.getCode(), amount, false);
					accountDAO.changeAmount(recipientAccount.getCode(), amount, true);
					transferDAO.createTransfer(account.getCode(), recipientAccount.getCode(), reason, amount);
				}

				if (dataError) {
					webContext.setVariable("lang",lang);
					webContext.setVariable("userAccountMatchError",userAccountMatchError);
					webContext.setVariable("sameAccount",sameAccount);
					webContext.setVariable("invalidAmount",invalidAmount);
					webContext.setVariable("account",account);
					thymeleaf.process(fail,webContext,resp.getWriter());
				} else {
					account = accountDAO.findAccount(account.getCode());
					recipientAccount = accountDAO.findAccount(recipientAccount.getCode());

					webContext.setVariable("lang",lang);
					webContext.setVariable("account",account);
					webContext.setVariable("recipientAccount",recipientAccount);
					thymeleaf.process(success,webContext,resp.getWriter());
				}
			} catch (NumberFormatException e) {
				resp.sendRedirect("/accountState?accountCode="+accountCode);
			} catch (SQLException e) {
				e.printStackTrace();
				resp.sendRedirect("/error?code=500");
			}
		} else {
			resp.sendRedirect("/accountState?accountCode="+accountCode);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String accountCode = req.getParameter("accountCode");

		if (accountCode != null) {
			resp.sendRedirect("/accountState?accountCode="+accountCode);
		} else {
			resp.sendRedirect("/homepage");
		}
	}
}
