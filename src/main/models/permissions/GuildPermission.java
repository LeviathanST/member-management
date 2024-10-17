package models.permissions;

import java.util.ArrayList;
import java.util.List;

import exceptions.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuildPermission {
	private int guildId;
	private String name;

	public GuildPermission(int guildId, String name) {
		this.guildId = guildId;
		this.name = name;
	}

	public static List<GuildPermission> getAllByAccountIdAndRoleId(Connection con, String accountId,
			int guildRoleId)
			throws SQLException {

		String query = """
				SELECT gp.id as id, gp.name as name
				FROM user_guild_role ugr
				JOIN guild_role_permission grp ON grp.guild_role_id = ugr.guild_role_id
				JOIN guild_permission gp ON gp.id = grp.guild_permission_id
				WHERE ugr.account_id = ? AND ugr.guild_role_id = ?
				""";

		List<GuildPermission> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, accountId);
		stmt.setInt(2, guildRoleId);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("name"));
			list.add(new GuildPermission(rs.getInt("id"), rs.getString("name")));
		}

		System.out.println(list);

		return list;
	}

	public static List<GuildPermission> getAllByGuildRoleId(Connection con, int guildRoleId)
			throws SQLException, NotFoundException {
		String query = "SELECT id, name FROM guild_role_permission WHERE guild_role_id = ?";
		List<GuildPermission> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, guildRoleId);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			list.add(new GuildPermission(rs.getInt("id"), rs.getString("name")));
		}

		return list;
	}

	public String getName() {
		return this.name;
	}
}
