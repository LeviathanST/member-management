package models.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import data.CrewData;
import exceptions.NotFoundException;
import models.Crew;

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

	public static List<CrewRole> getAllByCrewId(Connection con, int crew_id) throws SQLException {
		String query = """
				SELECT name
				FROM crew_role
				WHERE crew_id = ?
				""";
		List<CrewRole> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, crew_id);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			list.add(new CrewRole(crew_id, rs.getString("name"), con));
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

	public int getCrewRoleId(Connection con, CrewData crewData) throws SQLException, NotFoundException {
		crewData.setCrewRoleId(Crew.getCrewId(con, crewData));
		String query = "SELECT * FROM crew_role WHERE crew_id = ? AND name  = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, crewData.getCrewRoleId());
		stmt.setString(2, crewData.getName());
		ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			throw new NotFoundException("Crew role id is not existed!");
		return rs.getInt("id");
	}

	public void insertCrewMember(Connection con, CrewData data) throws SQLException, NotFoundException {
		int crew_role_id = getCrewRoleId(con, data);
		String query = "INSERT INTO user_crew_role(account_id, crew_role_id) VALUES (?, ?)";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, data.getAccount_id());
		stmt.setInt(2, crew_role_id);
		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("Insert failured");
	}

	public void updateCrewMember(Connection con, CrewData data, int newCrewRoleId)
			throws SQLException, NotFoundException {
		int crew_role_id = getCrewRoleId(con, data);
		String query = "UPDATE user_crew_role SET crew_role_id = ? WHERE account_id = ? AND crew_role_id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, newCrewRoleId);
		stmt.setString(2, data.getAccount_id());
		stmt.setInt(3, crew_role_id);
		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("Update failured");
	}

	public void deleteCrewMember(Connection con, CrewData crewData) throws SQLException, NotFoundException {
		String query = "DELETE FROM user_crew_role WHERE account_id = ? AND crew_role_id = ?";
		int crew_role_id = getCrewRoleId(con, crewData);
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, crewData.getAccount_id());
		stmt.setInt(2, crew_role_id);
		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("Delete failured");
	}

}
