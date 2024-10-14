package models.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import data.GuildData;
import exceptions.NotFoundException;
import models.UserAccount;
import java.sql.ResultSet;

public class GuildRole {
	private String id;
	private String guild_id;
	private String name;

	// TODO:
	// public static GuildRole getByAccountId(Connection con, String account_id) {
	//
	// }

	public GuildRole(String id, String guild_id, String name) {
		this.id = id;
		this.guild_id = guild_id;
		this.name = name;
	}

	public String getId() {return id;}

	public String getGuild_id() {return guild_id;}

	public String getName() {return name;}

	public String getAccountId(Connection con, GuildData data) throws SQLException, NotFoundException {
		String query = "SELECT * FROM user_account WHERE username = ?";

		PreparedStatement stmt = con.prepareStatement(query);

		stmt.setString(1, data.getUsername());
		ResultSet rs = stmt.executeQuery();
		if(!rs.next())
			throw new NotFoundException("Account ID doesn't exist!");
		return rs.getString("id");
	}

	public int getGuildId(Connection con, GuildData guildData) throws SQLException, NotFoundException{
		String query = "SELECT * FROM guild WHERE name = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, guildData.getUsername());
		ResultSet rs = stmt.executeQuery();

		if (!rs.next()) {
			throw new NotFoundException("Guild ID doesn't exist!");
		}
		return rs.getInt("id");
	}
	public int getGuildRoleId(Connection con, GuildData guildData) throws SQLException, NotFoundException{
		guildData.setGuildRoleId(getGuildId(con, guildData));
		String query = "SELECT * FROM guild_role WHERE guild_id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, guildData.getGuildRoleId());
		ResultSet rs = stmt.executeQuery();
		if(!rs.next())
			throw new NotFoundException("Guild role id doesn't exist");
		return rs.getInt("id");
	}
	public void insert(Connection con, GuildData guildData) throws SQLException, NotFoundException{
		String accountId = getAccountId(con, guildData);
		int guildRoleId = getGuildRoleId(con, guildData);
		String query = "INSERT INTO user_role(account_id, guild_role_id) VALUES (?, ?)";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, accountId);
		stmt.setInt(2, guildRoleId);
		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0)
			throw new SQLException("Insert role is failed, no row is effected!");
	}
	public void update(Connection con, GuildData guildData) throws SQLException, NotFoundException{
		String accountId = getAccountId(con, guildData);
		int guildRoleId = getGuildRoleId(con, guildData);
		String query = "UPDATE user_role SET guild_role_id = ? WHERE account_id = ? ";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, guildRoleId);
		stmt.setString(2, accountId);
		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0)
			throw new SQLException("Update role is failed, no row is effected!");
	}
	public void delete(Connection con, GuildData guildData) throws SQLException, NotFoundException{
		String accountId = getAccountId(con, guildData);
		int guildRoleId = getGuildRoleId(con, guildData);
		String query = "DELETE FROM user_role WHERE account_id = ? AND guild_role_id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, accountId);
		stmt.setInt(2, guildRoleId);
		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0)
			throw new SQLException("Delete role is failed, no row is effected!");
	}
}
