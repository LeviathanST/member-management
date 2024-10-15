package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRole {

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
