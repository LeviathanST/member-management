package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import data.SignUpData;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;

public class UserAccount {

	public static String getIdByUsername(Connection con, String username) throws SQLException, NotFoundException {
		String query = """
				SELECT id FROM user_account WHERE username = ?
				""";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, username);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			return rs.getString("id");
		}

		throw new NotFoundException("This username is not existed!");
	}

	public static void insert(Connection con, SignUpData data)
			throws DataEmptyException, SQLException, SQLIntegrityConstraintViolationException {

		String query = "INSERT INTO user_account (username, hashed_password) VALUES (?, ?)";

		if (data.getPassword() == null || data.getUsername() == null) {
			throw new DataEmptyException("Your username or password is empty");
		}

		PreparedStatement stmt = con.prepareStatement(query);

		stmt.setString(1, data.getUsername());
		stmt.setString(2, data.getPassword());

		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0)
			throw new SQLException("Create new user is failed, no row is effected!");

		System.out.println("Create new user is successfully!");
	}
}
