package models.events;

import config.Database;
import dto.CrewEventDto;
import exceptions.NotFoundException;
import models.Crew;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrewEvent {
	public static void insert( CrewEventDto data) throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
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

	}

	public static void update( CrewEventDto data, int crewEventID) throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = """
				    UPDATE crew_event
				    SET crew_id = ? ,title = ?, description = ?, generation_id = ?, start_at = ?, end_at = ?, type = ?
				    WHERE id = ?
				""";
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setInt(1, data.getCrewId());
				stmt.setString(2, data.getTitle());
				stmt.setString(3, data.getDescription());
				stmt.setInt(4, data.getGenerationId());
				stmt.setTimestamp(5, data.getStartAt());
				stmt.setTimestamp(6, data.getEndAt());
				stmt.setString(7, data.getType());
				stmt.setInt(8, crewEventID);

				int rowEffected = stmt.executeUpdate();
				if (rowEffected == 0) {
					throw new SQLException(
							String.format("Update of CrewEvent with crewId %d failed",
									data.getCrewId()));
				}
			}
		}

	}

	// Delete
	public static void delete(int crewEventId) throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "DELETE FROM crew_event WHERE id = ?";

			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setInt(1,crewEventId);

				int rowEffected = stmt.executeUpdate();
				if (rowEffected == 0) {
					throw new SQLException(
							String.format("Delete of CrewEvent with crewId %d failed",
									crewEventId));
				}
			}
		}

	}
	public static List<CrewEventDto> getAllCrewEvent() throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			List<CrewEventDto> crewEvents = new ArrayList<>();
			String query = "SELECT * FROM crew_event";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				crewEvents.add(new
						CrewEventDto(rs.getInt("id"),
						Crew.getNameByID(rs.getInt("crew_id")),
						rs.getString("title"),
						rs.getString("description"),
						rs.getInt("generation_id"),
						rs.getTimestamp("start_at"),rs.getTimestamp("end_at"),rs.getString("type")));
			}
			return crewEvents;
		}

	}
}
