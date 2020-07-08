package it.polimi.tiw.pure_html.controller;

import it.polimi.tiw.pure_html.filter.HttpServletFilter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.SQLException;

public class HttpServletDBConnected extends HttpServlet {
	protected Connection conn;

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			ServletContext context = config.getServletContext();
			conn = HttpServletFilter.applyConnection(context);
		} catch (ClassNotFoundException e) {
			throw new UnavailableException("Can't find database driver");
		} catch (SQLException e) {
			throw new UnavailableException("Can't load database");
		}
	}
}
