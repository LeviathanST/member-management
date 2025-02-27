package repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import config.Database;
import dto.guild.GuildInfoDTO;
import exceptions.NotFoundException;

public class GuildRepository {
	public static GuildInfoDTO getInfo(String guildName) throws SQLException {
		String query = """
				SELECT g.name, COUNT(ur.account_id) as totalMember FROM guild g
					JOIN role r ON r.name LIKE CONCAT(g.code, '\\_%')
					JOIN user_role ur ON ur.role_id = r.id
				WHERE g.name = ?
				GROUP BY g.name
				""";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, guildName);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				GuildInfoDTO dto = new GuildInfoDTO();
				dto.setGuildName(rs.getString("name"));
				dto.setTotalMember(rs.getString("totalMember"));
				return dto;
			}
			throw new SQLException("Get info of %s failed".formatted(guildName));
		}

	}

	public static List<String> getAllOfUser(String accountId) throws SQLException {
		String query = """
				SELECT g.name FROM guild g
					JOIN role r ON g.code = LEFT(r.name, LENGTH(g.code))
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
				SELECT * FROM guild
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
	/// Create guild with at least a leader
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
			stmt.setString(1, "guild");
			stmt.setString(2, name);
			stmt.setString(3, code);
			stmt.setString(4, username);
			stmt.execute();
		}
	}

	public static void update(String oldName, String newName) throws SQLException {
		String query = """
					UPDATE guild
					SET name = ?
					WHERE name = ?
				""";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			int rows = stmt.executeUpdate();

			if (rows <= 0) {
				throw new SQLException("Updated guild failed!");
			}
		}
	}

	public static void delete(String prefix) throws SQLException {
		String query = """
					CALL removeParty(?, ?)
				""";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, "guild");
			stmt.setString(2, prefix);
			stmt.execute();
		}
	}

	public static String getCodeByName(String name) throws SQLException, NotFoundException {
		String query = "SELECT code FROM guild WHERE name = ?";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("code");
			}

			throw new NotFoundException("Not found guild code of: " + name);
		}
	}
}
