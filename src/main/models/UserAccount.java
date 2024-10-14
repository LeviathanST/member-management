package models;

import java.lang.reflect.Field;
import java.sql.*;

import data.SignUpData;
import exceptions.ExceptionDataEmpty;

public class UserAccount {
	// TODO: Hashing password
	public static void insert(Connection con, SignUpData data)
			throws ExceptionDataEmpty, SQLException {

		String query = "INSERT INTO user_account (id, username, hashed_password,email,is_active,created_at,updated_at, count_mistake) VALUES (UUID(), ?, ?,?,?,?,?,?)";

		if (data.getPassword() == null || data.getUsername() == null || data.getEmail() == null) {
			throw new ExceptionDataEmpty("Your username or password is empty");
		}
		try {
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setString(1, data.getUsername());
			stmt.setString(2, data.getPassword());
			stmt.setString(3, data.getEmail());
			stmt.setBoolean(4,data.is_active());
			stmt.setDate(5,data.getCreated_at());
			stmt.setDate(6,data.getUpdated_at());
			stmt.setInt(7,data.getCount_mistake());
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
	public static void edit(Connection con, SignUpData data)
			throws ExceptionDataEmpty, SQLException {
		Field[] fields = data.getClass().getDeclaredFields();
		//Create query
		StringBuilder query = new StringBuilder("UPDATE user_account SET ");
		for (Field field : fields) {
			if (field.getName().equals("password")) {
				query.append("hashed_password=?, ");
			}
			else {
				query.append(field.getName()).append(" = ?, ");
			}

		}
		query.delete(query.length() - 2, query.length());
		query.append(" WHERE username = ?");
		if (data == null ) {
			throw new ExceptionDataEmpty("Your username is empty");
		}
		try {

			PreparedStatement stmt = con.prepareStatement(query.toString());
			int index = 1;
			for (Field field : fields) {
				field.setAccessible(true);
				stmt.setObject(index++, field.get(data));
			}
			stmt.setString(index++, data.getUsername());
			int rowUpdated = stmt.executeUpdate();
			if (rowUpdated == 0)
				throw new SQLException("Edit user is failed, no row is effected!");
			System.out.println("Edit user is successfully!");
		} catch (SQLIntegrityConstraintViolationException e) {
			System.err.println("Your username isn't existed!");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	public static void delete(Connection con, String data)
		throws ExceptionDataEmpty, SQLException {
		String query = "DELETE FROM user_account WHERE username = ?";
		if (data == null ) {
			throw new ExceptionDataEmpty("Your username is empty");
		}

		try {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, data);
			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0)
				throw new SQLException("Delete user is failed, no row is effected!");
			System.out.println("Delete is successfully!");
		} catch (SQLIntegrityConstraintViolationException e) {
			System.err.println("Your username is existed!");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
