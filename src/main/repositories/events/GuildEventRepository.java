package repositories.events;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.Database;
import exceptions.NotFoundException;
import models.GuildEvent;
import repositories.GuildRepository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class GuildEventRepository {
	// Insert
	public static void insert(GuildEvent data) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
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
							String.format("Insert %s to GuildEvent is failed",
									data.getTitle()));
				}
			}
		}

	}

	// Update
	public static void update(GuildEvent data, int guildEventID)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
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

	}

	// Delete
	public static void delete(int guildEventId) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
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

	}

	public static List<GuildEvent> getAllGuildEvent()
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			List<GuildEvent> guildEvent = new ArrayList<>();
			String query = "SELECT * FROM guild_event";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				guildEvent.add(new GuildEvent(rs.getInt("id"),
						GuildRepository.getNameByID(rs.getInt("guild_id")),
						rs.getString("title"),
						rs.getString("description"),
						rs.getInt("generation_id"),
						rs.getTimestamp("start_at"), rs.getTimestamp("end_at"),
						rs.getString("type")));

			}
			return guildEvent;
		}
	}
}
