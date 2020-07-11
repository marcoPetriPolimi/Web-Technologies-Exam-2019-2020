package it.polimi.tiw.ria.utils;

import java.util.ResourceBundle;

public class RegistrationMessage extends GeneralMessage {
	private String error;

	public RegistrationMessage(ResourceBundle language) {
		super(language);
		error = "";
	}
	public RegistrationMessage(ResourceBundle language, String errorMessage) {
		super(language);
		error = errorMessage;
	}
}
