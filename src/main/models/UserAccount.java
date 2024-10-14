package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import data.SignUpData;
import exceptions.ExceptionDataEmpty;

public class UserAccount {

	public static void insert(Connection con, SignUpData data)
			throws ExceptionDataEmpty, SQLException {

		String query = "INSERT INTO user_account (username, hashed_password) VALUES (?, ?)";

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
		} catch (SQLIntegrityConstraintViolationException e) {
			System.err.println("Your username is existed!");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
