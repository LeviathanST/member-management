package models.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import constants.GuildType;
import data.GuildData;
import exceptions.NotFoundException;

public class GuildRole {
	private String name;
	private int guild_id;
	private Connection con;

	private GuildRole(int guild_id, String name, Connection con) {
		this.guild_id = guild_id;
		this.name = name;
		this.con = con;
	}

	public int getId() {return guild_id;}

	public static List<GuildRole> getAllByGuildId(Connection con, int guild_id) throws SQLException {
		String query = """
				SELECT name
				FROM guild_role
				WHERE guild_id = ?
				""";
		List<GuildRole> list = new ArrayList<GuildRole>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, guild_id);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			list.add(new GuildRole(guild_id, rs.getString("name"), con));
		}

		return list;
	}

	public static List<GuildRole> getAllByAccountId(Connection con, String account_id)
			throws SQLException, NotFoundException {
		String query = """
					SELECT gr.guild_id as id, gr.name as name
					FROM user_account ua
					JOIN user_guild_role ugr ON ugr.account_id = ua.id
					JOIN guild_role gr ON gr.id = ugr.guild_role_id
					WHERE ua.id = ?
				""";

		List<GuildRole> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, account_id);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			GuildRole gr = new GuildRole(rs.getInt("id"), rs.getString("name"), con);
			list.add(gr);
		}

		return list;
	}
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
				return new GuildRole(rs.getInt("id"), name,con);
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
