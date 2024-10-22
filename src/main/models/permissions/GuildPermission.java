package models.permissions;

import java.util.ArrayList;
import java.util.List;

import exceptions.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuildPermission {
	private int id;
	private String name;
	public GuildPermission(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return this.name;
	}

	public static List<GuildPermission> getAllByAccountIdAndGuildId(Connection con, String accountId,
			int guildId)
			throws SQLException {

		String query = """
				SELECT gp.name as name, gp.id as id
				FROM guild g
				JOIN guild_role gr ON gr.guild_id = g.id
				JOIN guild_role_permission grp ON grp.guild_role_id = gr.id
				JOIN guild_permission gp ON gp.id = grp.guild_permission_id
				JOIN user_guild_role ugr ON ugr.guild_role_id = gr.id
				WHERE g.id = ? AND ugr.account_id = ?
				""";

		List<GuildPermission> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, guildId);
		stmt.setString(2, accountId);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("name"));
			list.add(new GuildPermission(rs.getInt("id"), rs.getString("name")));
		}

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


	public void insert(String name, Connection con) throws SQLException {
		String query = "INSERT INTO guild_permission (name) VALUES (?)";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when adding!");

		System.out.println("Add a permission successfully!");
	}

	public void update(String newName, Connection con) throws SQLException {
		String query = "UPDATE guild_permission SET name = ? where name = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, this.name);
		stmt.setString(2, newName);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when update!");

		System.out.println("Update a permission successfully!");
	}

	public void delete(String name, Connection con) throws SQLException {
		String query = "DELETE FROM guild_permission WHERE name = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when deleting!");

		System.out.println("Delete a permission successfully!");
	}

}
