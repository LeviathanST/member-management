package models.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import exceptions.NotFoundException;
import java.sql.ResultSet;
import data.ProfileData;

public class UserProfile {
	//Consider that should I input account_id into table user_profile
    public String getAccountId(Connection con, ProfileData data) throws SQLException, NotFoundException{
		String query = "SELECT * FROM user_account WHERE username = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, data.getUserName());
		ResultSet rs = stmt.executeQuery();
		if(!rs.next()) 
			throw new NotFoundException("User acount id is not existed!");
		return rs.getString("id");
	}

    public static void insert(Connection con, ProfileData data)
			throws SQLException{

		String query = "INSERT INTO user_profile (full_name, sex, student_code, contact_email, generation, dob) VALUES (?, ?, ?, ?, ?, ?)";

		PreparedStatement stmt = con.prepareStatement(query);

		stmt.setString(1, data.getFullName());
		stmt.setString(2, data.getSex().name());
		stmt.setString(3, data.getStudentCode());
		stmt.setString(4, data.getContactEmail());
		stmt.setString(5, data.getGeneration());
		stmt.setDate(6, data.getDateOfBirth());
		try {
			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException("Update user profile is failed, no row is affected!");
			}
			System.out.println("Update user profile is successfully!");
		} catch (SQLException e) {
			System.err.println("SQL error: " + e.getMessage());
			throw e;
		}
	}
}