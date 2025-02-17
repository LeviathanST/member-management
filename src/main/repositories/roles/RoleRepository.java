package repositories.roles;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import config.Database;
import constants.RoleContext;
import exceptions.NotFoundException;
import models.Role;
import repositories.permissions.PermissionRepository;

public class RoleRepository {

	public static List<Role> getAll() throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT * FROM role";
			List<Role> list = new ArrayList<>();

			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(new Role(rs.getInt("id"), rs.getString("name")));
			}

			return list;
		}

	}

	public static Role getByName(String name)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT id FROM role WHERE name = ?";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				return new Role(rs.getInt("id"), name);
			}

			throw new NotFoundException("Your specified role is not found!");
		}

	}

	public static Role getByAccountId(String account_id)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
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

	}

	public static void createRole(String nameRole) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					INSERT INTO role (name) VALUES (?)
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, nameRole);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Create role failed : now row is affected!");
		}

	}

	public static void updateRole(int roleId, String newNameRole)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					UPDATE role
					SET name = ?
					WHERE id = ?
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, newNameRole);
			stmt.setInt(2, roleId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Update role failed : now row is affected!");
		}

	}

	public static void deleteRole(int roleId) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					DELETE FROM role
					WHERE id = ?
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, roleId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Delete role failed : now row is affected!");
		}
	}

	public static void addPermissionRole(String namePermission, String nameRole)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			Role role = getByName(nameRole);
			int permissionId = PermissionRepository.getIdByName(namePermission);
			String query = """
					INSERT INTO role_permission(role_id, permission_id) VALUES (?, ?)
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, role.getId());
			stmt.setInt(2, permissionId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Add permission to role is failed : no row is affected!");
		}
	}

	public static boolean existPermissionWithContext(RoleContext ctx, String permission, String accountId)
			throws SQLException {
		String context = switch (ctx) {
			case RoleContext.APP -> "app";
			case RoleContext.GUILD -> "guild";
			case RoleContext.CREW -> "crew";
			default -> throw new IllegalArgumentException("Role context not found!");
		};

		String query = """
				SELECT EXISTS (
					SELECT 1 FROM permission p
							JOIN role_permission rp ON rp.permission_id = p.id
							JOIN user_role ur ON ur.role_id = rp.role_id
							WHERE ur.account_id = ?
							AND p.name = ?
							AND p.context = ?
							LIMIT 1
				)

						""";

		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, accountId);
			stmt.setString(2, permission);
			stmt.setString(3, context);
			ResultSet checked = stmt.executeQuery();

			if (checked.next()) {
				return checked.getBoolean(1);
			} else {
				return false;
			}

		}
	}
}
