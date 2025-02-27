package repositories;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import config.Database;
import constants.RoleContext;
import dto.role.GetUserDTO;
import exceptions.NotFoundException;
import models.Role;
import utils.Pressessor;

public class RoleRepository {
	/// NOTE:
	/// Delete role then convert all user assigned to the role to the role default
	/// of that context
	public static void delete(String prefix, String roleName) throws SQLException {
		String query = """
					CALL removeRoleAndReassigned(?, ?, @row)
				""";
		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, prefix);
			stmt.setString(2, prefix + "_" + roleName);
			stmt.execute();
		}
	}

	public static void create(String roleName) throws SQLException {
		String query = """
					INSERT INTO role (name)
					VALUES (?)
				""";

		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, roleName);

			int row = stmt.executeUpdate();
			if (row <= 0) {
				throw new SQLException("Add role failed");
			}
		}
	}

	/// NOTE:
	/// @param ctx:
	/// LIKE or NOT LIKE for filter
	public static void addDefaultForUserByPrefix(String username, String prefix) throws SQLException {
		String query = """
					INSERT INTO user_role (account_id, role_id)
					VALUES (
						(SELECT id FROM user_account WHERE username = ?),
						(SELECT id FROM role WHERE name LIKE ? AND is_default = true)
					)
				""";

		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, prefix + "_%");

			int row = stmt.executeUpdate();
			if (row <= 0) {
				throw new SQLException("Add default role for user is failed");
			}
		}
	}

	/// TODO: We can change multi query into procedure
	/// NOTE:
	/// This function will be rollback if one of actions is failed.
	/// @param prefix:
	/// - We need to remove all roles in the same guild, crew, app before insert new
	/// one for the user.
	/// @param roleName:
	/// - The specified role is assigned for the user.
	public static void updateSpecifiedForUserWithPrefix(String prefix, String username, String roleName)
			throws SQLException {
		String insertQuery = """
				INSERT INTO user_role (account_id, role_id)
				VALUES (
					(SELECT id FROM user_account WHERE username = ?),
					(SELECT id FROM role WHERE name = ?)
				)
				""";
		String deleteQuery = """
				DELETE FROM user_role
				WHERE
					account_id = (SELECT id FROM user_account WHERE username = ?)
					AND role_id IN (SELECT id FROM role WHERE name LIKE ?)
				""";
		try (Connection conn = Database.connection()) {
			conn.setAutoCommit(false);
			try {
				PreparedStatement stmt1 = conn.prepareStatement(deleteQuery);
				stmt1.setString(1, username);
				stmt1.setString(2, prefix + "_%");
				int rowEffect1 = stmt1.executeUpdate();
				if (rowEffect1 <= 0) {
					throw new SQLException(
							"Delete old role of user before insert new one is failed!");
				}

				PreparedStatement stmt2 = conn.prepareStatement(insertQuery);
				stmt2.setString(1, username);
				stmt2.setString(2, prefix + "_" + roleName);
				int rowEffect2 = stmt2.executeUpdate();
				if (rowEffect2 <= 0) {
					throw new SQLException(
							"Insert new role for user is failed!");
				}

				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
		}
	}

	public static void addSpecifiedForUserWithPrefix(String prefix, String username, String roleName)
			throws SQLException {
		String insertQuery = """
				INSERT INTO user_role (account_id, role_id)
				VALUES (
					(SELECT id FROM user_account WHERE username = ?),
					(SELECT id FROM role WHERE name = ?)
				)
				""";
		try (Connection conn = Database.connection()) {
			try {
				PreparedStatement stmt = conn.prepareStatement(insertQuery);
				stmt.setString(1, username);
				stmt.setString(2, prefix + "_" + roleName);
				int rowEffect = stmt.executeUpdate();
				if (rowEffect <= 0) {
					throw new SQLException(
							"Insert new role for user is failed!");
				}
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
		}
	}

	/// NOTE:
	/// - Delete a user from guild, crew even application by prefix.
	/// - Using IN and LIKE keyword to ensure all prefix in guild, crew, app are
	/// removed.
	/// - Because we use the prefix in the role to determine whether the user can
	/// access the specified resource or not.
	public static void deleteUserFromPrefix(String username, String prefix) throws SQLException {
		String query = """
					DELETE FROM user_role
					WHERE
						account_id = (SELECT id FROM user_account WHERE username = ?)
						AND role_id IN (SELECT id FROM role WHERE name LIKE ?)
				""";

		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, prefix + "_%");

			int row = stmt.executeUpdate();
			if (row <= 0) {
				throw new SQLException("Delete role user is failed");
			}
		}
	}

	public static List<GetUserDTO> getUsersByPrefix(String prefix) throws SQLException {
		String query = """
					SELECT up.full_name AS full_name, ua.username as username, r.name AS role
					FROM user_profile up
						JOIN user_role ur ON ur.account_id = up.account_id
						JOIN user_account ua ON ua.id = ur.account_id
						JOIN role r ON r.id = ur.role_id
					WHERE r.name LIKE ?
				""";
		List<GetUserDTO> list = new ArrayList<>();
		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, prefix + "_%");

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				GetUserDTO user = new GetUserDTO();
				user.setFullName(rs.getString("full_name"));
				user.setUsername(rs.getString("username"));
				user.setRole(Pressessor.removePrefixFromRole(rs.getString("role")));
				list.add(user);
			}
		}
		return list;
	}

	public static List<GetUserDTO> getUsers() throws SQLException {
		String query = """
					SELECT up.full_name AS full_name, ua.username as username, r.name AS role
					FROM user_profile up
						JOIN user_account ua ON ua.id = up.account_id
						JOIN user_role ur ON ur.account_id = ua.id
						JOIN role r ON r.id = ur.role_id
				""";
		List<GetUserDTO> list = new ArrayList<>();
		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				GetUserDTO user = new GetUserDTO();
				user.setFullName(rs.getString("full_name"));
				user.setUsername(rs.getString("username"));
				user.setRole(Pressessor.removePrefixFromRole(rs.getString("role")));
				list.add(user);
			}
		}
		return list;
	}

	/// NOTE:
	/// @param permissions
	/// Using for multiple deleted permissions
	/// @return listError:
	/// Exception without preventing other permissions is deleted
	public static List<String> deletePermission(String roleName, List<String> permissions, RoleContext ctx)
			throws SQLException, IOException {
		List<String> listError = new ArrayList<>();
		if (permissions.isEmpty()) {
			return listError;
		}
		String query = """
				DELETE
				FROM role_permission rp
				WHERE
					role_id = (SELECT id FROM role WHERE name = ?)
					AND permission_id = (SELECT id FROM permission WHERE name = ? AND context = ?)
				""";

		try (Connection con = Database.connection()) {
			for (String permission : permissions) {
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, roleName);
				stmt.setString(2, permission);
				stmt.setString(3, ctx.name().toLowerCase());

				int row = stmt.executeUpdate();
				if (row == 0)
					listError.add("%s is failed to delete for %s".formatted(permission, roleName));
			}
		}
		return listError;
	}

	/// NOTE:
	/// @param permissions
	/// Using for multiple inserted permissions
	/// @return listError:
	/// Exception without preventing other permissions is inserted
	public static List<String> insertPermissionRole(String roleName, List<String> permissions, RoleContext ctx)
			throws SQLException, IOException {
		List<String> listError = new ArrayList<>();
		if (permissions.isEmpty()) {
			return listError;
		}
		String query = """
				INSERT INTO role_permission (role_id, permission_id)
				VALUES (
					(SELECT id FROM role WHERE name = ? LIMIT 1),
					(SELECT id FROM permission WHERE name = ? AND context = ? LIMIT 1)
				)
				""";

		try (Connection con = Database.connection()) {
			for (String permission : permissions) {
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, roleName);
				stmt.setString(2, permission);
				stmt.setString(3, ctx.name().toLowerCase());

				int row = stmt.executeUpdate();
				if (row == 0)
					listError.add("%s is failed to insert for %s".formatted(permission, roleName));
			}
		}
		return listError;
	}

	public static void update(String roleName, String newRoleName) throws SQLException {
		String query = """
					UPDATE role
					SET name = ?
					WHERE name = ?
				""";

		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, newRoleName);
			stmt.setString(2, roleName);

			ResultSet checked = stmt.executeQuery();
		}
	}

	/// NOTE:
	/// Multiple checking if user have specified permissions in list
	public static boolean existPermissionWithContext(RoleContext ctx, List<String> permissions, String accountId)
			throws SQLException {
		if (permissions == null || permissions.isEmpty()) {
			return false;
		}

		String placeholder = String.join(", ", Collections.nCopies(permissions.size(), "?"));
		String query = """
				SELECT 1
				FROM permission p
					JOIN role_permission rp ON rp.permission_id = p.id
					JOIN user_role ur ON ur.role_id = rp.role_id
				WHERE ur.account_id = ?
					AND p.context = ?
					AND p.name IN (%s)
				""".formatted(placeholder);

		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, accountId);
			stmt.setString(2, ctx.name().toLowerCase());
			int i = 3;
			for (String permission : permissions) {
				stmt.setString(i++, permission);
			}
			ResultSet checked = stmt.executeQuery();

			if (checked.next()) {
				return checked.getBoolean(1);
			} else {
				return false;
			}

		}
	}

	/// NOTE:
	/// Single checking if user have specified permission
	public static boolean existPermissionWithContext(RoleContext ctx, String permission, String accountId)
			throws SQLException {
		if (permission == null || permission.isEmpty()) {
			return false;
		}

		String query = """
				SELECT 1
				FROM permission p
					JOIN role_permission rp ON rp.permission_id = p.id
					JOIN user_role ur ON ur.role_id = rp.role_id
				WHERE ur.account_id = ?
					AND p.context = ?
					AND p.name = ?
				LIMIT 1
				""";

		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, accountId);
			stmt.setString(2, ctx.name().toLowerCase());
			stmt.setString(3, permission);

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
			stmt.setString(1, prefix + "\\_%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(Pressessor.removePrefixFromRole(rs.getString("name")));
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
				String permission = rs.getString("name");
				if (Pressessor.validPermission(permission)) {
					list.add(permission);
				}
			}
			return list;
		}
	}

	/// NOTE:
	/// @param ctx:
	/// - Prevent from crew but not from another guild
	public static List<String> getAllPermissionOfARole(String name)
			throws SQLException {
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

			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		}
	}
}
