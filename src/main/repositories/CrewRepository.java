package repositories;

import config.Database;
import dto.crew.CrewInfoDTO;
import exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class CrewRepository {
	public static CrewInfoDTO getInfo(String crewName) throws SQLException {
		String query = """
				SELECT c.name, COUNT(ur.account_id) as totalMember FROM crew c
					JOIN role r ON r.name LIKE CONCAT(c.code, '\\_%')
					JOIN user_role ur ON ur.role_id = r.id
				WHERE c.name = ?
				GROUP BY c.name
				""";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, crewName);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				CrewInfoDTO dto = new CrewInfoDTO();
				dto.setCrewName(rs.getString("name"));
				dto.setTotalMember(rs.getString("totalMember"));
				return dto;
			}
			throw new SQLException("Get info of %s failed".formatted(crewName));
		}

	}

	public static List<String> getAllOfUser(String accountId) throws SQLException {
		String query = """
				SELECT c.name FROM crew c
					JOIN role r ON c.code = LEFT(r.name, LENGTH(c.code))
					JOIN user_role ur ON ur.role_id = r.id
					JOIN user_account ua ON ua.id = ur.account_id
				WHERE ua.id = ?
				""";
		List<String> list = new ArrayList<>();
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, accountId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("name"));
			}
			return list;
		}
	}

	public static List<String> getAll() throws SQLException {
		String query = """
				SELECT * FROM crew
				""";
		List<String> list = new ArrayList<>();
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("name"));
			}
			return list;
		}
	}

	/// NOTE:
	/// Create crew with at least a leader
	/// @param code:
	/// Guild abbreviation
	/// Sample: Technical -> T, Media -> M
	/// @param username
	/// Who is leader
	public static void create(String name, String code, String username) throws SQLException {
		String query = """
					CALL createParty(?, ?, ?, ?)
				""";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, "crew");
			stmt.setString(2, name);
			stmt.setString(3, code);
			stmt.setString(4, username);
			int rows = stmt.executeUpdate();

			if (rows <= 0) {
				throw new SQLException("Created crew failed!");
			}
		}
	}

	public static void update(String oldName, String newName) throws SQLException {
		String query = """
					UPDATE crew
					SET name = ?
					WHERE name = ?
				""";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			int rows = stmt.executeUpdate();

			if (rows <= 0) {
				throw new SQLException("Updated crew failed!");
			}
		}
	}

	public static void delete(String prefix) throws SQLException {
		String query = """
					CALL removeParty(?, ?)
				""";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, "crew");
			stmt.setString(2, prefix);
			int rows = stmt.executeUpdate();

			if (rows <= 0) {
				throw new SQLException("Updated guild failed!");
			}
		}
	}

	public static String getCodeByName(String name) throws SQLException, NotFoundException {
		String query = "SELECT code FROM crew WHERE name = ?";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("code");
			}

			throw new NotFoundException("Not found crew code of: " + name);
		}
	}
}
