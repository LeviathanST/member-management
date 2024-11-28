package dto;

public class SignUpDTO {
	private String username;
	private String password;

	public SignUpDTO() {

	}

	public SignUpDTO(String username, String password) {
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

	public void setUsername(String username) {
		this.username = username;
	}
	
}
