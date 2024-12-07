package models;

public class GuildRole {
	private String role;
	private int guildId;
	private int id;
	private String guildName;

	public GuildRole(int guildId, String name) {
		this.guildId = guildId;
		this.role = name;
	}

	public GuildRole(String role, String guildName) {
		this.role = role;
		this.guildName = guildName;
	}

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getGuildId() {
		return guildId;
	}

	public void setGuildId(int guildId) {
		this.guildId = guildId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGuild_id() {
		return guildId;
	}

	public String getName() {
		return role;
	}
}
