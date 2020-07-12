package it.polimi.tiw.ria.utils;

import java.util.ResourceBundle;

public class ErrorMessage extends GeneralMessage {
	private String error;

	public ErrorMessage(ResourceBundle language) {
		super(language);
		error = "";
	}
	public ErrorMessage(ResourceBundle language, String errorMessage) {
		super(language);
		error = errorMessage;
	}
}
