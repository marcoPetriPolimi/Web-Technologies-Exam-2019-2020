package it.polimi.tiw.ria.controller;

import com.google.gson.Gson;
import it.polimi.tiw.ria.beans.Account;
import it.polimi.tiw.ria.beans.AddressBook;
import it.polimi.tiw.ria.beans.Transfer;
import it.polimi.tiw.ria.beans.User;
import it.polimi.tiw.ria.dao.AccountDAO;
import it.polimi.tiw.ria.dao.AddressBookDAO;
import it.polimi.tiw.ria.utils.AccountMessage;
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
		ResourceBundle lang = HttpServletThymeleaf.findLanguage(req);
		AccountDAO accountDAO = new AccountDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		AddressBookDAO addressBookDAO = new AddressBookDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		List<Transfer> ingoingTransfers, outgoingTransfers;
		Account account;
		AddressBook addressBook;
		AccountMessage accountMessage;
		Gson gson = new Gson();
		String jsonResponse;
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
			addressBook = addressBookDAO.findAddressBook(account.getOwner());

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

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_OK);
			accountMessage = new AccountMessage(lang,account,ingoingTransfers,outgoingTransfers,numberOfIngoing,numberOfOutgoing,addressBook);
			jsonResponse = gson.toJson(accountMessage);
			resp.getWriter().write(jsonResponse);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			accountMessage = new AccountMessage(lang);
			jsonResponse = gson.toJson(accountMessage);
			resp.getWriter().write(jsonResponse);
		} catch (NumberFormatException e) {
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			accountMessage = new AccountMessage(lang);
			jsonResponse = gson.toJson(accountMessage);
			resp.getWriter().write(jsonResponse);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
