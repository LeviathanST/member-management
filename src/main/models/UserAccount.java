package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import exceptions.ExceptionDataEmpty;

public class UserAccount {
	// TODO: Hashing password
	public static void Insert(Connection con, SignUpData data)
			throws ExceptionDataEmpty, SQLException {

		String query = "INSERT INTO user_account (id, username, hashed_password) VALUES (UUID(), ?, ?)";

		if (data.getPassword() == null || data.getUsername() == null) {
			throw new ExceptionDataEmpty("Your username or password is empty");
		}

		try {
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setString(1, data.getUsername());
			stmt.setString(2, data.getPassword());

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0)
				throw new SQLException("Create new user is failed, no row is effected!");

			System.out.println("Create new user is successfully!");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
