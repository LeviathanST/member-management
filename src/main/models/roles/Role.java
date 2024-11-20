package models.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.NotFoundException;
import models.permissions.Permission;

public class Role {
	private int id;
	private String name;

	public Role(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public static List<Role> getAll(Connection con) throws SQLException {
		String query = "SELECT * FROM role";
		List<Role> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			list.add(new Role(rs.getInt("id"), rs.getString("name")));
		}

		return list;
	}

	public static Role getByName(Connection con, String name) throws SQLException, NotFoundException {
		String query = "SELECT id FROM role WHERE name = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			return new Role(rs.getInt("id"), name);
		}

		throw new NotFoundException("Your specified role is not found!");
	}

	public static Role getByAccountId(Connection con, String account_id) throws SQLException, NotFoundException {
		String query = """
				SELECT r.id as id, r.name as name
				FROM user_role ur
				JOIN role r ON r.id = ur.role_id
				WHERE ur.account_id = ?
				""";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, account_id);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			return new Role(rs.getInt("id"), rs.getString("name"));
		}

		throw new NotFoundException("This account is not have any role in application!");
	}

	public static void createRole(Connection con, String nameRole) throws SQLException{
		String query = """
				INSERT INTO role (name) VALUES (?)
				""";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, nameRole);
		int row = stmt.executeUpdate();
		if(row == 0)
			throw new SQLException("Create role failed : now row is affected!");
	}

	public static void updateRole(Connection con, int roleId, String newNameRole) throws SQLException{
		String query = """
				UPDATE role
				SET name = ?
				WHERE id = ?
				""";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, newNameRole);
		stmt.setInt(2, roleId);
		int row = stmt.executeUpdate();
		if(row == 0)
			throw new SQLException("Update role failed : now row is affected!");
	}

	public static void deleteRole(Connection con, int roleId) throws SQLException{
		String query = """
				DELETE FROM role
				WHERE id = ?
				""";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, roleId);
		int row = stmt.executeUpdate();
		if(row == 0)
			throw new SQLException("Delete role failed : now row is affected!");
	}

	public static void addPermissionRole(Connection con, String namePermission, String nameRole) throws SQLException, NotFoundException {
		Role role = getByName(con, nameRole);
		int permissionId = Permission.getIdByName(con, namePermission);
		String query = """
				INSERT INTO role_permission(role_id, permission_id) VALUES (?, ?)
				""";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, role.getId());
		stmt.setInt(2, permissionId);
		int row = stmt.executeUpdate();
		if(row == 0)
			throw new SQLException("Add permission to role is failed : no row is affeccted!");
	}
}
