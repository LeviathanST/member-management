package models;

import java.sql.*;

import exceptions.NotFoundException;
import models.permissions.GuildPermission;
import models.users.UserAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Guild {
	public static void insert(Connection con, String name)
			throws SQLException, SQLIntegrityConstraintViolationException{
		String query = "INSERT INTO guild (name) VALUES (?)";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, name);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Insert guild %s is failed", name));
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(e);
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	public static void update(Connection con, int guildId, String newName) throws SQLException {
		String query = "UPDATE guild SET name = ? WHERE id = ?";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, newName);
			stmt.setInt(2, guildId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Update name guild into %s is failed", newName));
			}
		}
	}

	public static void delete(Connection con, int guildId) throws SQLException {
		String query = "DELETE FROM guild WHERE id = ?";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, guildId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException("Delete guild is failed");
			}
		}
	}
	public static String getNameByID(Connection con, int id) throws SQLException, NotFoundException {
		String query = "SELECT name FROM guild WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			return rs.getString("name");
		}

		throw new NotFoundException("Guild is not existed!");
	}

	public static void getAllName(Connection con) throws SQLException {
		String query = "SELECT name FROM guild ";
		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("name"));
		}
	}

	public static int getIdByName(Connection con, String name) throws SQLException, NotFoundException {
		String query = "SELECT id FROM guild WHERE name = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			return rs.getInt("id");
		}

		throw new NotFoundException("Guild ID is not existed!");
	}
	public static List<String> getAllGuildByAccountId(Connection con, String accountId)
			throws SQLException {

		String query = """
				SELECT g.name as name
				FROM guild g
				JOIN guild_role gr ON gr.guild_id = g.id
				JOIN user_guild_role ugr ON ugr.guild_role_id = gr.id
				WHERE ugr.account_id = ?
				""";

		List<String> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, accountId);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			list.add(rs.getString("name"));
		}

		return list;
	}
	public static List<String> getMemberInGuild(Connection con, int guildId)
            throws SQLException, NotFoundException {

		String query = """
				SELECT ugr.account_id as account_id
				FROM guild g
				JOIN guild_role gr ON gr.guild_id = g.id
				JOIN user_guild_role ugr ON ugr.guild_role_id = gr.id
				WHERE g.id = ?
				""";

		List<String> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, guildId);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			list.add(UserAccount.getNameById(con,rs.getString("account_id")));
		}

		return list;
	}

	public static List<String> getAllNameToList(Connection con) throws SQLException {
		List<String> guildNames = new ArrayList<>();
		String query = "SELECT name FROM guild ";
		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			guildNames.add(rs.getString("name"));
		}
		return guildNames;
	}
}
