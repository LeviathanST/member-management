package models.permissions;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;

public class Permission {
	private String id;
	private String name;

	private Connection con;

	public Permission(String id, String name) {
		this.id = id;
		this.name = name;
	}

	// TODO:
	// public static Permission getByPermissionName(String name) {
	// // TODO: GET ID FROM SELECT
	// //
	// return new Permission(id, name);
	// }

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
