package models.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import data.GuildData;
import exceptions.NotFoundException;
import java.sql.ResultSet;

public class GuildRole {
	private int id;
	private String name;

	// TODO:
	// public static GuildRole getByAccountId(Connection con, String account_id) {
	//
	// }

	public GuildRole(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {return id;}

	public String getName() {return name;}

	public String getAccountId(Connection con, GuildData data) throws SQLException, NotFoundException {
		String query = "SELECT id FROM user_account WHERE username = ?";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, data.getUsername());
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					throw new NotFoundException("Account ID doesn't exist");
				}
				return rs.getString("id");
			}
		}
	}

	public GuildRole getGuildRoleByName(Connection con, GuildData guildData) throws SQLException, NotFoundException{
		String query = "SELECT id FROM guild WHERE name = ?";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, guildData.getGuildName());
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				return new GuildRole(rs.getInt("id"), name);
			}

			throw new NotFoundException("Your specified role is not found!");
		}

	}
	public int getGuildRoleId(Connection con, GuildData guildData) throws SQLException, NotFoundException{
		GuildRole guildRole = getGuildRoleByName(con, guildData);
		String query = "SELECT id FROM guild_role WHERE guild_id = ? AND name = ?";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, guildRole.getId());
			stmt.setString(2, guildData.getGuildRoleName());
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					throw new NotFoundException("Guild Role ID doesn't exist");
				}
				return rs.getInt("id");
			}
		}
	}
	public int getUserGuildRoleId(Connection con, GuildData guildData) throws SQLException, NotFoundException{
		String query = "SELECT id FROM user_guild_role WHERE account_id = ? AND guild_role_id = ?";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, getAccountId(con,guildData));
			stmt.setInt(2, getGuildRoleId(con,guildData));
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					throw new NotFoundException("User Guild Role ID doesn't exist");
				}
				return rs.getInt("id");
			}
		}
	}
	public void insert(Connection con, GuildData guildData) throws SQLException, NotFoundException{
		String accountId = getAccountId(con, guildData);
		int guildRoleId = getGuildRoleId(con, guildData);
		String query = "INSERT INTO user_guild_role(account_id, guild_role_id) VALUES (?, ?)";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, accountId);
			stmt.setInt(2, guildRoleId);
			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0)
				throw new SQLException("Insert role is failed, no row is effected!");
		}

	}
	public void update(Connection con, GuildData guildData) throws SQLException, NotFoundException{
		int userGuildRoleId = getUserGuildRoleId(con, guildData);
		String query = "UPDATE user_guild_role SET guild_role_id = ? WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(2, userGuildRoleId);
		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0)
			throw new SQLException("Update role is failed, no row is effected!");
	}
	public void delete(Connection con, GuildData guildData) throws SQLException, NotFoundException{
		int userGuildRoleId = getUserGuildRoleId(con, guildData);
		String query = "DELETE FROM user_guild_role WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, userGuildRoleId);
		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0)
			throw new SQLException("Delete role is failed, no row is effected!");
	}
}
