package it.polimi.tiw.ria.dao;

import it.polimi.tiw.ria.utils.Const;

import java.sql.Connection;
import java.util.Locale;
import java.util.ResourceBundle;

public class GeneralDAO {
	protected Connection conn;
	protected ResourceBundle selectedLanguage;

	public GeneralDAO(Connection conn) {
		this.conn = conn;
		selectedLanguage = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(Const.defaultLanguage,Const.defaultCountry));
	}
	public GeneralDAO(Connection conn, String language, String country) {
		this.conn = conn;
		selectedLanguage = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(language,country));
	}
}
