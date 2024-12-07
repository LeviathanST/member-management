package repositories.users;

import config.Database;
import constants.Sex;
import exceptions.NotFoundException;
import models.UserProfile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserProfileRepository {
	public static void insert(UserProfile data)
			throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "INSERT INTO user_profile (account_id, full_name, sex, student_code, email, contact_email, generation_id, dob) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, data.getAccountId());
			stmt.setString(2, data.getFullName());
			stmt.setString(3, data.getSex().name());
			stmt.setString(4, data.getStudentCode());
			stmt.setString(5, data.getEmail());
			stmt.setString(6, data.getContactEmail());
			stmt.setInt(7, data.getGenerationId());
			stmt.setDate(8, data.getDateOfBirth());
			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException("Insert failed : No rows affected.");
			}
		}

	}

	public static void update(UserProfile data) throws SQLException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = "UPDATE user_profile SET full_name = ?, sex = ?, student_code = ?, email = ?, contact_email = ?, generation_id = ?, dob = ? WHERE account_id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, data.getFullName());
			stmt.setString(2, data.getSex().name());
			stmt.setString(3, data.getStudentCode());
			stmt.setString(4, data.getEmail());
			stmt.setString(5, data.getContactEmail());
			stmt.setInt(6, data.getGenerationId());
			stmt.setDate(7, data.getDateOfBirth());
			stmt.setString(8, data.getAccountId());

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
				throw new NotFoundException("Delete failed: No rows affected.");
			}
		}
	}

	public static List<UserProfile> findByUsername(String username)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
						SELECT *
									FROM user_profile
									WHERE username LIKE ?;
					""";
			List<UserProfile> result = new ArrayList<>();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, username);
			UserProfile dto = new UserProfile();
			ResultSet rs = stmt.executeQuery();
			if (!rs.next())
				throw new NotFoundException("User profile is not existed");
			while (rs.next()) {
				dto.setAccountId(rs.getString("account_id"));
				dto.setFullName(rs.getString("full_name"));
				dto.setSex(Sex.valueOf(rs.getString("sex")));
				dto.setStudentCode(rs.getString("student_code"));
				dto.setEmail(rs.getString("email"));
				dto.setContactEmail(rs.getString("contact_email"));
				dto.setGenerationId(rs.getInt("generation_id"));
				dto.setDateOfBirth(rs.getDate("dob"));
				result.add(dto);
			}
			return result;
		}
	}

	public static void read(UserProfile user_profile)
			throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try (Connection con = Database.connection()) {
			String query = """
						SELECT account_id, full_name, sex, student_code, email, contact_email,
						generation_id, dob FROM user_profile WHERE account_id = ?
					""";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, user_profile.getAccountId());
			ResultSet rs = stmt.executeQuery();
			if (!rs.next())
				throw new NotFoundException("User profile is not existed.");
			StringBuilder gen = new StringBuilder();
			gen.append("F");
			gen.append(rs.getInt("generation_id"));
			String generation = gen.toString();
			user_profile.setAccountId(rs.getString("account_id"));
			user_profile.setFullName(rs.getString("full_name"));
			user_profile.setSex(Sex.valueOf(rs.getString("sex")));
			user_profile.setEmail(rs.getString("email"));
			user_profile.setContactEmail(rs.getString("contact_email"));
			user_profile.setStudentCode(rs.getString("student_code"));
			user_profile.setDateOfBirth(rs.getDate("dob"));
			user_profile.setGenerationName(generation);
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
