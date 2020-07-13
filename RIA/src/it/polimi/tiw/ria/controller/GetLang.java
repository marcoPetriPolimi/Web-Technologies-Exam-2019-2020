package it.polimi.tiw.ria.controller;

import com.google.gson.Gson;
import it.polimi.tiw.ria.utils.GeneralMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet("/language")
public class GetLang extends HttpServletDBConnected {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = HttpServletThymeleaf.findLanguage(req);
		GeneralMessage languageMessage = new GeneralMessage(lang);
		Gson gson = new Gson();
		String jsonResponse;

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		jsonResponse = gson.toJson(languageMessage);
		resp.getWriter().write(jsonResponse);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
