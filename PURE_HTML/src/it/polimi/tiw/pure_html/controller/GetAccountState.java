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
		int numberOfIngoing, numberOfOutgoing, ingoingPage, outgoingPage;

		String out = req.getParameter("out");
		String in = req.getParameter("in");
		if (out == null) {
			out = "1";
		}
		if (in == null) {
			in = "1";
		}

		try {
			account = accountDAO.findAccount(Integer.parseInt(req.getParameter("accountCode")));

			// calculates the pages for transfers
			numberOfIngoing = (int) Math.ceil((double)accountDAO.numberOfTransfers(account.getCode(),false)/10.0);
			numberOfOutgoing = (int) Math.ceil((double)accountDAO.numberOfTransfers(account.getCode(),true)/10.0);
			ingoingPage = Integer.parseInt(in);
			outgoingPage = Integer.parseInt(out);
			if (ingoingPage > numberOfIngoing) {
				ingoingPage = 1;
			}
			if (outgoingPage > numberOfOutgoing) {
				outgoingPage = 1;
			}

			// find the wanted transfers
			ingoingTransfers = accountDAO.findTransfers(account.getCode(),false,ingoingPage);
			outgoingTransfers = accountDAO.findTransfers(account.getCode(),true,outgoingPage);

			// sets thymeleaf variables
			webContext.setVariable("lang",lang);
			webContext.setVariable("ingoingPage",ingoingPage);
			webContext.setVariable("outgoingPage",outgoingPage);
			webContext.setVariable("numberOfIngoing",numberOfIngoing);
			webContext.setVariable("numberOfOutgoing",numberOfOutgoing);
			webContext.setVariable("firstIngoingPage",ingoingPage > 1 ? ingoingPage-1 : ingoingPage);
			webContext.setVariable("lastIngoingPage",ingoingPage < numberOfIngoing ? ingoingPage+1 : ingoingPage);
			webContext.setVariable("firstOutgoingPage",outgoingPage > 1 ? outgoingPage-1 : outgoingPage);
			webContext.setVariable("lastOutgoingPage",outgoingPage < numberOfOutgoing ? outgoingPage+1 : outgoingPage);
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
		} catch (NumberFormatException e) {
			resp.sendRedirect("/accountState?accountCode="+req.getParameter("accountCode"));
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/CheckTransfer").forward(req,resp);
	}
}
