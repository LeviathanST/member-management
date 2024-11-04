package models.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import dto.SignUpDTO;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

public class UserAccount {

	public static String getIdByUsername(Connection con, String username) throws SQLException, NotFoundException {
		String query = """
				SELECT id FROM user_account WHERE username = ?
				""";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, username);

		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			return rs.getString("id");
		}
		throw new NotFoundException("User not found");
	}

	public static String getNameById(Connection con, String accountId) throws SQLException, NotFoundException {
		String query = """
				SELECT username FROM user_account WHERE id = ?
				""";

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, accountId);

		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			return rs.getString("username");
		}
		throw new NotFoundException("User not found");
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
