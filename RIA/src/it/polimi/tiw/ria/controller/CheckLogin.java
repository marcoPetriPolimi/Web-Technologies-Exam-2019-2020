package it.polimi.tiw.ria.controller;

import com.google.gson.Gson;
import it.polimi.tiw.ria.beans.User;
import it.polimi.tiw.ria.dao.UserDAO;
import it.polimi.tiw.ria.utils.GeneralMessage;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

@WebServlet("/login")
@MultipartConfig
public class CheckLogin extends HttpServletDBConnected {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = HttpServletThymeleaf.findLanguage(req);
		ServletContext context = getServletContext();
		HttpSession session;
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		UserDAO userDAO = new UserDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		User user = null;
		Gson gson = new Gson();
		String jsonResponse;
		GeneralMessage loginMessage;
		boolean dataError = false, generalError = false;

		// the request is processed only if the parameters sent are correct
		if (username != null && password != null) {
			try {
				user = userDAO.findUser(username);
				if (user != null) {
					if (user.getPassword().equals(password)) {
						session = req.getSession(true);
						session.setAttribute("user",user);
					} else {
						dataError = true;
					}
				} else {
					dataError = true;
				}
			} catch (SQLException e) {
				generalError = true;
			} catch (NumberFormatException e) {
				dataError = true;
			}

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			// if there has been an error the user is sent back to its initial page
			if (dataError) {
				// there has been some errors while logging in
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else if (!generalError) {
				// login correctly worked
				resp.setStatus(HttpServletResponse.SC_OK);
			} else {
				// there has been a server internal error
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			loginMessage = new GeneralMessage(lang);
			jsonResponse = gson.toJson(loginMessage);
			resp.getWriter().write(jsonResponse);
		} else {
			resp.sendRedirect("/index");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("/index");
	}
}
