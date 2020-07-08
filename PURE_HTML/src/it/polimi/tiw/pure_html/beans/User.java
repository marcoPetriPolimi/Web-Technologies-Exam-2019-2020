package it.polimi.tiw.pure_html.beans;

public class User {
	private final int code;
	private final String name;
	private final String password;

	public User(int code, String name, String password) {
		this.code = code;
		this.name = name;
		this.password = password;
	}
	public User(User other, String newValue, boolean isName) {
		this.code = other.getCode();
		this.name = isName ? newValue : other.getName();
		this.password = isName ? other.getPassword() : newValue;
	}

	public int getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
}
