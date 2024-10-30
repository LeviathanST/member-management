package models.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.ArrayList;
import dto.SignUpDTO;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;

public class UserAccount {
	private String username;
	
	public UserAccount(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public static List<UserAccount> getAllUserAccounts(Connection con) {
		List<UserAccount> list = new ArrayList<>();
		String query = """
				SELECT username FROM user_account
				""";
		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			list.add(new UserAccount(rs.getString("username")));
		}
		return list;
	}

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

	public static void insert(Connection con, SignUpDTO data)
			throws DataEmptyException, SQLException, SQLIntegrityConstraintViolationException {

		String query = "INSERT INTO user_account (username, hashed_password, email) VALUES (?, ?, ?)";

		if (data.getPassword() == null || data.getUsername() == null) {
			throw new DataEmptyException("Your username or password is empty");
		}

		PreparedStatement stmt = con.prepareStatement(query);

		stmt.setString(1, data.getUsername());
		stmt.setString(2, data.getPassword());
		stmt.setString(3, data.getEmail());

		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0)
			throw new SQLException("Create new user is failed, no row is effected!");

	}
}
