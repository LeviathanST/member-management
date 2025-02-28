package repositories;

import config.Database;
import constants.RoleContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;

public class PermissionRepository {
	/// NOTE:
	/// Generate new bypass permission
	/// Format: prefix + .*
	public static void generateForNew(String prefix, RoleContext ctx) throws SQLException {
		String query = """
					INSERT INTO permission (context, name)
					VALUES (?, ?)
				""";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, ctx.name().toLowerCase());
			stmt.setString(2, prefix + ".*");

			int rows = stmt.executeUpdate();
			if (rows <= 0) {
				throw new SQLException("Generate permission pair for guild failed!");
			}
		}
	}
}
