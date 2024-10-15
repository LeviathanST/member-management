package models.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import constants.GuildType;
import exceptions.NotFoundException;

public class GuildRole {
	private String name;
	private int guild_id;
	private Connection con;

	private GuildRole(int guild_id, String name, Connection con) {
		this.guild_id = guild_id;
		this.name = name;
		this.con = con;
	}

	public static List<GuildRole> getAllByGuildId(Connection con, int guild_id) throws SQLException {
		String query = """
				SELECT name
				FROM guild_role
				WHERE guild_id = ?
				""";
		List<GuildRole> list = new ArrayList<GuildRole>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, guild_id);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			list.add(new GuildRole(guild_id, rs.getString("name"), con));
		}

		return list;
	}

	public static List<GuildRole> getAllByAccountId(Connection con, String account_id)
			throws SQLException, NotFoundException {
		String query = """
					SELECT gr.guild_id as id, gr.name as name
					FROM user_account ua
					JOIN user_guild_role ugr ON ugr.account_id = ua.id
					JOIN guild_role gr ON gr.id = ugr.guild_role_id
					WHERE ua.id = ?
				""";

		List<GuildRole> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, account_id);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			GuildRole gr = new GuildRole(rs.getInt("id"), rs.getString("name"), con);
			list.add(gr);
		}

		return list;
	}
}
