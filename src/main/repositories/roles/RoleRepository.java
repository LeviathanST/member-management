package repositories.roles;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public static void create(String nameRole) throws SQLException, IOException, ClassNotFoundException {
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

	public static void update(int roleId, String newNameRole)
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

	public static void delete(int roleId) throws SQLException, IOException, ClassNotFoundException {
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

	/// NOTE:
	/// @param permissions
	/// Using for multiple deleted permissions
	public static void deletePermission(String roleName, List<String> permissions)
			throws SQLException, IOException, ClassNotFoundException {
		String placeholders = permissions.stream()
				.map(p -> "?")
				.collect(Collectors.joining(", "));

		try (Connection con = Database.connection()) {
			String query = """
					DELETE
					FROM role_permission rp
						JOIN role r ON r.id = rp.role_id
					WHERE
						r.name = ?
						AND EXISTS (
							SELECT 1
							FROM permission
							WHERE name IN (%s)
						)
					""".formatted(placeholders);
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, roleName);

			int i = 1;
			for (String permission : permissions) {
				stmt.setString(i++, permission);
			}

			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Delete role failed");
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
				throw new SQLException("Add permission to role failed");
		}
	}

	public static boolean existPermissionWithContext(RoleContext ctx, String permission, String accountId)
			throws SQLException {

		String query = """
				SELECT 1
				FROM permission p
					JOIN role_permission rp ON rp.permission_id = p.id
					JOIN user_role ur ON ur.role_id = rp.role_id
				WHERE ur.account_id = ?
					AND p.name = ?
					AND p.context = ?
				""";

		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, accountId);
			stmt.setString(2, permission);
			stmt.setString(3, ctx.name().toLowerCase());
			ResultSet checked = stmt.executeQuery();

			if (checked.next()) {
				return checked.getBoolean(1);
			} else {
				return false;
			}

		}
	}

	/// NOTE:
	/// @param prefix: determine by @param ctx
	/// Sample: BE1, Technical, Media
	public static List<String> getAllByPrefix(String prefix) throws SQLException {
		List<String> list = new ArrayList<>();
		String query = """
					SELECT name from role
					WHERE name LIKE ?
				""";

		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, prefix + "%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("name"));
			}
			return list;
		}
	}

	public static List<String> getAllPermissionByContext(RoleContext ctx) throws SQLException {
		List<String> list = new ArrayList<>();
		String query = """
					SELECT name from permission
					WHERE context = ?
				""";

		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, ctx.name().toLowerCase());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("name"));
			}
			return list;
		}
	}

	/// NOTE:
	/// @param ctx:
	/// - Prevent from crew but not from another guild
	public static List<String> getAllPermissionOfARole(String name)
			throws SQLException {
		Logger logger = LoggerFactory.getLogger(RoleRepository.class);
		logger.info("Role: " + name);
		List<String> list = new ArrayList<>();
		String query = """
				SELECT p.name
				FROM role r
					JOIN role_permission rp ON rp.role_id = r.id
					JOIN permission p ON p.id = rp.permission_id
				WHERE
					r.name = ?
				""";

		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("name"));
			}
			return list;
		}
	}

	/// NOTE:
	/// Use for crew and guild
	/// @param prefix
	/// Sample: BE1, FE1, IA1
	/// @param ctx: route context
	/// Prevent guild/name=BE1 and crew/name=technical
	public static boolean existPermissionWithPrefix(String prefix, RoleContext ctx, String permission,
			String accountId)
			throws SQLException {
		String query = """
				SELECT 1
				FROM role r
					JOIN user_role ur ON ur.role_id = r.id
					JOIN role_permission rp ON rp.role_id = ur.role_id
					JOIN permission p ON p.id = rp.permission_id
				WHERE
					p.name = ?
					AND p.context = ?
					AND r.name like ?
					AND ur.account_id = ?
				""";

		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, permission);
			stmt.setString(2, ctx.name().toLowerCase());
			stmt.setString(3, prefix + "%");
			stmt.setString(4, accountId);

			ResultSet rs = stmt.executeQuery();
			Logger logger = LoggerFactory.getLogger(RoleRepository.class);

			if (rs.next()) {
				logger.info("Hi");
				return true;
			} else {
				return false;
			}
		}
	}
}
