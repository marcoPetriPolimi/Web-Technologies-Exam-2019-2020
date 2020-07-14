package it.polimi.tiw.ria.controller;

import com.google.gson.Gson;
import it.polimi.tiw.ria.utils.Const;
import it.polimi.tiw.ria.utils.GeneralMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet("/language")
public class GetLang extends HttpServletDBConnected {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = HttpServletThymeleaf.findLanguage(req);
		HttpSession session = req.getSession(true);
		GeneralMessage languageMessage;
		Gson gson = new Gson();
		String jsonResponse;

		String language = req.getParameter("lang");
		String country = req.getParameter("country");

		if (language != null && country != null) {
			lang = HttpServletThymeleaf.getLanguage(language,country);
			session.setMaxInactiveInterval(Const.sessionExpireTime);
			session.setAttribute("lang",lang.getLocale().getLanguage());
			session.setAttribute("country",lang.getLocale().getCountry());
		}

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		languageMessage = new GeneralMessage(lang);
		jsonResponse = gson.toJson(languageMessage);
		resp.getWriter().write(jsonResponse);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
