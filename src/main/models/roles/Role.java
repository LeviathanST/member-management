package models.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.NotFoundException;

public class Role {
	private int id;
	private String name;

	public Role(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public static List<Role> getAll(Connection con) throws SQLException {
		String query = "SELECT * FROM role";
		List<Role> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("name"));
			list.add(new Role(rs.getInt("id"), rs.getString("name")));
		}

		return list;
	}

	public static Role getByName(Connection con, String name) throws SQLException, NotFoundException {
		String query = "SELECT id FROM role WHERE name = ?";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			return new Role(rs.getInt("id"), name);
		}

		throw new NotFoundException("Your specified role is not found!");
	}

	public static Role getByAccountId(Connection con, String account_id) throws SQLException, NotFoundException {
		String query = """
				SELECT r.id as id, r.name as name
				FROM user_role ur
				JOIN role r ON r.id = ur.role_id
				WHERE ur.account_id = ?
				""";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, account_id);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println("take it!");
			return new Role(rs.getInt("id"), rs.getString("name"));
		}

		throw new NotFoundException("This account is not have any role in application!");
	}
}
