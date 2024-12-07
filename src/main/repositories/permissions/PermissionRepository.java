package repositories.permissions;

import config.Database;
import models.Permission;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class PermissionRepository {
	public static List<Permission> getAllPermission()
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					SELECT * FROM permission
					""";

			List<Permission> list = new ArrayList<>();

			PreparedStatement stmt = con.prepareStatement(query);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new Permission(rs.getInt("id"), rs.getString("name")));
			}

			return list;
		}

	}

	public static int getIdByName(String namePermission) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					SELECT id FROM permission WHERE name = ?
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, namePermission);
			ResultSet rs = stmt.executeQuery();
			while (!rs.next())
				throw new SQLException("Permission is not existed!");
			return rs.getInt("id");
		}
	}

	public static List<Permission> getByAccountId(String accountId)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					SELECT p.id as id, p.name as name
					FROM user_role ur
					JOIN role_permission rp ON rp.role_id = ur.role_id
					JOIN permission p ON p.id = rp.permission_id
					WHERE ur.account_id = ?
					""";

			List<Permission> list = new ArrayList<>();

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, accountId);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new Permission(rs.getInt("id"), rs.getString("name")));
			}

			return list;
		}

	}

	public static void insert(String name) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "INSERT INTO permission (name) VALUES (?)";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);

			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("A permission is failed when adding!");
		}
	}

	public static void update(int id, String newName) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "UPDATE permission SET name = ? where id = ?";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, newName);
			stmt.setInt(2, id);

			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("A permission is failed when update!");
		}
	}

	public static void delete(int permissionId) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			deleteRolePermission(permissionId);
			String query = "DELETE FROM permission WHERE id = ?";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, permissionId);

			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("A permission is failed when deleting!");
		}
	}

	public static void addPermissionToRole(int roleID, int permissionId)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "INSERT INTO role_permission (role_id,permission_id) VALUES (?,?)";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, roleID);
			stmt.setInt(2, permissionId);

			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("A permission is failed when adding to guild role!");
		}
	}

	public static void updatePermissionToRole(int roleID, int permissionID, int newPermissionID)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "UPDATE role_permission SET permission_id = ? WHERE permission_id = ? AND role_id = ?";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, newPermissionID);
			stmt.setInt(2, permissionID);
			stmt.setInt(3, roleID);

			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("A application role permission is failed when update!");
		}

	}
		public static void deleteRolePermission(int permissionID)
			throws SQLException, IOException, ClassNotFoundException {
			try (Connection con = Database.connection()) {
				String query = "DELETE FROM role_permission WHERE permission_id = ? ";

				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setInt(1, permissionID);

				int row = stmt.executeUpdate();
				if (row == 0)
					throw new SQLException("A application role permission is failed when deleting!");
			}
	}


	public static void deletePermissionRole(int permissionID, int roleID)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "DELETE FROM role_permission WHERE permission_id = ? AND role_id = ?";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, permissionID);
			stmt.setInt(2, roleID);

			int row = stmt.executeUpdate();
			delete(permissionID);
			if (row == 0)
				throw new SQLException("A application role permission is failed when deleting!");
		}
	}
}
