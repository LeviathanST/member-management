package models.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.GuildDTO;
import exceptions.NotFoundException;
import models.Guild;
import models.users.UserAccount;

public class GuildRole {
	private String name;
	private int guild_id;
	private Connection con;

	private GuildRole(int guild_id, String name, Connection con) {
		this.guild_id = guild_id;
		this.name = name;
		this.con = con;
	}

	public int getGuild_id() {return guild_id;}
	public String getName() {return name;}

	/// Find role id by name of a specified guild
	public static int getIdByName(Connection con, int guild_id, String name) throws SQLException,
			NotFoundException {
		String query = "SELECT id FROM guild_role WHERE name = ? AND guild_id = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);
		stmt.setInt(2, guild_id);

		ResultSet rs = stmt.executeQuery();

		if (!rs.next()) {
			throw new NotFoundException("Specified guild role name not found!");
		} else {
			return rs.getInt("id");
		}
	}

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

	public static void insertGuildRole(Connection con, String role, int guildId)
			throws SQLException, NotFoundException {
		String query = "INSERT INTO guild_role (name, guild_id) VALUES (?, ?)";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, role);
		stmt.setInt(2, guildId);
		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("Insert guild role to database is failed!");
	}

	public static void updateGuildRole(Connection con, String newRole, int guildRoleId, int newGuildId)
			throws SQLException, NotFoundException {
		String query = """
				UPDATE guild_role
				SET name = ?, guild_id = ?
				WHERE id = ?
				""";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, newRole);
		stmt.setInt(2, newGuildId);
		stmt.setInt(3, guildRoleId);
		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("Update guild role from database is failed!");
	}

	public static void deleteGuildRole(Connection con, int guildRoleId)
			throws SQLException, NotFoundException {
		String query = "DELETE FROM guild_role WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, guildRoleId);
		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("Delete guild role from database is failed!");
	}

}
