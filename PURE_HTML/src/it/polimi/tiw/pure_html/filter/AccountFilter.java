package it.polimi.tiw.pure_html.filter;

import it.polimi.tiw.pure_html.beans.Account;
import it.polimi.tiw.pure_html.beans.User;
import it.polimi.tiw.pure_html.controller.HttpServletDBConnected;
import it.polimi.tiw.pure_html.dao.AccountDAO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebFilter
public class AccountFilter extends HttpServletFilter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		AccountDAO accountDAO = new AccountDAO(conn, HttpServletDBConnected.findLanguage(req).getLocale().getLanguage(), HttpServletDBConnected.findLanguage(req).getLocale().getCountry());
		User user = (User) session.getAttribute("user");
		Account account;
		int accountCode;

		try {
			accountCode = Integer.parseInt(req.getParameter("accountCode"));
			account = accountDAO.findAccount(accountCode);

			if (account == null || !account.getOwner().equals(user)) {
				resp.sendRedirect("/homepage");
			} else {
				chain.doFilter(request,response);
			}
		} catch (NumberFormatException e) {
			resp.sendRedirect("/homepage");
		} catch (SQLException e) {
			resp.sendRedirect("/error?code=500");
		}
	}
}
