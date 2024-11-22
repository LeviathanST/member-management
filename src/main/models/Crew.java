package models;

import exceptions.NotFoundException;
import models.users.UserAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Crew {
	public static void insert(Connection con, String name)
			throws SQLException, SQLIntegrityConstraintViolationException {
		String query = "INSERT INTO crew (name) VALUES (?)";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, name);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Insert crew %s is failed", name));
			}
		}
	}
	public static List<String> getMemberInCrew(Connection con, int crewId)
			throws SQLException, NotFoundException {

		String query = """
				SELECT ucr.account_id as account_id
				FROM crew g
				JOIN crew_role cr ON cr.guild_id = c.id
				JOIN user_crew_role ucr ON ucr.crew_role_id = cr.id
				WHERE c.id = ?
				""";

		List<String> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, crewId);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			list.add(UserAccount.getNameById(con,rs.getString("account_id")));
		}

		return list;
	}

	public static void update(Connection con, int crewId, String newName) throws SQLException {
		String query = "UPDATE crew SET name = ? WHERE id = ?";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, newName);
			stmt.setInt(2, crewId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Update name crew into %s is failed", newName));
			}
		}
	}

	public static void delete(Connection con, int crewId) throws SQLException {
		String query = "DELETE FROM crew WHERE id = ?";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, crewId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException("Delete crew is failed");
			}
		}
	}

	public static void getAllName(Connection con) throws SQLException {
		String query = "SELECT name FROM crew ";
		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("name"));
		}
	}

	public static int getIdByName(Connection con, String name) throws SQLException, NotFoundException {
		String query = "SELECT id FROM crew WHERE name = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			return rs.getInt("id");
		}

		throw new NotFoundException("Crew ID is not existed!");
	}
	public static String getNameByID(Connection con, int id) throws SQLException, NotFoundException {
		String query = "SELECT name FROM crew WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			return rs.getString("name");
		}

		throw new NotFoundException("Crew is not existed!");
	}

	public static List<String> getAllNameToList(Connection con) throws SQLException {
		List<String> crewNames = new ArrayList<>();
		String query = "SELECT name FROM crew ";
		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			crewNames.add(rs.getString("name"));
		}
		return crewNames;
	}
	public static List<String> getAllCrewByAccountId(Connection con, String accountId)
			throws SQLException {

		String query = """
				SELECT c.name as name
				FROM crew g
				JOIN crew_role cr ON cr.crew_id = c.id
				JOIN user_crew_role ucr ON ucr.crew_role_id = cr.id
				WHERE ucr.account_id = ?
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
}
