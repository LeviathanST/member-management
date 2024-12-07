package repositories.users;

import config.Database;
import exceptions.NotFoundException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRoleRepository {
	public static int getIdByAccountId(String account_id)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					SELECT role_id FROM user_role WHERE account_id = ?
					""";
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setString(1, account_id);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					return rs.getInt("role_id");
				}
				throw new NotFoundException("This account not have any user_role in application!");
			}
		}

	}

	public static void insert(String account_id, int role_id)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					INSERT INTO user_role (account_id, role_id)
					VALUES (?, ?)
					""";
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setString(1, account_id);
				stmt.setInt(2, role_id);

				int rowEffected = stmt.executeUpdate();
				if (rowEffected == 0)
					throw new SQLException("Add role failed!");
			}
		}

	}

	public static void update(String account_id, int new_role_id)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					UPDATE user_role
					SET role_id = ?
					WHERE account_id = ?
					""";
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setInt(1, new_role_id);
				stmt.setString(2, account_id);

				int rowEffected = stmt.executeUpdate();
				if (rowEffected == 0)
					throw new SQLException("Update role failed!");
			}
		}

	}

	public static void delete(String accountId) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					DELETE FROM user_role WHERE account_id = ?
					""";
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setString(1, accountId);
				int row = stmt.executeUpdate();
				if (row == 0)
					throw new SQLException("Delete user role failed.");
			}
		}
	}

	public static List<String> getRoleByAccountId(String accountId) throws SQLException, NotFoundException,
			NullPointerException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
						SELECT r.name as role
						FROM user_role ur
					 			JOIN role r ON r.id = ur.role_id
						WHERE ur.account_id = ?
					""";
			List<String> list = new ArrayList<>();
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setString(1, accountId);

				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					list.add(rs.getString("role"));
					while (rs.next()) {
						list.add(rs.getString("role"));
					}
				} else {
					throw new NullPointerException("Null Data");
				}
				return list;
			}
		}
	}
}
