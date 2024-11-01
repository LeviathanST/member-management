package models.events;

import dto.EventDto;
import dto.GuildEventDto;
import exceptions.NotFoundException;
import models.Guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Event {
	// Insert
	public static void insert(Connection con, EventDto data) throws SQLException {
		String query = """
				    INSERT INTO event (title, description, generation_id, start_at, end_at, type)
				    VALUES (?, ?, ?, ?, ?, ?)
				""";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, data.getTitle());
			stmt.setString(2, data.getDescription());
			stmt.setInt(3, data.getGenerationId());
			stmt.setTimestamp(4, data.getStartAt());
			stmt.setTimestamp(5, data.getEndAt());
			stmt.setString(6, data.getType());

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Insert %s to Event is failed", data.getTitle()));
			}
		}
	}

	// Update
	public static void update(Connection con, EventDto data, int eventId) throws SQLException {
		String query = """
				    UPDATE event
				    SET  title = ?, description = ?, generation_id = ?, start_at = ?, end_at = ?, type = ?
				    WHERE id = ?
				""";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, data.getTitle());
			stmt.setString(2, data.getDescription());
			stmt.setInt(3, data.getGenerationId());
			stmt.setTimestamp(4, data.getStartAt());
			stmt.setTimestamp(5, data.getEndAt());
			stmt.setString(6, data.getType());
			stmt.setInt(7, eventId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Update of Event with Id %d failed",
								data.getTitle()));
			}
		}
	}

	// Delete
	public static void delete(Connection con, int eventId) throws SQLException {
		String query = "DELETE FROM event WHERE id = ?";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, eventId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Delete of Event with Id %d failed",
								eventId));
			}
		}
	}
	public static List<EventDto> getAllEvent(Connection con) throws SQLException, NotFoundException {
		List<EventDto> event = new ArrayList<>();
		String query = "SELECT * FROM event";
		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			event.add(new
					EventDto(rs.getInt("id"),
					rs.getString("title"),
					rs.getString("description"),
					rs.getInt("generation_id"),
					rs.getTimestamp("start_at"),rs.getTimestamp("end_at"),rs.getString("type")));
		}
		return event;
	}
}
