package it.polimi.tiw.ria.controller;

import it.polimi.tiw.ria.filter.HttpServletFilter;
import it.polimi.tiw.ria.utils.Const;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.SQLException;

public class HttpServletDBConnected extends HttpServlet {
	protected Connection conn;

	@Override
	public void init() throws ServletException {
		try {
			// getting the connection
			ServletContext context = getServletContext();
			conn = HttpServletFilter.applyConnection(context);
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
}