package view.formbeans;

public class RegistrationBean {
	private String username;
	private String email;
	private String password;
	private String password_repeat;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword_repeat() {
		return password_repeat;
	}

	public void setPassword_repeat(String password_repeat) {
		this.password_repeat = password_repeat;
	}
}
