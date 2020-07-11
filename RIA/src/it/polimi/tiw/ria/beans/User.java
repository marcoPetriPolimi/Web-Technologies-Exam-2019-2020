package it.polimi.tiw.ria.beans;

public class User {
	private final int code;
	private final String email;
	private final String name;
	private final String password;

	public User(int code, String email, String name, String password) {
		this.code = code;
		this.email = email;
		this.name = name;
		this.password = password;
	}
	private User(User other, String newValue, int toChange) {
		this.code = other.getCode();
		this.email = toChange == 3 ? newValue : other.getEmail();
		this.name = toChange == 0 ? newValue : other.getName();
		this.password = toChange == 1 ? newValue : other.getPassword();
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
	public String getPassword() {
		return password;
	}

	public User setName(String name) {
		return new User(this,name,0);
	}
	public User setPassword(String password) {
		return new User(this,password,1);
	}
	public User setEmail(String email) {
		return new User(this,email,3);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User other = (User) obj;
			return code == other.code;
		} else {
			return false;
		}
	}
}
