package repositories;

import config.Database;
import exceptions.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenerationRepository {
	public static int getMax() throws SQLException, NotFoundException {
		String query = "SELECT MAX(id) as id FROM generation";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("id");
			}

			throw new NotFoundException("Not found any generation!");
		}
	}
}
