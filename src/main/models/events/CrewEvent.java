package models.events;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import dto.CrewEventDTO;

import java.sql.Connection;

public class CrewEvent {
	private int id;
	private int crewId;
	private String title;
	private String description;
	private int generationId;
	private Timestamp startAt;
	private Timestamp endAt;
	private int typeId;

	// Constructor
	public CrewEvent(int id,
			int crewId,
			String title,
			String description,
			int generationId,
			Timestamp startAt,
			Timestamp endAt,
			int typeId) {
		this.id = id;
		this.crewId = crewId;
		this.title = title;
		this.description = description;
		this.generationId = generationId;
		this.startAt = startAt;
		this.endAt = endAt;
		this.typeId = typeId;
	}

	// CRUD Operations
	public void insert(Connection con, CrewEventDTO data) throws SQLException {
		String query = """
				    INSERT INTO crew_event (crew_id, title, description, generation_id, start_at, end_at, type_id)
				    VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, data.getCrewId());
			stmt.setString(2, data.getTitle());
			stmt.setString(3, this.description);
			stmt.setInt(4, this.generationId);
			stmt.setTimestamp(5, this.startAt); // Corrected index for startAt
			stmt.setTimestamp(6, this.endAt); // Corrected index for endAt
			stmt.setInt(7, this.typeId); // Corrected index for typeId

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Insert %s to CrewEvent is failed", this.title));
			}
		}
	}

	public void updateEvent(Connection con) throws SQLException {
		String query = """
				    UPDATE crew_event
				    SET title = ?, description = ?, generation_id = ?, start_at = ?, end_at = ?, type_id = ?
				    WHERE crew_id = ?
				""";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, this.title);
			stmt.setString(2, this.description);
			stmt.setInt(3, this.generationId);
			stmt.setTimestamp(4, this.startAt);
			stmt.setTimestamp(5, this.endAt);
			stmt.setInt(6, this.typeId);
			stmt.setInt(7, this.crewId); // Using crewId to locate the event to update

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Update of CrewEvent with crewId %d failed",
								this.crewId));
			}
		}
	}

	public void deleteEvent(Connection con) throws SQLException {
		String query = "DELETE FROM crew_event WHERE crew_id = ?";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, this.crewId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Delete of CrewEvent with crewId %d failed",
								this.crewId));
			}
		}
	}
}
