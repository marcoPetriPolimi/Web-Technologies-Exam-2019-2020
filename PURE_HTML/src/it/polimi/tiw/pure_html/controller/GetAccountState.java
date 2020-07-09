package it.polimi.tiw.pure_html.controller;

import it.polimi.tiw.pure_html.beans.Account;
import it.polimi.tiw.pure_html.beans.Transfer;
import it.polimi.tiw.pure_html.beans.User;
import it.polimi.tiw.pure_html.dao.AccountDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet("/accountState")
public class GetAccountState extends HttpServletDBConnected {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = findLanguage(req);
		ServletContext context = getServletContext();
		WebContext webContext = new WebContext(req,resp,context);
		String page = "accountState";
		AccountDAO accountDAO = new AccountDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		User user = (User) req.getSession().getAttribute("user");
		List<Transfer> ingoingTransfers, outgoingTransfers;
		Account account;

		try {
			account = accountDAO.findAccount(Integer.parseInt(req.getParameter("accountCode")));
			ingoingTransfers = accountDAO.findTransfers(account.getCode(),false);
			outgoingTransfers = accountDAO.findTransfers(account.getCode(),true);
			webContext.setVariable("lang",lang);
			webContext.setVariable("accountCode",account.getCode());
			webContext.setVariable("accountBalance",account.getBalance());
			webContext.setVariable("autoTransfer",false);
			webContext.setVariable("userAccountMatchError",false);
			webContext.setVariable("invalidAmount",false);
			webContext.setVariable("outgoingTransfers",outgoingTransfers);
			webContext.setVariable("ingoingTransfers",ingoingTransfers);
			thymeleaf.process(page,webContext,resp.getWriter());
		} catch (SQLException e) {
			e.printStackTrace();
			resp.sendRedirect("/error?code=500");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/CheckTransfer").forward(req,resp);
	}
}
