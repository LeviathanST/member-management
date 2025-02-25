package repositories;

import java.sql.*;

import config.Database;
import exceptions.NotFoundException;

public class GuildRepository {
	public static String getCodeByName(String name) throws SQLException, NotFoundException {
		String query = "SELECT code FROM guild WHERE name = ?";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("code");
			}

			throw new NotFoundException("Not found guild code of: " + name);
		}
	}
}
