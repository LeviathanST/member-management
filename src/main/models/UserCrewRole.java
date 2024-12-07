package models;

public class UserCrewRole {
	private String username;
	private String name;
	private String crew;

	private String accountId;
	private int crewRoleId;

	public UserCrewRole(String crew, String username, String role) {
		this.crew = crew;
		this.username = username;
		this.name = role;
	}

	public String getUsername() {
		return this.username;
	}

	public String getName() {
		return this.name;
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
