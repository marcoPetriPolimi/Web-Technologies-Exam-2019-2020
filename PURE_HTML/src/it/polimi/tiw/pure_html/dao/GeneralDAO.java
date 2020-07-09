package it.polimi.tiw.pure_html.dao;

import java.sql.Connection;
import java.util.Locale;
import java.util.ResourceBundle;

public class GeneralDAO {
	protected Connection conn;
	protected ResourceBundle selectedLanguage;

	public GeneralDAO(Connection conn) {
		this.conn = conn;
		selectedLanguage = ResourceBundle.getBundle("PURE_HTML",new Locale("en","US"));
	}
	public GeneralDAO(Connection conn, String language, String country) {
		this.conn = conn;
		selectedLanguage = ResourceBundle.getBundle("PURE_HTML",new Locale(language,country));
	}
}
