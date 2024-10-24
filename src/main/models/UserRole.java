package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exceptions.NotFoundException;

public class UserRole {

	public static int getIdByAccountId(Connection con, String account_id) throws SQLException, NotFoundException {
		String query = """
				SELECT id FROM user_role WHERE account_id = ?
				""";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, account_id);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			return rs.getInt("id");
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
}
