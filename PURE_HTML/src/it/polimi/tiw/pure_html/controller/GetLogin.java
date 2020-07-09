package it.polimi.tiw.pure_html.controller;

import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class GetLogin extends HttpServletDBConnected {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);

		ServletContext context = getServletContext();
		WebContext webContext = new WebContext(req,resp,context);
		String page = "login";

		webContext.setVariable("lang",lang);
		thymeleaf.process(page,webContext,resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);

		req.getRequestDispatcher("/CheckLogin").forward(req,resp);
	}
}
