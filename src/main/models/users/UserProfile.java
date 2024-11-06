package models.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import constants.Sex;
import exceptions.NotFoundException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.UserProfileDTO;


public class UserProfile {
	
    public static void insert(Connection con, UserProfileDTO data)
			throws SQLException{

		String query = "INSERT INTO user_profile (account_id, full_name, sex, student_code, contact_email, generation_id, dob) VALUES (?, ?, ?, ?, ?, ?, ?)";
 		PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, data.getAccountId());
		stmt.setString(2, data.getFullName());
		stmt.setString(3, data.getSex().name());
		stmt.setString(4, data.getStudentCode());
		stmt.setString(5, data.getContactEmail());
		stmt.setInt(6, data.getGenerationId());
		stmt.setDate(7, data.getDateOfBirth());
		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0) {
			throw new SQLException("Insert failed : No rows affected.");
		}
	}

	public static void update(Connection con, UserProfileDTO data) throws SQLException{
		String query = "UPDATE user_profile SET full_name = ?, sex = ?, student_code = ?, contact_email = ?, generation_id = ?, dob = ? WHERE account_id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, data.getFullName());
		stmt.setString(2, data.getSex().name());
		stmt.setString(3, data.getStudentCode());
		stmt.setString(4, data.getContactEmail());
		stmt.setInt(5, data.getGenerationId());
		stmt.setDate(6, data.getDateOfBirth());
		stmt.setString(7, data.getAccountId());
	
		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0) {
			throw new SQLException("Update failed: No rows affected.");
		}
	}
	
	
	public static void delete(Connection con, String accountId) throws SQLException, NotFoundException {
		String query = "DELETE FROM user_profile WHERE account_id = ?";
		
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, accountId);
	
		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0) {
			throw new NotFoundException("Delete failed: No rows affected.");
		}
	}
	public static List<UserProfileDTO> findByUsername(Connection con, String username) throws SQLException, NotFoundException {
		String query = """
			SELECT *
   			FROM user_profile
   			WHERE username LIKE ?;
		""";
		List<UserProfileDTO> result = new ArrayList<>();
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, username);
		UserProfileDTO dto = new UserProfileDTO();
		ResultSet rs = stmt.executeQuery();
		if(!rs.next())
			throw new NotFoundException("User profile is not existed");
		while(rs.next()) {
			dto.setAccountId(rs.getString("account_id"));
			dto.setFullName(rs.getString("full_name"));
			dto.setSex(Sex.valueOf(rs.getString("sex")));
			dto.setStudentCode(rs.getString("student_code"));
			dto.setContactEmail(rs.getString("contact_email"));
			dto.setGenerationId(rs.getInt("generation_id"));
			dto.setDateOfBirth(rs.getDate("dob"));
			result.add(dto);
		}
		return result;
	}

	public static void read(Connection con, UserProfileDTO user_profile) throws SQLException, NotFoundException {
		String query = """
						SELECT account_id, full_name, sex, student_code, contact_email,
						generation_id, dob FROM user_profile WHERE account_id = ?
					""";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, user_profile.getAccountId());
		ResultSet rs = stmt.executeQuery();
		if(!rs.next())
			throw new NotFoundException("User profile is not existed.");
        user_profile.setAccountId(rs.getString("account_id"));
		user_profile.setFullName(rs.getString("full_name"));
		user_profile.setSex(Sex.valueOf(rs.getString("sex")));
		user_profile.setStudentCode(rs.getString("student_code"));
		user_profile.setContactEmail(rs.getString("contact_email"));
		user_profile.setGenerationId(rs.getInt("generation_id"));
		user_profile.setDateOfBirth(rs.getDate("dob"));
		
	}
	
}