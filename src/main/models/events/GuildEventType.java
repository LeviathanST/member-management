package models.events;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GuildEventType {
	private int id;
	private String name;

	public GuildEventType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static void insert(Connection con, String name) throws SQLException {
		String query = "INSERT INTO guild_event_type (name) VALUES (?)";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);

		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0) {
			throw new SQLException(String.format("Insert %s to GuildEventType is failed!", name));
		}
	}

	public static void update(Connection con, String oldName, String newName) throws SQLException {
		String query = "UPDATE guild_event_type SET name = ? WHERE name = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, newName);
		stmt.setString(2, oldName);

		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0) {
			throw new SQLException(
					String.format("Update %s to %s in GuildEventType is failed!", oldName,
							newName));
		}
	}

	public static void delete(Connection con, String name) throws SQLException {
		String query = "DELETE FROM guild_event_type WHERE name = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);

		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0) {
			throw new SQLException(String.format("Delete %s from GuildEventType is failed!", name));
		}
	}
}
