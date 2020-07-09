package it.polimi.tiw.pure_html.controller;

import it.polimi.tiw.pure_html.beans.User;
import it.polimi.tiw.pure_html.dao.UserDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServletDBConnected {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = findLanguage(req);
		ServletContext context = getServletContext();
		WebContext webContext = new WebContext(req,resp,context);
		HttpSession session;
		String loginPage = "login";
		String uniqueCode = req.getParameter("code");
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String login = req.getParameter("login");
		String register = req.getParameter("register");
		UserDAO userDAO = new UserDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		User user;
		boolean loginError = false, registerError = false, dataError = false;

		// the request is processed only if the parameters sent are correct
		if (login != null && uniqueCode != null && password != null) {
			try {
				user = userDAO.findUser(Integer.parseInt(uniqueCode));
				if (user != null) {
					if (user.getPassword().equals(password)) {
						session = req.getSession(true);
						session.setAttribute("user",user);
					} else {
						loginError = true;
						dataError = true;
					}
				} else {
					loginError = true;
					dataError = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				resp.sendRedirect("/error?code=500");
			} catch (NumberFormatException e) {
				loginError = true;
				dataError = true;
			}

			// if there has been an error the user is sent back to its initial page
			if (dataError) {
				webContext.setVariable("lang",lang);
				webContext.setVariable("loginError",loginError);
				thymeleaf.process(loginPage,webContext,resp.getWriter());
			} else {
				resp.sendRedirect("/homepage");
			}
		} else {
			resp.sendRedirect("/index");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("/index");
	}
}
