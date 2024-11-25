package models.users;

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
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

public class UserAccount {
	private String username;
	
	public UserAccount(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public static List<String> getAllId() throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "SELECT id FROM user_account";
			List<String> list = new ArrayList<>();
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
				list.add(rs.getString("id"));
			return list;
		}

	}

	public static String getHashPasswordByUsername( LoginDTO data) throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "SELECT hashed_password FROM user_account WHERE username = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, data.getUsername());
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getString("hashed_password");
		}

	}

	public static List<UserAccount> getAllUserAccounts() throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
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

	}

	public static String getIdByUsername(String username) throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
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

	public static String getNameById(String accountId) throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
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
            throws DataEmptyException, SQLException, SQLIntegrityConstraintViolationException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
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

	public static void update( String username, String password, String email, String accountId) throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = """
				UPDATE user_account
				SET username = ?, hashed_password = ?, email = ?
				WHERE id = ?
				""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, email);
			stmt.setString(4, accountId);
			int row = stmt.executeUpdate();
			if(row == 0)
				throw new SQLException("Update user account failed!");
		}

	}

	public static void delete(String accountId) throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			UserProfile.delete( accountId);
			UserRole.delete( accountId);
			String query = """
				DELETE FROM user_account WHERE id = ?
				""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, accountId);
			int row = stmt.executeUpdate();
			if(row == 0)
				throw new SQLException("Delete user account failed!");
		}

	}
}
