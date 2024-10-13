package data;



import services.AuthService;

public class SignUpData {
	private String username;
	private String password;

	public SignUpData(String username, String password) {
		this.username = username;
		String[] errors = AuthService.validatePassword(password);
		if(errors.length == 0)
			this.password = password;
		else this.password = null;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pwd) {
		String[] errors = AuthService.validatePassword(pwd);
		if(errors.length == 0)
			this.password = pwd;
		else this.password = null;
	}

	
}
