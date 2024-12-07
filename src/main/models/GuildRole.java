package models;

public class GuildRole {
	private String name;
	private int guildId;
	private int id;
	private String guildName;

	public GuildRole(int guildId, String name) {
		this.guildId = guildId;
		this.name = name;
	}

	public GuildRole(String role, String guildName) {
		this.name = role;
		this.guildName = guildName;
	}

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	public String getName() {
		return name;
	}

	public void setName(String role) {
		this.name = role;
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
}
