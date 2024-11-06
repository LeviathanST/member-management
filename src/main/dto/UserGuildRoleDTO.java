package dto;

public class UserGuildRoleDTO {
	private String username;
	private String role;
	private String guild;

	private String accountId;
	private int guildRoleId;

	public UserGuildRoleDTO(String guild, String username, String role) {
		this.guild = guild;
		this.username = username;
		this.role = role;
	}

	public String getUsername() {return this.username;}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() { return this.role; }

	public String getGuild() {return this.guild;}

	public String getAccountId() {return this.accountId;}

	public void setAccountId(String accountId) {this.accountId = accountId;}

	public int getGuildRoleId() {return this.guildRoleId;}
	public void setGuildRoleId(int guildRoleId) {this.guildRoleId = guildRoleId;}
}
