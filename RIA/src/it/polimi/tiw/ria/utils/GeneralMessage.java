package it.polimi.tiw.ria.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GeneralMessage {
	private Map<String,String> keys;

	public GeneralMessage(ResourceBundle languageResource) {
		if (languageResource != null) {
			keys = new HashMap<>();
			for (String key : languageResource.keySet()) {
				keys.put(key, languageResource.getString(key));
			}
		} else {
			keys = null;
		}
	}
}
