package repositories;

import config.Database;
import exceptions.NotFoundException;
import repositories.users.UserAccountRepository;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrewRepository {
	public static void insert(String name)
			throws SQLException, SQLIntegrityConstraintViolationException, IOException,
			ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "INSERT INTO crew (name) VALUES (?)";
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setString(1, name);

				int rowEffected = stmt.executeUpdate();
				if (rowEffected == 0) {
					throw new SQLException(String.format("Insert crew %s is failed", name));
				}
			}
		}

	}

	public static List<String> getMemberInCrew(int crewId)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					SELECT ucr.account_id as account_id
					FROM crew c
					JOIN crew_role cr ON cr.crew_id = c.id
					JOIN user_crew_role ucr ON ucr.crew_role_id = cr.id
					WHERE c.id = ?
					""";

			List<String> list = new ArrayList<>();

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, crewId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(UserAccountRepository.getNameById(rs.getString("account_id")));
			}

			return list;
		}

	}

	public static void update(int crewId, String newName) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "UPDATE crew SET name = ? WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, newName);
			stmt.setInt(2, crewId);
			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Update name crew into %s is failed", newName));
			}
		}
	}

	public static void delete(int crewId) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "DELETE FROM crew WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, crewId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException("Delete crew is failed");
			}
		}

	}

	public static void getAllName() throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT name FROM crew ";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getString("name"));
			}
		}

	}

	public static int getIdByName(String name)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT id FROM crew WHERE name = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("id");
			}

			throw new NotFoundException("Crew ID is not existed!");
		}

	}

	public static String getNameByID(int id)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT name FROM crew WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("name");
			}

			throw new NotFoundException("Crew is not existed!");
		}
	}

	public static List<String> getAllNameToList() throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			List<String> crewNames = new ArrayList<>();
			String query = "SELECT name FROM crew ";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				crewNames.add(rs.getString("name"));
			}
			return crewNames;
		}
	}

	public static List<String> getAllCrewByAccountId(String accountId)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					SELECT c.name as name
					FROM crew g
					JOIN crew_role cr ON cr.crew_id = c.id
					JOIN user_crew_role ucr ON ucr.crew_role_id = cr.id
					WHERE ucr.account_id = ?
					""";

			List<String> list = new ArrayList<>();

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, accountId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("name"));
			}

			return list;
		}
	}
}
