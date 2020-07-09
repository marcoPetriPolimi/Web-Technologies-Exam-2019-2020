package it.polimi.tiw.pure_html.filter;

import it.polimi.tiw.pure_html.utils.Const;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class HttpServletFilter extends HttpFilter {
	protected Connection conn;

	@Override
	public void init(FilterConfig config) throws ServletException {
		try {
			ServletContext context = config.getServletContext();
			conn = applyConnection(context);
		} catch (ClassNotFoundException e) {
			throw new UnavailableException(Const.unavailableException);
		} catch (SQLException e) {
			throw new UnavailableException(Const.sqlException);
		}
	}

	public static Connection applyConnection(ServletContext context) throws ClassNotFoundException, SQLException {
		String username = context.getInitParameter("DbUser");
		String password = context.getInitParameter("DbPassword");
		String database = context.getInitParameter("DbUrl");
		String driver = context.getInitParameter("DbDriver");
		Class.forName(driver);
		return DriverManager.getConnection(database,username,password);
	}
}
