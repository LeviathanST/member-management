package models.permissions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class Permission {
	private int id;
	private String name;

	private Connection con;

	public Permission() {
		
	}

	public Permission(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public static List<Permission> getAllPermission(Connection con) throws SQLException {
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

	public static int getIdByName(Connection con, String namePermission) throws SQLException {
		String query = """
				SELECT id FROM permission WHERE name = ? 
				""";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, namePermission);
		ResultSet rs = stmt.executeQuery();
		while(!rs.next())
			throw new SQLException("Permission is not existed!");
		return rs.getInt("id");

	}

	public static List<Permission> getByAccountId(Connection con, String accountId) throws SQLException {
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
			System.out.println(rs.getString("name"));
			list.add(new Permission(rs.getInt("id"), rs.getString("name")));
		}

		return list;
	}

	public static void insert(String name, Connection con) throws SQLException {
		String query = "INSERT INTO permission (name) VALUES (?)";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when adding!");

		System.out.println("Add a permission successfully!");
	}

	public void update(String newName, Connection con) throws SQLException {
		String query = "UPDATE permission SET name = ? where name = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, this.name);
		stmt.setString(2, newName);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when update!");

		System.out.println("Update a permission successfully!");
	}

	public static void delete(String name, Connection con) throws SQLException {
		String query = "DELETE FROM permission WHERE name = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when deleting!");

		System.out.println("Delete a permission successfully!");
	}
	public static void addPermissionToRole(Connection con, int roleID, int permissionId) throws SQLException {
		String query = "INSERT INTO role_permission (role_id,permission_id) VALUES (?,?)";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, roleID);
		stmt.setInt(2, permissionId);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when adding to guild role!");
	}
	public static void updatePermissionToRole(int newPermissionID,int permissionID,int roleID, Connection con) throws SQLException {
		String query = "UPDATE role_permission SET permission_id = ? WHERE permission_id = ? AND role_id = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, newPermissionID);
		stmt.setInt(2, permissionID);
		stmt.setInt(3, roleID);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A guild role permission is failed when update!");
	}
	public static void deletePermissionRole(int permissionID,int roleID, Connection con) throws SQLException {
		String query = "DELETE FROM role_permission WHERE permission_id = ? AND role_id = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, permissionID);
		stmt.setInt(2, roleID);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A guild role permission is failed when deleting!");
	}
}
