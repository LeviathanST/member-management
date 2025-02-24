package repositories.events;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import config.Database;
import dto.guild.GetGuildEventDTO;
import exceptions.NotFoundException;
import models.GuildEvent;
import repositories.GuildRepository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class GuildEventRepository {
	public static void insert(String guildName, String title, String description, LocalDateTime startedAt,
			LocalDateTime endedAt)
			throws SQLException, IOException, ClassNotFoundException {
		String query = """
				    INSERT INTO guild_event (guild_id, title, description, started_at, ended_at)
				    VALUES (
					(SELECT id FROM guild WHERE name = ?),
					?, ?, ?, ?)
				""";
		try (Connection conn = Database.connection()) {
			PreparedStatement stmt = conn.prepareStatement(query);

			stmt.setString(1, guildName);
			stmt.setString(2, title);
			stmt.setString(3, description);
			stmt.setTimestamp(4, Timestamp.valueOf(startedAt));
			stmt.setTimestamp(5, Timestamp.valueOf(endedAt));

			int rowEffected = stmt.executeUpdate();
			if (rowEffected <= 0) {
				throw new SQLException("Event for guild is failed!");
			}
		}
	}

	// TODO:
	// Dynamic update for each field
	public static void update(String guildName, int eventId, String title, String description,
			LocalDateTime startedAt,
			LocalDateTime endedAt)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					    UPDATE guild_event
					    SET title = ?, description = ?, started_at = ?, ended_at = ?
					    WHERE
						id = ?
						AND guild_id = (SELECT id FROM guild WHERE name = ?)
					""";
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setString(1, title);
				stmt.setString(2, description);
				stmt.setTimestamp(3, Timestamp.valueOf(startedAt));
				stmt.setTimestamp(4, Timestamp.valueOf(endedAt));
				stmt.setInt(5, eventId);
				stmt.setString(6, guildName);

				int rowEffected = stmt.executeUpdate();
				if (rowEffected <= 0) {
					throw new SQLException("Update of event for guild is failed!");
				}
			}
		}

	}

	public static void delete(int eventId) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "DELETE FROM guild_event WHERE id = ?";

			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setInt(1, eventId);

				int rowEffected = stmt.executeUpdate();
				if (rowEffected == 0) {
					throw new SQLException(
							String.format("Delete of GuildEvent with Id %d failed",
									eventId));
				}
			}
		}

	}

	public static List<GetGuildEventDTO> getAllGuildEvent(String guildName)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			List<GetGuildEventDTO> list = new ArrayList<>();
			String query = """
						SELECT id, title, description, started_at, ended_at FROM guild_event
						WHERE guild_id = (SELECT id FROM guild WHERE name = ?)
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, guildName);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				GetGuildEventDTO guildEvent = new GetGuildEventDTO();
				guildEvent.setGuildName(guildName);
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
