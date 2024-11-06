package models.events;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.CrewEventDto;
import dto.GuildEventDto;
import exceptions.NotFoundException;
import models.Crew;
import models.Guild;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class GuildEvent {
	// Insert
	public static void insert(Connection con, GuildEventDto data) throws SQLException {
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
	public static void update(Connection con, GuildEventDto data, int guildEventID) throws SQLException {
		TextIO textIO = TextIoFactory.getTextIO();
		textIO.getTextTerminal().println(String.valueOf(guildEventID));
		String query = """
				    UPDATE guild_event
				    SET guild_id = ? , title = ?, description = ?, generation_id = ?, start_at = ?, end_at = ?, type = ?
				    WHERE id = ?
				""";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, data.getGuildId());
			stmt.setString(2, data.getTitle());
			stmt.setString(3, data.getDescription());
			stmt.setInt(4, data.getGenerationId());
			stmt.setTimestamp(5, data.getStartAt());
			stmt.setTimestamp(6, data.getEndAt());
			stmt.setString(7, data.getType());
			stmt.setInt(8, guildEventID);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Update of GuildEvent with guildId %d failed",
								data.getGuildId()));
			}
		}
	}

	// Delete
	public static void delete(Connection con, int guildEventId) throws SQLException {
		String query = "DELETE FROM guild_event WHERE id = ?";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, guildEventId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Delete of GuildEvent with Id %d failed",
								guildEventId));
			}
		}
	}
	public static List<GuildEventDto> getAllGuildEvent(Connection con) throws SQLException, NotFoundException {
		List<GuildEventDto> guildEvent = new ArrayList<>();
		String query = "SELECT * FROM guild_event";
		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			guildEvent.add(new
					GuildEventDto(rs.getInt("id"),
					Guild.getNameByID(con,rs.getInt("guild_id")),
					rs.getString("title"),
					rs.getString("description"),
					rs.getInt("generation_id"),
					rs.getTimestamp("start_at"),rs.getTimestamp("end_at"),rs.getString("type")));

		}
		return guildEvent;
	}
}
