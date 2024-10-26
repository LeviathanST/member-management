package dto;

public class UserGuildRoleDTO {
	private String accountId;
	private int guildRoleId;
	private String name;

	public UserGuildRoleDTO(String name, String accountId, int guildRoleId) {
		this.accountId = accountId;
		this.guildRoleId = guildRoleId;
	}

	public String getName() {
		return this.name;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public int getGuildRoleId() {
		return this.guildRoleId;
	}

	public void setGuildRoleId(int crewRoleId) {
		this.guildRoleId = crewRoleId;
	}
}
