package models.events;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Connection;

public class GuildEvent {
	private int id;
	private int guildId; // Changed from crewId to guildId
	private String title;
	private String description;
	private int generationId;
	private Timestamp startAt;
	private Timestamp endAt;
	private int typeId;

	// Constructor
	public GuildEvent(int id, int guildId,
			String title,
			String description,
			int generationId,
			Timestamp startAt,
			Timestamp endAt,
			int typeId) {
		this.id = id;
		this.guildId = guildId; // Updated variable name
		this.title = title;
		this.description = description;
		this.generationId = generationId;
		this.startAt = startAt;
		this.endAt = endAt;
		this.typeId = typeId;
	}

	// Getters and Setters
	// (omitting for brevity, keep as previously defined)

	// CRUD Operations
	public void insert(Connection con) throws SQLException {
		String query = """
				    INSERT INTO guild_event (guild_id, title, description, generation_id, start_at, end_at, type_id)
				    VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, this.guildId); // Updated variable name
			stmt.setString(2, this.title);
			stmt.setString(3, this.description);
			stmt.setInt(4, this.generationId);
			stmt.setTimestamp(5, this.startAt); // Corrected index for startAt
			stmt.setTimestamp(6, this.endAt); // Corrected index for endAt
			stmt.setInt(7, this.typeId); // Corrected index for typeId

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Insert %s to GuildEvent is failed", this.title)); // Updated
															// error
															// message
			}
		}
	}

	public void updateEvent(Connection con) throws SQLException {
		String query = """
				    UPDATE guild_event
				    SET title = ?, description = ?, generation_id = ?, start_at = ?, end_at = ?, type_id = ?
				    WHERE guild_id = ?  -- Updated to use guild_id
				""";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, this.title);
			stmt.setString(2, this.description);
			stmt.setInt(3, this.generationId);
			stmt.setTimestamp(4, this.startAt);
			stmt.setTimestamp(5, this.endAt);
			stmt.setInt(6, this.typeId);
			stmt.setInt(7, this.guildId); // Using guildId to locate the event to update

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Update of GuildEvent with guildId %d failed",
								this.guildId)); // Updated error message
			}
		}
	}

	public void deleteEvent(Connection con) throws SQLException {
		String query = "DELETE FROM guild_event WHERE guild_id = ?"; // Updated to use guild_id

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, this.guildId); // Updated variable name

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Delete of GuildEvent with guildId %d failed",
								this.guildId)); // Updated error message
			}
		}
	}
}
