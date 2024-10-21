package data;

public class SignUpData {
	private String username;
	private String password;

	public SignUpData() {}

	public SignUpData(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public void setUserName(String userName) {
		this.username = userName;
	}

	
}
