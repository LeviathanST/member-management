package models;

import exceptions.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class Crew {
	public static void insert(Connection con, String name)
			throws SQLException, SQLIntegrityConstraintViolationException {
		String query = "INSERT INTO crew (name) VALUES (?)";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, name);
			stmt.executeUpdate();

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Insert crew %s is failed", name));
			}
		}
	}

	public static void update(Connection con, int crewId, String newName) throws SQLException {
		String query = "UPDATE crew SET name = ? WHERE id = ?";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, newName);
			stmt.setInt(2, crewId);
			stmt.executeUpdate();

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
			stmt.executeUpdate();

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
}
