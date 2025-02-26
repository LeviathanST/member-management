package repositories.events;

import config.Database;
import dto.app.GetEventDTO;
import exceptions.NotFoundException;
import models.Event;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventRepository {
	public static void insert(String title, String description, LocalDateTime startedAt,
			LocalDateTime endedAt)
			throws SQLException, IOException, ClassNotFoundException {
		String query = """
				    INSERT INTO event (title, description, started_at, ended_at)
				    VALUES (?, ?, ?, ?)
				""";
		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);

			stmt.setString(1, title);
			stmt.setString(2, description);
			stmt.setTimestamp(3, Timestamp.valueOf(startedAt));
			stmt.setTimestamp(4, Timestamp.valueOf(endedAt));

			int rowEffected = stmt.executeUpdate();
			if (rowEffected <= 0) {
				throw new SQLException("Event for guild is failed!");
			}
		}
	}

	// TODO:
	// Dynamic update for each field
	public static void update(int eventId, String title, String description,
			LocalDateTime startedAt,
			LocalDateTime endedAt)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					    UPDATE event
					    SET title = ?, description = ?, started_at = ?, ended_at = ?
					    WHERE id = ?
					""";
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setString(1, title);
				stmt.setString(2, description);
				stmt.setTimestamp(3, Timestamp.valueOf(startedAt));
				stmt.setTimestamp(4, Timestamp.valueOf(endedAt));
				stmt.setInt(5, eventId);

				int rowEffected = stmt.executeUpdate();
				if (rowEffected <= 0) {
					throw new SQLException("Update event failed!");
				}
			}
		}

	}

	public static void delete(int eventId) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "DELETE FROM event WHERE id = ?";

			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setInt(1, eventId);

				int rowEffected = stmt.executeUpdate();
				if (rowEffected == 0) {
					throw new SQLException("Delete event failed");
				}
			}
		}

	}

	public static List<GetEventDTO> getAllEvent()
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			List<GetEventDTO> list = new ArrayList<>();
			String query = """
						SELECT id, title, description, started_at, ended_at FROM event
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				GetEventDTO guildEvent = new GetEventDTO();
				guildEvent.setId(rs.getInt("id"));
				guildEvent.setTitle(rs.getString("title"));
				guildEvent.setDescription(rs.getString("description"));
				guildEvent.setStartedAt(rs.getTimestamp("started_at"));
				guildEvent.setEndedAt(rs.getTimestamp("ended_at"));

				list.add(guildEvent);
			}
			return list;
		}
	}
}
