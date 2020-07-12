package it.polimi.tiw.ria.utils;

import java.util.List;
import java.util.Map;

public class Const {
	// there can't be instantiation of this class
	private Const() {}

	// accepted languages list
	public static final String defaultLanguage = "eng";
	public static final String defaultCountry = "US";
	public static final List<String> acceptedLangTags = List.of("ita","eng");
	public static final List<String> acceptedOldIsoLangTags = List.of("it","en");
	public static final Map<String,String> oldIsoLangTagsToNew = Map.of("it","ita","en","eng");
	public static final Map<String,String> isoTagToCountry = Map.of("ita","IT","eng","US");

	// default properties files position
	public static final String propertiesDirectory = "/WEB-INF/classes";
	public static final String propertiesBaseName = "PURE_HTML";

	// error messages before having instantiated the correct lang
	public static final String unavailableException = "Can't find database driver";
	public static final String sqlException = "Can't load database";

	// error due to client's file modification
	public static final String modificationError = "Error";
}
