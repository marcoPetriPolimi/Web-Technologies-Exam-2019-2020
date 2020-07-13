package it.polimi.tiw.ria.controller;

import it.polimi.tiw.ria.filter.HttpServletFilter;
import it.polimi.tiw.ria.utils.Const;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class HttpServletThymeleaf extends HttpServlet {
	protected Connection conn;
	protected TemplateEngine thymeleaf;

	@Override
	public void init() throws ServletException {
		try {
			// getting the connection
			ServletContext context = getServletContext();
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
	public void destroy() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException ignored) {}
	}

	public static ResourceBundle findLanguage(HttpServletRequest req) {
		ResourceBundle lang;
		HttpSession session = req.getSession(true);
		String language, country;

		if (session.getAttribute("lang") != null) {
			language = (String) session.getAttribute("lang");
			country = (String) session.getAttribute("country");
			lang = getLanguage(language,country);
		} else {
			if (Const.acceptedLangTags.contains(req.getLocale().getLanguage())) {
				language = req.getLocale().getLanguage();
				country = Const.isoTagToCountry.get(language);
				lang = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(language,country));
			} else if (Const.acceptedOldIsoLangTags.contains(req.getLocale().getLanguage())) {
				language = Const.oldIsoLangTagsToNew.get(req.getLocale().getLanguage());
				country = Const.isoTagToCountry.get(language);
				lang = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(language,country));
			} else {
				lang = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(Const.defaultLanguage,Const.defaultCountry));
			}
			session.setMaxInactiveInterval(Const.sessionExpireTime);
			session.setAttribute("lang",lang.getLocale().getLanguage());
			session.setAttribute("country",lang.getLocale().getCountry());
		}


		return lang;
	}

	public static ResourceBundle getLanguage(String language, String country) {
		ResourceBundle lang;

		if (Const.acceptedLangTags.contains(language)) {
			country = Const.isoTagToCountry.get(language);
			lang = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(language,country));
		} else if (Const.acceptedOldIsoLangTags.contains(language)) {
			language = Const.oldIsoLangTagsToNew.get(language);
			country = Const.isoTagToCountry.get(language);
			lang = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(language,country));
		} else {
			lang = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(Const.defaultLanguage,Const.defaultCountry));
		}

		return lang;
	}
}
