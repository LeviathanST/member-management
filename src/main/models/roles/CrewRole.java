package models.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.NotFoundException;
import java.sql.ResultSet;

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
	public static int getIdByName(Connection con, int crew_id, String name)
			throws SQLException, NotFoundException {
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

	public static List<CrewRole> getAllByCrewId(Connection con, int crewId) throws SQLException {
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

	public static List<CrewRole> getAllByAccountId(Connection con, String account_id)
			throws SQLException, NotFoundException {
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

	public static int getIdsByCrewId(Connection con, int crew_id) throws SQLException, NotFoundException {
		String query = "SELECT id FROM crew_role WHERE crew_id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, crew_id);
		ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			throw new NotFoundException("Crew role id is not existed!");
		return rs.getInt("id");
	}

	public static void insertCrewMember(Connection con, String accountId, int crewRoleId)
			throws SQLException, NotFoundException {
		String query = "INSERT INTO user_crew_role(account_id, crew_role_id) VALUES (?, ?)";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, accountId);
		stmt.setInt(2, crewRoleId);
		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("Insert member to crew is failed!");
	}

	public static void updateCrewMember(Connection con, String accountId, int crewId, int newCrewRoleId)
			throws SQLException, NotFoundException {
		String query = """
				UPDATE user_crew_role ucr
				JOIN crew_role cr ON cr.id = ucr.crew_role_id
				SET ucr.crew_role_id = ?
				WHERE ucr.account_id = ? AND cr.crew_id = ?
				""";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, newCrewRoleId);
		stmt.setString(2, accountId);
		stmt.setInt(3, crewId);
		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("Update member from crew is failed!");
	}

	public static void deleteCrewMember(Connection con, String accountId, int crewId)
			throws SQLException, NotFoundException {
		String query = "DELETE FROM user_crew_role WHERE account_id = ? AND crew_role_id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, accountId);
		stmt.setInt(2, crewId);
		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException(String.format("Delete member from crew is failed!"));
	}
}
