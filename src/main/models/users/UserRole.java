package models.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exceptions.NotFoundException;

public class UserRole {

	public static int getIdByAccountId(Connection con, String account_id) throws SQLException, NotFoundException {
		String query = """
				SELECT role_id FROM user_role WHERE account_id = ?
				""";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, account_id);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			return rs.getInt("role_id");
		}

		throw new NotFoundException("This account not have any user_role in application!");
	}

	public static void insert(Connection con, String account_id, int role_id) throws SQLException {
		String query = """
				INSERT INTO user_role (account_id, role_id)
				VALUES (?, ?)
				""";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, account_id);
		stmt.setInt(2, role_id);

		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0)
			throw new SQLException("Add role failed!");
	}

	public static void update(Connection con, String account_id, int new_role_id) throws SQLException {
		String query = """
				UPDATE user_role
				SET role_id = ?
				WHERE account_id = ? 
				""";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, new_role_id);
		stmt.setString(2, account_id);

		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0)
			throw new SQLException("Update role failed!");
	}
}
