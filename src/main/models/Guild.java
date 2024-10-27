package models;

import java.sql.*;

import exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class Guild {
	public static void insert(Connection con, String name)
			throws SQLException, SQLIntegrityConstraintViolationException {
		String query = "INSERT INTO guild (name) VALUES (?)";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, name);
			stmt.executeUpdate();

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Insert guild %s is failed", name));
			}
		}
	}

	public static void update(Connection con, int guildId, String newName) throws SQLException {
		String query = "UPDATE guild SET name = ? WHERE id = ?";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, newName);
			stmt.setInt(2, guildId);
			stmt.executeUpdate();

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
			stmt.executeUpdate();

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException("Delete guild is failed");
			}
		}
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
