package repositories;

import config.Database;
import exceptions.NotFoundException;

import java.sql.*;

public class CrewRepository {
	public static String getCodeByName(String name) throws SQLException, NotFoundException {
		String query = "SELECT code FROM crew WHERE name = ?";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("code");
			}

			throw new NotFoundException("Not found crew code of: " + name);
		}
	}
}
