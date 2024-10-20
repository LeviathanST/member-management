package models.permissions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.NotFoundException;

public class CrewPermission {
	private int id;
	private String name;

	private Connection con;

	public CrewPermission(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public int getId() {
		return id;
	}

	public static List<CrewPermission> getAllByAccountIdAndGuildId(Connection con, String accountId,
			int guildId)
			throws SQLException {

		String query = """
				SELECT cp.name as name, cp.id as id
				FROM crew c
				JOIN crew_role cr ON cr.crew_id = c.id
				JOIN crew_role_permission crp ON crp.crew_role_id = cr.id
				JOIN crew_permission cp ON cp.id = crp.crew_permission_id
				JOIN user_crew_role ucr ON ucr.crew_role_id = cr.id
				WHERE c.id = ? AND ucr.account_id = ?
				""";

		List<CrewPermission> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, guildId);
		stmt.setString(2, accountId);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("name"));
			list.add(new CrewPermission(rs.getInt("id"), rs.getString("name")));
		}

		return list;
	}

	public static List<CrewPermission> getAllByCrewRoleId(Connection con, int crewRoleId)
			throws SQLException, NotFoundException {
		String query = "SELECT id, name FROM crew_role_permission WHERE crew_role_id = ?";
		List<CrewPermission> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, crewRoleId);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			list.add(new CrewPermission(rs.getInt("id"), rs.getString("name")));
		}

		return list;
	}

	public void insert(String name) throws SQLException {
		String query = "INSERT INTO permission (name) VALUES (?)";

		PreparedStatement stmt = this.con.prepareStatement(query);
		stmt.setString(1, name);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when adding!");

		System.out.println("Add a permission successfully!");
	}

	public void update(String newName) throws SQLException {
		String query = "UPDATE permission SET name = ? where name = ?";

		PreparedStatement stmt = this.con.prepareStatement(query);
		stmt.setString(1, this.name);
		stmt.setString(2, newName);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when update!");

		System.out.println("Update a permission successfully!");
	}

	public void delete(String name) throws SQLException {
		String query = "DELETE FROM permission WHERE name = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when deleting!");

		System.out.println("Delete a permission successfully!");
	}

}
