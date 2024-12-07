package models;

public class UserCrewRole {
	private String username;
	private String role;
	private String crew;

	private String accountId;
	private int crewRoleId;

	public UserCrewRole(String crew, String username, String role) {
		this.crew = crew;
		this.username = username;
		this.role = role;
	}

	public String getUsername() {
		return this.username;
	}

	public String getRole() {
		return this.role;
	}

	public String getCrew() {
		return this.crew;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public int getCrewRoleId() {
		return this.crewRoleId;
	}

	public void setCrewRoleId(int crewRoleId) {
		this.crewRoleId = crewRoleId;
	}
}
