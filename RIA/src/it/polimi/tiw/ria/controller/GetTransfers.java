package it.polimi.tiw.ria.controller;

import com.google.gson.Gson;
import it.polimi.tiw.ria.beans.Account;
import it.polimi.tiw.ria.beans.Transfer;
import it.polimi.tiw.ria.dao.AccountDAO;
import it.polimi.tiw.ria.utils.TransferMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet("/getTransfers")
@MultipartConfig
public class GetTransfers extends HttpServletDBConnected {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = HttpServletThymeleaf.findLanguage(req);
		AccountDAO accountDAO = new AccountDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		Account account;
		List<Transfer> transfers;
		int numberOfPages, page;
		TransferMessage transferMessage;
		Gson gson = new Gson();
		String jsonResponse;

		String type = req.getParameter("type");
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

			if (type != null) {
				if (type.equals("out")) {
					numberOfPages = (int) Math.ceil((double)accountDAO.numberOfTransfers(account.getCode(),true)/10.0);
					page = Integer.parseInt(out);
					if (page > numberOfPages) {
						page = 1;
					}

					// find the wanted transfers
					transfers = accountDAO.findTransfers(account.getCode(),true,page);
				} else {
					numberOfPages = (int) Math.ceil((double)accountDAO.numberOfTransfers(account.getCode(),false)/10.0);
					page = Integer.parseInt(in);
					if (page > numberOfPages) {
						page = 1;
					}

					// find the wanted transfers
					transfers = accountDAO.findTransfers(account.getCode(),false,page);
				}

				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.setStatus(HttpServletResponse.SC_OK);
				transferMessage = new TransferMessage(lang,transfers,numberOfPages,page);
				jsonResponse = gson.toJson(transferMessage);
				resp.getWriter().write(jsonResponse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			transferMessage = new TransferMessage(lang);
			jsonResponse = gson.toJson(transferMessage);
			resp.getWriter().write(jsonResponse);
		} catch (NumberFormatException e) {
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			transferMessage = new TransferMessage(lang);
			jsonResponse = gson.toJson(transferMessage);
			resp.getWriter().write(jsonResponse);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
