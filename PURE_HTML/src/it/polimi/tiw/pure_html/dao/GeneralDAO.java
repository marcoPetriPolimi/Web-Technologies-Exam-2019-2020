package it.polimi.tiw.pure_html.dao;

import it.polimi.tiw.pure_html.utils.Const;

import java.sql.Connection;
import java.util.Locale;
import java.util.ResourceBundle;

public class GeneralDAO {
	protected Connection conn;
	protected ResourceBundle selectedLanguage;

	public GeneralDAO(Connection conn) {
		this.conn = conn;
		selectedLanguage = ResourceBundle.getBundle(Const.bundlePrefix,new Locale(Const.defaultLanguage,Const.defaultCountry));
	}
	public GeneralDAO(Connection conn, String language, String country) {
		this.conn = conn;
		selectedLanguage = ResourceBundle.getBundle(Const.bundlePrefix,new Locale(language,country));
	}
}
