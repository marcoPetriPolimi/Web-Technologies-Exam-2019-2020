package it.polimi.tiw.ria.beans;

/**
 * This is an object thought to be sent on the internet to the client's browser with JSON, for this reason the password mustn't be visible, this class is a perfect copy of the User class except that this class doesn't hold the password information.
 */
public class UserSecurity {
	private final int code;
	private final String email;
	private final String name;

	public UserSecurity(User toCopy) {
		this.code = toCopy.getCode();
		this.email = toCopy.getEmail();
		this.name = toCopy.getName();
	}
	public UserSecurity(int code, String email, String name) {
		this.code = code;
		this.email = email;
		this.name = name;
	}

	public int getCode() {
		return code;
	}
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
}
