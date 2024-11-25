package models.roles;

import config.Database;
import exceptions.NotFoundException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrewRole {
	private int crew_id;
	private String name;
	private Connection con;

	public CrewRole(int crew_id, String name, Connection con) {
		this.crew_id = crew_id;
		this.name = name;
		this.con = con;
	}

	public int getCrewId() {
		return this.crew_id;
	}

	public String getName() {
		return this.name;
	}

	/// Find role id by name of a specified crew
	public static int getIdByName( int crew_id, String name)
            throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "SELECT id FROM crew_role WHERE name = ? AND crew_id = ?";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);
			stmt.setInt(2, crew_id);

			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) {
				throw new NotFoundException("Specified crew name is not found!");
			} else {
				return rs.getInt("id");
			}
		}

	}

	public static List<CrewRole> getAllByCrewId( int crewId) throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = """
				SELECT name
				FROM crew_role
				WHERE crew_id = ?
				""";
			List<CrewRole> list = new ArrayList<>();

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, crewId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(new CrewRole(crewId, rs.getString("name"), con));
			}

			return list;
		}

	}

	public static List<CrewRole> getAllByAccountId( String account_id)
            throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = """
					SELECT cr.crew_id AS id, cr.name AS name
					FROM user_account ua
					JOIN user_crew_role ucr ON ucr.account_id = ua.id
					JOIN crew_role cr ON cr.id = ucr.crew_role_id
					WHERE ua.id = ?
				""";

			List<CrewRole> list = new ArrayList<>();

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, account_id);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				CrewRole cr = new CrewRole(rs.getInt("id"), rs.getString("name"), con);
				list.add(cr);
			}

			return list;
		}

	}

	public static int getIdsByCrewId(int crew_id) throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "SELECT id FROM crew_role WHERE crew_id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, crew_id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next())
				throw new NotFoundException("Crew role id is not existed!");
			return rs.getInt("id");
		}

	}

	public static void insertCrewRole( String role, int crewId)
            throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "INSERT INTO crew_role(name, crew_id) VALUES (?, ?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, role);
			stmt.setInt(2, crewId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Insert crew role to database is failed!");
		}

	}

	public static void updateCrewRole(String newRole, int crewRoleId, int newCrewId)
            throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = """
				UPDATE crew_role
				SET name = ?, crew_id = ?
				WHERE id = ?
				""";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, newRole);
			stmt.setInt(2, newCrewId);
			stmt.setInt(3, crewRoleId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Update crew role from database is failed!");
		}

	}

	public static void deleteCrewRole( int crewRoleId)
            throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "DELETE FROM crew_role WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, crewRoleId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Delete crew role from database is failed!");
		}

	}
}
