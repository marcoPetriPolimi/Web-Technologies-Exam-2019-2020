package it.polimi.tiw.ria.filter;

import com.google.gson.Gson;
import it.polimi.tiw.ria.beans.Account;
import it.polimi.tiw.ria.beans.User;
import it.polimi.tiw.ria.controller.HttpServletThymeleaf;
import it.polimi.tiw.ria.dao.AccountDAO;
import it.polimi.tiw.ria.utils.GeneralMessage;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

@WebFilter
@MultipartConfig
public class AccountFilter extends HttpServletFilter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		ResourceBundle lang = HttpServletThymeleaf.findLanguage(req);
		AccountDAO accountDAO = new AccountDAO(conn, lang.getLocale().getLanguage(), lang.getLocale().getCountry());
		User user = (User) session.getAttribute("user");
		GeneralMessage filterMessage = new GeneralMessage(lang);
		Gson gson = new Gson();
		String jsonResponse;
		Account account;
		int accountCode;

		try {
			accountCode = Integer.parseInt(req.getParameter("accountCode"));
			account = accountDAO.findAccount(accountCode);

			if (account == null || !account.getOwner().equals(user)) {
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				jsonResponse = gson.toJson(filterMessage);
				resp.getWriter().write(jsonResponse);
			} else {
				chain.doFilter(request,response);
			}
		} catch (NumberFormatException e) {
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			jsonResponse = gson.toJson(filterMessage);
			resp.getWriter().write(jsonResponse);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			jsonResponse = gson.toJson(filterMessage);
			resp.getWriter().write(jsonResponse);
		}
	}
}
