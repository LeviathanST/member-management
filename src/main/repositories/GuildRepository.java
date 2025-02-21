package repositories;

import java.io.IOException;
import java.sql.*;

import config.Database;
import exceptions.NotFoundException;
import repositories.users.UserAccountRepository;

import java.util.ArrayList;
import java.util.List;

public class GuildRepository {
	public static void insert(String name)
			throws SQLException, SQLIntegrityConstraintViolationException, IOException,
			ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "INSERT INTO guild (name) VALUES (?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Insert guild %s is failed", name));
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(e);
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	public static void update(int guildId, String newName)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "UPDATE guild SET name = ? WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, newName);
			stmt.setInt(2, guildId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Update name guild into %s is failed", newName));
			}
		}
	}

	public static void delete(int guildId) throws SQLException, IOException, ClassNotFoundException {

		try (Connection con = Database.connection()) {
			String query = "DELETE FROM guild WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, guildId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException("Delete guild is failed");
			}
		}
	}

	public static String getNameByID(int id)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT name FROM guild WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("name");
			}

			throw new NotFoundException("Guild is not existed!");
		}

	}

	public static void getAllName() throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT name FROM guild ";
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
			String query = "SELECT id FROM guild WHERE name = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("id");
			}

			throw new NotFoundException("Guild ID is not existed!");
		}
	}

	public static List<String> getAllGuildByAccountId(String accountId)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					SELECT g.name as name
					FROM guild g
					JOIN guild_role gr ON gr.guild_id = g.id
					JOIN user_guild_role ugr ON ugr.guild_role_id = gr.id
					WHERE ugr.account_id = ?
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

	public static List<String> getMemberInGuild(int guildId)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					SELECT ugr.account_id as account_id
					FROM guild g
					JOIN guild_role gr ON gr.guild_id = g.id
					JOIN user_guild_role ugr ON ugr.guild_role_id = gr.id
					WHERE g.id = ?
					""";

			List<String> list = new ArrayList<>();

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, guildId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(UserAccountRepository.getNameById(rs.getString("account_id")));
			}

			return list;
		}

	}

	public static List<String> getAllNameToList() throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			List<String> guildNames = new ArrayList<>();
			String query = "SELECT name FROM guild ";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				guildNames.add(rs.getString("name"));
			}
			return guildNames;
		}
	}

	public static String GetCodeByName(String name) throws SQLException, NotFoundException {
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
