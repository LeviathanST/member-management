package repositories.users;

import config.Database;
import dto.UpdateProfileDTO;
import constants.Sex;
import exceptions.NotFoundException;
import models.UserProfile;
import utils.Pressessor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserProfileRepository {
	public static void insert(String username, Sex sex, int generationId, Date dob)
			throws SQLException, IOException, ClassNotFoundException,
			SQLIntegrityConstraintViolationException {
		String query = """
						INSERT INTO user_profile (account_id, sex, generation_id, dob)
						VALUES (
							(SELECT id FROM user_account WHERE username = ?),
							?, ?, ?
						)
				""";
		try (Connection con = Database.connection()) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, sex.name().toUpperCase());
			stmt.setInt(3, generationId);
			stmt.setDate(4, dob);
			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException("Insert profile failed!");
			}
		}

	}

	public static void update(UpdateProfileDTO data, String accountId)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "UPDATE user_profile SET full_name = ?, student_code = ?, sex = ?, email = ?, contact_email = ?, dob = ? WHERE account_id = ?";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setString(1, data.getFullName());
			stmt.setString(2, data.getStudentCode());
			stmt.setString(3, data.getSex().name());
			// TODO: Pls move validation to service after formatter is fixed :((
			stmt.setString(4, Pressessor.isValidEmail(data.getEmail()));
			stmt.setString(5, Pressessor.isValidEmail(data.getContactEmail()));
			stmt.setDate(6, data.getDateOfBirth());
			stmt.setString(7, accountId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException("Update failed: No rows affected.");
			}
		}
	}

	public static void delete(String accountId)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "DELETE FROM user_profile WHERE account_id = ?";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, accountId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new NotFoundException("Delete failed!");
			}
		}
	}

	/// NOTE:
	/// Read specified user profile
	public static UserProfile findByUsername(String username)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
					SELECT * FROM user_profile
					WHERE account_id = (SELECT id FROM user_account WHERE username = ?)
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				UserProfile userProfile = new UserProfile();
				userProfile.setAccountId(rs.getString("account_id"));
				userProfile.setFullName(rs.getString("full_name"));
				userProfile.setSex(Sex.valueOf(rs.getString("sex")));
				userProfile.setStudentCode(rs.getString("student_code"));
				userProfile.setEmail(rs.getString("email"));
				userProfile.setContactEmail(rs.getString("contact_email"));
				userProfile.setGenerationId(rs.getInt("generation_id"));
				userProfile.setDateOfBirth(rs.getDate("dob"));
				return userProfile;
			}

			throw new NotFoundException("Not found profile of %s".formatted(username));
		}
	}

	/// NOTE:
	/// For read myself
	public static UserProfile read(String accountId)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
						SELECT account_id, full_name, sex, student_code, email, contact_email,
						generation_id, dob FROM user_profile WHERE account_id = ?
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, accountId);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next())
				throw new NotFoundException("User profile is not existed.");

			UserProfile userProfile = new UserProfile(
					rs.getString("account_id"),
					rs.getString("full_name"),
					Sex.valueOf(rs.getString("sex")),
					rs.getString("student_code"),
					rs.getString("email"),
					rs.getString("contact_email"),
					rs.getInt("generation_id"),
					rs.getDate("dob") != null ? rs.getDate("dob")
							: new java.sql.Date(System.currentTimeMillis()));
			return userProfile;
		}
	}

	public static List<UserProfile> readAll() throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
						SELECT account_id, full_name, sex, student_code, email, contact_email,
						generation_id, dob FROM user_profile
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			List<UserProfile> list = new ArrayList<>();
			while (rs.next()) {
				UserProfile userProfile = new UserProfile();
				userProfile.setAccountId(rs.getString("account_id"));
				userProfile.setFullName(rs.getString("full_name"));
				userProfile.setSex(Sex.valueOf(rs.getString("sex")));
				userProfile.setStudentCode(rs.getString("student_code"));
				userProfile.setContactEmail(rs.getString("email"));
				userProfile.setContactEmail(rs.getString("contact_email"));
				userProfile.setGenerationId(rs.getInt("generation_id"));
				userProfile.setDateOfBirth(rs.getDate("dob"));

				list.add(userProfile);
			}

			return list;
		}
	}

}
