package models.events;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import dto.GuildEventDto;
import java.sql.Connection;

public class GuildEvent {
	// Insert
	public void insert(Connection con, GuildEventDto data) throws SQLException {
		String query = """
				    INSERT INTO guild_event (guild_id, title, description, generation_id, start_at, end_at, type)
				    VALUES (?, ?, ?, ?, ?, ?, ?)
				""";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, data.getGuildId());
			stmt.setString(2, data.getTitle());
			stmt.setString(3, data.getDescription());
			stmt.setInt(4, data.getGenerationId());
			stmt.setTimestamp(5, data.getStartAt());
			stmt.setTimestamp(6, data.getEndAt());
			stmt.setString(7, data.getType());

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Insert %s to GuildEvent is failed", data.getTitle()));
			}
		}
	}

	// Update
	public void update(Connection con, GuildEventDto data) throws SQLException {
		String query = """
				    UPDATE guild_event
				    SET title = ?, description = ?, generation_id = ?, start_at = ?, end_at = ?, type = ?
				    WHERE guild_id = ?
				""";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, data.getTitle());
			stmt.setString(2, data.getDescription());
			stmt.setInt(3, data.getGenerationId());
			stmt.setTimestamp(4, data.getStartAt());
			stmt.setTimestamp(5, data.getEndAt());
			stmt.setString(6, data.getType());
			stmt.setInt(7, data.getGuildId());

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Update of GuildEvent with guildId %d failed",
								data.getGuildId()));
			}
		}
	}

	// Delete
	public void delete(Connection con, GuildEventDto data) throws SQLException {
		String query = "DELETE FROM guild_event WHERE guild_id = ?";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, data.getGuildId());

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Delete of GuildEvent with guildId %d failed",
								data.getGuildId()));
			}
		}
	}
}
