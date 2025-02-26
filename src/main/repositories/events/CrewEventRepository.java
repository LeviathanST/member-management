package repositories.events;

import config.Database;
import dto.crew.GetCrewEventDTO;
import exceptions.NotFoundException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrewEventRepository {
	public static void insert(String crewName, String title, String description, LocalDateTime startedAt,
			LocalDateTime endedAt)
			throws SQLException, IOException, ClassNotFoundException {
		String query = """
				    INSERT INTO crew_event (crew_id, title, description, started_at, ended_at)
				    VALUES (
					(SELECT id FROM crew WHERE name = ?),
					?, ?, ?, ?)
				""";
		Logger logger = LoggerFactory.getLogger(CrewEventRepository.class);
		logger.debug("Hi");
		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);

			stmt.setString(1, crewName);
			stmt.setString(2, title);
			stmt.setString(3, description);
			stmt.setTimestamp(4, Timestamp.valueOf(startedAt));
			stmt.setTimestamp(5, Timestamp.valueOf(endedAt));

			int rowEffected = stmt.executeUpdate();
			if (rowEffected <= 0) {
				throw new SQLException("Created crew event failed!");
			}
		}
	}

	// TODO:
	// Dynamic update for each field
	public static void update(String crewName, int eventId, String title, String description,
			LocalDateTime startedAt,
			LocalDateTime endedAt)
			throws SQLException, IOException, ClassNotFoundException {

		Logger logger = LoggerFactory.getLogger(CrewEventRepository.class);
		logger.debug("Hi");
		try (Connection con = Database.connection()) {
			String query = """
					    UPDATE crew_event
					    SET title = ?, description = ?, started_at = ?, ended_at = ?
					    WHERE
						id = ?
						AND crew_id = (SELECT id FROM crew WHERE name = ?)
					""";
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setString(1, title);
				stmt.setString(2, description);
				stmt.setTimestamp(3, Timestamp.valueOf(startedAt));
				stmt.setTimestamp(4, Timestamp.valueOf(endedAt));
				stmt.setInt(5, eventId);
				stmt.setString(6, crewName);

				int rowEffected = stmt.executeUpdate();
				if (rowEffected <= 0) {
					throw new SQLException("Update of event for crew is failed!");
				}
			}
		}

	}

	public static void delete(int eventId) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "DELETE FROM crew_event WHERE id = ?";

			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setInt(1, eventId);

				int rowEffected = stmt.executeUpdate();
				if (rowEffected == 0) {
					throw new SQLException("Delete event failed");
				}
			}
		}

	}

	public static List<GetCrewEventDTO> getAllCrewEvent(String crewName)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			List<GetCrewEventDTO> list = new ArrayList<>();
			String query = """
						SELECT id, title, description, started_at, ended_at FROM crew_event
						WHERE crew_id = (SELECT id FROM crew WHERE name = ?)
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, crewName);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				GetCrewEventDTO guildEvent = new GetCrewEventDTO();
				guildEvent.setCrewName(crewName);
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
