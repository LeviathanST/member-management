package repositories.roles;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.Database;
import exceptions.NotFoundException;
import models.GuildRole;

public class GuildRoleRepository {
	/// Find role id by name of a specified guild
	public static int getIdByName(int guild_id, String name) throws SQLException,
			NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
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

	}

	public static List<GuildRole> getAllByGuildId(int guild_id)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					SELECT name
					FROM guild_role
					WHERE guild_id = ?
					""";
			List<GuildRole> list = new ArrayList<>();

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, guild_id);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(new GuildRole(guild_id, rs.getString("name")));
			}

			return list;
		}

	}

	public static List<GuildRole> getAllByAccountId(String account_id)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
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
				GuildRole gr = new GuildRole(rs.getInt("id"), rs.getString("name"));
				list.add(gr);
			}

			return list;
		}

	}

	public static void insertGuildRole(String role, int guildId)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "INSERT INTO guild_role (name, guild_id) VALUES (?, ?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, role);
			stmt.setInt(2, guildId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Insert guild role to database is failed!");
		}

	}

	public static void updateGuildRole(String newRole, int guildRoleId)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					UPDATE guild_role
					SET name = ?
					WHERE id = ?
					""";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, newRole);
			stmt.setInt(2, guildRoleId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Update guild role from database is failed!");
		}

	}

	public static void deleteGuildRole(int guildRoleId)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "DELETE FROM guild_role WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, guildRoleId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Delete guild role from database is failed!");
		}

	}

}
