package models.users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

import dto.UserGuildRoleDTO;
import exceptions.NotFoundException;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

public class UserGuildRole {
	public static List<Integer> getIdByAccountId(Connection con, String account_id)
			throws SQLException, NotFoundException {
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
	public static List<UserGuildRoleDTO> getAllByGuildId(Connection con, String guild, int guild_id) throws SQLException, NotFoundException,NullPointerException {
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
	public static void insertGuildMember(Connection con, String accountId, int guildRoleId)
			throws SQLException, NotFoundException {
		String query = "INSERT INTO user_guild_role(account_id, guild_role_id) VALUES (?, ?)";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, accountId);
		stmt.setInt(2, guildRoleId);
		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("Insert member to guild is failed!");
	}
	public static void updateGuildMember(Connection con, String accountId, int guildId, int newGuildRoleId)
			throws SQLException, NotFoundException {
		TextIO textIO = TextIoFactory.getTextIO();
		textIO.getTextTerminal().println(accountId);
		textIO.getTextTerminal().println(String.valueOf(guildId));
		textIO.getTextTerminal().println(String.valueOf(newGuildRoleId));
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
	public static void deleteGuildMember(Connection con, String accountId, int guildId)
			throws SQLException, NotFoundException {
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
