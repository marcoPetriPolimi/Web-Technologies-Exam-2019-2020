package it.polimi.tiw.pure_html.controller;

import it.polimi.tiw.pure_html.filter.HttpServletFilter;
import it.polimi.tiw.pure_html.utils.Const;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class HttpServletDBConnected extends HttpServlet {
	protected Connection conn;
	protected TemplateEngine thymeleaf;
	protected ResourceBundle lang;

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			// getting the connection
			ServletContext context = config.getServletContext();
			conn = HttpServletFilter.applyConnection(context);

			// preparing thymeleaf template
			ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
			templateResolver.setTemplateMode(TemplateMode.HTML);
			templateResolver.setCharacterEncoding("UTF-8");
			templateResolver.setPrefix("/WEB-INF/");
			templateResolver.setSuffix(".html");
			thymeleaf = new TemplateEngine();
			thymeleaf.setTemplateResolver(templateResolver);
		} catch (ClassNotFoundException e) {
			throw new UnavailableException(Const.unavailableException);
		} catch (SQLException e) {
			throw new UnavailableException(Const.sqlException);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		findLanguage(req);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		findLanguage(req);
	}

	@Override
	public void destroy() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException ignored) {}
	}

	private void findLanguage(HttpServletRequest req) {
		if (Const.acceptedLangTags.contains(req.getLocale().getLanguage())) {
			String language = req.getLocale().getLanguage();
			String country = Const.isoTagToCountry.get(language);
			lang = ResourceBundle.getBundle(Const.bundlePrefix,new Locale(language,country));
		} else if (Const.acceptedOldIsoLangTags.contains(req.getLocale().getLanguage())) {
			String language = Const.oldIsoLangTagsToNew.get(req.getLocale().getLanguage());
			String country = Const.isoTagToCountry.get(language);
			lang = ResourceBundle.getBundle(Const.bundlePrefix,new Locale(language,country));
		} else {
			lang = ResourceBundle.getBundle(Const.bundlePrefix,new Locale(Const.defaultLanguage,Const.defaultCountry));
		}
	}
}
