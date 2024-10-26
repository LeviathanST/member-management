package models.roles;

import java.sql.Connection;


public class GuildRole {
	private String name;
	private int guild_id;
	private Connection con;

	public GuildRole(int guild_id, String name, Connection con) {
		this.guild_id = guild_id;
		this.name = name;
		this.con = con;
	}

	public int getGuildId() {
		return this.guild_id;
	}

	public String getName() {
		return this.name;
	}
	
}
