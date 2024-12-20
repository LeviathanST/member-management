package repositories.permissions;

import config.Database;
import exceptions.NotFoundException;
import models.CrewPermission;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrewPermissionRepository {
	public static List<CrewPermission> getAllByAccountIdAndCrewId(String accountId, int crewId)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
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
			stmt.setInt(1, crewId);
			stmt.setString(2, accountId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getString("name"));
				list.add(new CrewPermission(rs.getInt("id"), rs.getString("name")));
			}

			return list;
		}
	}

	public static List<String> getAllByCrewRoleId(int crewRoleId)
			throws SQLException, NotFoundException, NullPointerException, IOException,
			ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT  crew_permission_id FROM crew_role_permission WHERE crew_role_id = ?";
			List<String> list = new ArrayList<>();

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, crewRoleId);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				list.add(getNameById(rs.getInt("crew_permission_id")));

				while (rs.next()) {

					list.add(getNameById(rs.getInt("crew_permission_id")));
				}
			} else {
				throw new NullPointerException("Null Data");
			}

			return list;
		}

	}

	public static String getNameById(int id)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT name FROM crew_permission WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("name");
			}

			throw new NotFoundException("Crew Permission is not existed!");
		}

	}

	public static void insert(String name) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "INSERT INTO crew_permission (name) VALUES (?)";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);

			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("A permission is failed when adding!");

		}

	}

	public static void update(String name, String newName)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "UPDATE crew_permission SET name = ? where name = ?";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, newName);
			stmt.setString(2, name);

			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("A permission is failed when update!");

		}

	}

	public static void delete(String name) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "DELETE FROM crew_permission WHERE name = ?";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);

			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("A permission is failed when deleting!");

		}

	}

	public static List<String> getAllCrewPermission()
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT name FROM crew_permission";
			List<String> list = new ArrayList<>();

			PreparedStatement stmt = con.prepareStatement(query);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("name"));
			}

			return list;
		}

	}

	public static void addPermissionToCrewRole(int crewRoleId, int permissionId)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "INSERT INTO crew_role_permission (crew_role_id,crew_permission_id) VALUES (?,?)";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, crewRoleId);
			stmt.setInt(2, permissionId);

			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("A permission is failed when adding to crew role!");
		}

	}

	public static void updatePermissionInCrewRole(int newPermissionID, int permissionID, int crewRoleId)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "UPDATE crew_role_permission SET crew_permission_id = ? WHERE crew_permission_id = ? AND crew_role_id = ?";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, newPermissionID);
			stmt.setInt(2, permissionID);
			stmt.setInt(3, crewRoleId);

			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("A crew role permission is failed when update!");
		}

	}

	public static void deletePermissionInCrewRole(int permissionID, int crewRoleId)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "DELETE FROM crew_role_permission WHERE crew_permission_id = ? AND crew_role_id = ?";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, permissionID);
			stmt.setInt(2, crewRoleId);

			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("A crew role permission is failed when deleting!");
		}

	}

	public static int getIdByName(String permission)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT id FROM crew_permission WHERE name = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, permission);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("id");
			}

			throw new NotFoundException("Crew Permission is not existed!");
		}

	}

}
