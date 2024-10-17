package models.permissions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.NotFoundException;

public class GuildPermission {
    private int id;
	private String account_id;
	private String name;
	private Connection con;

	public GuildPermission(int id, String account_id) {
		this.id = id;
		this.account_id = account_id;
	}

	public String getAccountId() {
		return this.account_id;
	}

	public String getName() {
		return this.name;
	}

	public static List<GuildPermission> getAllByGuildRoleId(Connection con, int guildRoleId)
			throws SQLException, NotFoundException {
		String query = "SELECT id, account_id FROM user_guild_role WHERE guild_role_id = ?";
		List<GuildPermission> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, guildRoleId);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			list.add(new GuildPermission(rs.getInt("id"), rs.getString("account_id")));
		}

		return list;
	}

	public void insert(String name) throws SQLException {
		String query = "INSERT INTO permission (name) VALUES (?)";

		PreparedStatement stmt = this.con.prepareStatement(query);
		stmt.setString(1, name);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when adding!");

		System.out.println("Add a permission successfully!");
	}

	public void update(String newName) throws SQLException {
		String query = "UPDATE permission SET name = ? where name = ?";

		PreparedStatement stmt = this.con.prepareStatement(query);
		stmt.setString(1, this.name);
		stmt.setString(2, newName);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when update!");

		System.out.println("Update a permission successfully!");
	}

	public void delete(String name) throws SQLException {
		String query = "DELETE FROM permission WHERE name = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);

		int row = stmt.executeUpdate();
		if (row == 0)
			throw new SQLException("A permission is failed when deleting!");

		System.out.println("Delete a permission successfully!");
	}
}
