package models.events;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import dto.CrewEventDto;
import java.sql.Connection;

public class CrewEvent {
	public void insert(Connection con, CrewEventDto data) throws SQLException {
		String query = """
				    INSERT INTO crew_event (crew_id, title, description, generation_id, start_at, end_at, type)
				    VALUES (?, ?, ?, ?, ?, ?, ?)
				""";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, data.getCrewId());
			stmt.setString(2, data.getTitle());
			stmt.setString(3, data.getDescription());
			stmt.setInt(4, data.getGenerationId());
			stmt.setTimestamp(5, data.getStartAt());
			stmt.setTimestamp(6, data.getEndAt());
			stmt.setString(7, data.getType());

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Insert %s to CrewEvent is failed", data.getTitle()));
			}
		}
	}

	public void update(Connection con, CrewEventDto data) throws SQLException {
		String query = """
				    UPDATE crew_event
				    SET title = ?, description = ?, generation_id = ?, start_at = ?, end_at = ?, type = ?
				    WHERE crew_id = ?
				""";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, data.getTitle());
			stmt.setString(2, data.getDescription());
			stmt.setInt(3, data.getGenerationId());
			stmt.setTimestamp(4, data.getStartAt());
			stmt.setTimestamp(5, data.getEndAt());
			stmt.setString(6, data.getType());
			stmt.setInt(7, data.getCrewId());

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Update of CrewEvent with crewId %d failed",
								data.getCrewId()));
			}
		}
	}

	// Delete
	public void delete(Connection con, CrewEventDto data) throws SQLException {
		String query = "DELETE FROM crew_event WHERE crew_id = ?";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, data.getCrewId());

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Delete of CrewEvent with crewId %d failed",
								data.getCrewId()));
			}
		}
	}
}
