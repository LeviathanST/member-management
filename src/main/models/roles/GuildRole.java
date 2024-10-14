package models.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.NotFoundException;

public class GuildRole {
	private String name;
	private Connection con;

	private GuildRole(String name, Connection con) {
		this.name = name;
		this.con = con;
	}

	public static List<GuildRole> getAllByAccountId(Connection con, String account_id)
			throws SQLException, NotFoundException {
		String query = "SELECT guild_role_name FROM user_guild_role WHERE account_id = ?";

		List<GuildRole> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, account_id);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("guild_role_name"));
			GuildRole gr = new GuildRole(rs.getString("guild_role_name"), con);

			list.add(gr);
		}

		return list;
	}

	public void loadToCache() throws SQLException {
		System.out.println("Starting to load...");
		String query = "SELECT gp.name as name FROM guild_role gr LEFT JOIN guild_permission gp ON gr.guild_permission_id = gp.id WHERE gr.name = ?";

		System.out.println("Load to cache");
		PreparedStatement stmt = this.con.prepareStatement(query);
		stmt.setString(1, this.name);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("name"));
		}
	}
}
