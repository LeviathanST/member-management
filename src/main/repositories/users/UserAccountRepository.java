package repositories.users;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import java.util.ArrayList;

import config.Database;
import dto.LoginDTO;
import dto.SignUpDTO;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;

public class UserAccountRepository {
	public static List<String> getAllId() throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT id FROM user_account";
			List<String> list = new ArrayList<>();
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				list.add(rs.getString("id"));
			return list;
		}

	}

	public static String getHashPasswordByUsername(LoginDTO data)
			throws SQLException, IOException, NotFoundException {
		try (Connection con = Database.connection()) {
			String query = "SELECT hashed_password FROM user_account WHERE username = ? AND count_mistake <= 3";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, data.getUsername());
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				throw new NotFoundException(
						"Username is not found or banned, please contact with PRESIDENT for details!");
			}
			return rs.getString("hashed_password");
		}

	}

	public static String getIdByUsername(String username)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
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

	}

	public static String getNameById(String accountId)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
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

	}

	public static void insert(SignUpDTO data)
			throws DataEmptyException, SQLException, SQLIntegrityConstraintViolationException, IOException,
			ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "INSERT INTO user_account (username, hashed_password) VALUES (?, ?)";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setString(1, data.getUsername());
			stmt.setString(2, data.getPassword());

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0)
				throw new SQLException("Create new user is failed, no row is effected!");
		}

	}

	public static void update(String username, String password, String accountId)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					UPDATE user_account
					SET username = ?, hashed_password = ?
					WHERE id = ?
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, accountId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Update user account failed!");
		}

	}

	public static void delete(String username)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					DELETE FROM user_account WHERE username = ?
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, username);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Delete user account failed!");
		}

	}
}
