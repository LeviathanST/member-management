package models.users;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

import config.Database;
import dto.UserGuildRoleDTO;
import exceptions.NotFoundException;

public class UserGuildRole {
	public static List<Integer> getIdByAccountId( String account_id)
            throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = """
				SELECT id FROM user_guild_role WHERE account_id = ?
				""";
			List<Integer> list = new ArrayList<>();

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, account_id);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(rs.getInt("id"));
			}
			return list;
		}

	}
	public static List<UserGuildRoleDTO> getAllByGuildId(String guild, int guild_id) throws SQLException, NotFoundException, NullPointerException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = """
					SELECT ua.username as name, gr.name as role
					FROM user_guild_role ugr
	    			JOIN user_account ua ON ugr.account_id = ua.id
					JOIN guild_role gr ON gr.id = ugr.guild_role_id
					WHERE gr.guild_id = ?
				""";
			List<UserGuildRoleDTO> list = new ArrayList<>();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, guild_id);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				list.add(new UserGuildRoleDTO(guild, rs.getString("name"), rs.getString("role")));
				while (rs.next()) {
					list.add(new UserGuildRoleDTO(guild, rs.getString("name"), rs.getString("role")));
				}
			}
			else {
				throw new NullPointerException("Null Data");
			}

			return list;
		}

	}
	public static List<String> getRoleByAccountId(String accountId) throws SQLException, NotFoundException, NullPointerException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = """
					SELECT gr.name as role
					FROM user_guild_role ugr
	    			JOIN guild_role gr ON gr.id = ugr.guild_role_id
					WHERE ugr.account_id = ?
				""";
			List<String> list = new ArrayList<>();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, accountId);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				list.add(rs.getString("role"));
				while (rs.next()) {
					list.add(rs.getString("role"));
				}
			}
			else {
				throw new NullPointerException("Null Data");
			}
			return list;
		}
	}
	public static void insertGuildMember( String accountId, int guildRoleId)
            throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "INSERT INTO user_guild_role(account_id, guild_role_id) VALUES (?, ?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, accountId);
			stmt.setInt(2, guildRoleId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Insert member to guild is failed!");
		}

	}
	public static void updateGuildMember( String accountId, int guildId, int newGuildRoleId)
            throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = """
				UPDATE user_guild_role as ugr
				JOIN guild_role gr ON gr.id = ugr.guild_role_id
				SET ugr.guild_role_id = ?
				WHERE ugr.account_id = ? AND gr.guild_id = ?
				""";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, newGuildRoleId);
			stmt.setString(2, accountId);
			stmt.setInt(3, guildId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Update member from guild is failed!");
		}

	}
	public static void deleteGuildMember( String accountId, int guildId)
            throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = """
					DELETE ugr
					FROM user_guild_role ugr
					JOIN guild_role gr ON gr.id = ugr.guild_role_id
					WHERE ugr.account_id = ? AND gr.guild_id = ?;
				""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, accountId);
			stmt.setInt(2, guildId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException(String.format("Delete member from guild is failed!"));
		}

	}
}
