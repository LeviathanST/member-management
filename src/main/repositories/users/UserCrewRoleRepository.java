package repositories.users;

import config.Database;
import exceptions.NotFoundException;
import models.UserCrewRole;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserCrewRoleRepository {
	public static List<Integer> getIdByAccountId(String account_id)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					SELECT id FROM user_crew_role WHERE account_id = ?
					""";
			List<Integer> list = new ArrayList<>();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, account_id);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(rs.getInt("id"));
			}

			return list;
		}

	}

	public static List<Integer> getCrewRoleIdByAccountId(String account_id)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					SELECT crew_role_id FROM user_crew_role WHERE account_id = ?
					""";
			List<Integer> list = new ArrayList<>();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, account_id);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(rs.getInt("crew_role_id"));
			}

			return list;
		}

	}

	public static void insertCrewMember(String accountId, int crewRoleId)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "INSERT INTO user_crew_role(account_id, crew_role_id) VALUES (?, ?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, accountId);
			stmt.setInt(2, crewRoleId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Insert member to crew is failed!");
		}

	}

	public static void updateCrewMember(String accountId, int crewId, int newCrewRoleId)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
						UPDATE user_crew_role ucr
						JOIN crew_role cr ON cr.id = ucr.crew_role_id
						SET ucr.crew_role_id = ?
						WHERE ucr.account_id = ? AND cr.crew_id = ?
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, newCrewRoleId);
			stmt.setString(2, accountId);
			stmt.setInt(3, crewId);
			int row = stmt.executeUpdate();
			if (row == 0)
				throw new SQLException("Update member from crew is failed!");
		}
	}

	public static void deleteCrewMember(String accountId, int crewId)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
						DELETE ucr
						FROM user_crew_role ucr
						JOIN crew_role cr ON cr.id = ucr.crew_role_id
						WHERE ucr.account_id = ? AND cr.crew_id = ?
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, accountId);
			stmt.setInt(2, crewId);
			int row = stmt.executeUpdate();
		}
	}

	public static List<UserCrewRole> getAllByCrewId(String crew, int crewId) throws SQLException,
			IndexOutOfBoundsException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
						SELECT ua.username as name, cr.name as role
						FROM user_crew_role ucr
					 			JOIN user_account ua ON ucr.account_id = ua.id
						JOIN crew_role cr ON cr.id = ucr.crew_role_id
						WHERE cr.crew_id = ?
					""";
			List<UserCrewRole> list = new ArrayList<>();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, crewId);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				list.add(new UserCrewRole(crew, rs.getString("name"), rs.getString("role")));
				while (rs.next()) {
					list.add(new UserCrewRole(crew, rs.getString("name"), rs.getString("role")));
				}
			} else {
				throw new IndexOutOfBoundsException("Null Data");
			}

			return list;
		}

	}

	public static List<String> getRoleByAccountId(String accountId) throws SQLException, NotFoundException,
			NullPointerException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
						SELECT cr.name as role
						FROM user_crew_role ucr
					 			JOIN crew_role cr ON cr.id = ucr.crew_role_id
						WHERE ucr.account_id = ?
					""";
			List<String> list = new ArrayList<>();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, accountId);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				list.add(rs.getString("role"));
				while (rs.next()) {
					list.add(rs.getString("role"));
				}
			} else {
				throw new NullPointerException("Null Data");
			}

			return list;
		}
	}
}
