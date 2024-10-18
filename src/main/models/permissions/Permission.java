package models.permissions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class Permission {
	private int id;
	private String name;

	private Connection con;

	public Permission(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public static List<Permission> getByAccountId(Connection con, String accountId) throws SQLException {
		String query = """
				SELECT p.id as id, p.name as name
				FROM user_role ur
				JOIN role_permission rp ON rp.role_id = ur.role_id
				JOIN permission p ON p.id = rp.permission_id
				WHERE ur.account_id = ?
				""";

		List<Permission> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, accountId);

		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getString("name"));
			list.add(new Permission(rs.getInt("id"), rs.getString("name")));
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
