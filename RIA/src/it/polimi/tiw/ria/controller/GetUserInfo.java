package it.polimi.tiw.ria.controller;

import com.google.gson.Gson;
import it.polimi.tiw.ria.beans.Account;
import it.polimi.tiw.ria.beans.User;
import it.polimi.tiw.ria.beans.UserSecurity;
import it.polimi.tiw.ria.dao.UserDAO;
import it.polimi.tiw.ria.utils.AccountMessage;
import it.polimi.tiw.ria.utils.HomepageMessage;
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

@WebServlet("/getUserInfo")
public class GetUserInfo extends HttpServletDBConnected {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = HttpServletThymeleaf.findLanguage(req);
		ServletContext context = getServletContext();
		WebContext webContext = new WebContext(req,resp,context);
		UserDAO userDAO = new UserDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		User user = (User) req.getSession().getAttribute("user");
		HomepageMessage homepageMessage;
		Gson gson = new Gson();
		String jsonResponse;
		List<Account> accounts;

		try {
			accounts = userDAO.findAccounts(user.getCode());
			webContext.setVariable("lang",lang);
			webContext.setVariable("user",user);
			webContext.setVariable("accounts",accounts);

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_OK);
			homepageMessage = new HomepageMessage(lang,new UserSecurity(user),accounts);
			jsonResponse = gson.toJson(homepageMessage);
			resp.getWriter().write(jsonResponse);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			homepageMessage = new HomepageMessage(lang);
			jsonResponse = gson.toJson(homepageMessage);
			resp.getWriter().write(jsonResponse);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
