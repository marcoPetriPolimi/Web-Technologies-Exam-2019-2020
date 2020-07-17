package it.polimi.tiw.ria.controller;

import com.google.gson.Gson;
import it.polimi.tiw.ria.beans.User;
import it.polimi.tiw.ria.dao.AccountDAO;
import it.polimi.tiw.ria.dao.AddressBookDAO;
import it.polimi.tiw.ria.dao.UserDAO;
import it.polimi.tiw.ria.utils.ErrorMessage;

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

@WebServlet("/registration")
@MultipartConfig
public class CheckRegistration extends HttpServletDBConnected {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = HttpServletThymeleaf.findLanguage(req);
		ServletContext context = getServletContext();
		HttpSession session;
		String username = req.getParameter("username");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String repeatPassword = req.getParameter("repeatedPwd");
		UserDAO userDAO = new UserDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		AccountDAO accountDAO = new AccountDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		AddressBookDAO addressBookDAO = new AddressBookDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
		User user = null;
		Gson gson = new Gson();
		String jsonResponse;
		ErrorMessage registrationMessage;
		boolean dataError = false, passwordError = false, userExistError = false, emailError = false, generalError = false;

		if (username != null && email != null && password != null && repeatPassword != null) {
			if (username.length() == 0 || username.length() > 30 || password.length() == 0 || password.length() > 30 || email.length() > 100 || email.length() == 0 || repeatPassword.length() == 0 || repeatPassword.length() > 30) {
				dataError = true;
			} else if (!password.equals(repeatPassword)) {
				passwordError = true;
			} else if (!email.matches(".*@.*\\..*")) {
				emailError = true;
			} else {
				try {
					user = userDAO.findUser(username);
					if (user == null) {
						userDAO.createUser(email,username,password);
						user = userDAO.findUser(username);
						accountDAO.createAccount(user.getCode());
						addressBookDAO.createAddressBook(user.getCode());
					} else {
						userExistError = true;
					}
				} catch (SQLException e) {
					generalError = true;
				}
			}

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			if (dataError) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				registrationMessage = new ErrorMessage(lang,lang.getString("errorRegistration"));
			} else if (passwordError) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				registrationMessage = new ErrorMessage(lang,lang.getString("errorRegistrationPassword"));
			} else if (emailError) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				registrationMessage = new ErrorMessage(lang,lang.getString("errorRegistrationEmail"));
			} else if (userExistError) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				registrationMessage = new ErrorMessage(lang,lang.getString("errorRegistrationUserExist"));
			} else if (generalError) {
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				registrationMessage = new ErrorMessage(lang);
			} else {
				resp.setStatus(HttpServletResponse.SC_OK);
				registrationMessage = new ErrorMessage(lang);
			}
			resp.getWriter().write(gson.toJson(registrationMessage));
		} else {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			registrationMessage = new ErrorMessage(lang,lang.getString("errorRegistration"));
			resp.getWriter().write(gson.toJson(registrationMessage));
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("/index");
	}
}
