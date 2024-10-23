package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Generation {
	private String name;

	public Generation(String name) {
		this.name = name;
	}

	public static void insert(Connection con, String name) throws SQLException {
		String query = "INSERT INTO generation (name) VALUES (?)";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);

		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0) {
			throw new SQLException(String.format("Insert %s to Generation is failed!", name));
		}
	}

	public static void update(Connection con, String oldName, String newName) throws SQLException {
		String query = "UPDATE generation SET name = ? WHERE name = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, newName);
		stmt.setString(2, oldName);

		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0) {
			throw new SQLException(
					String.format("Update %s to %s in generation is failed!", oldName, newName));
		}
	}

	public static void delete(Connection con, String name) throws SQLException {
		String query = "DELETE FROM generation WHERE name = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);

		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0) {
			throw new SQLException(String.format("Delete %s from Generation is failed!", name));
		}
	}
}
