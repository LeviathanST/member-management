package models.users;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import exceptions.NotFoundException;
import java.sql.ResultSet;
import dto.UserProfileDTO;

public class UserProfile {
    public static String getAccountIdByEmail(Connection con, String  email) throws SQLException, NotFoundException{
		String query = "SELECT * FROM user_account WHERE email = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, email);
		ResultSet rs = stmt.executeQuery();
		if(!rs.next()) 
			throw new NotFoundException("User acount id is not existed!");
		return rs.getString("id");
	}

    public static int getGenerationId(Connection con, String name) throws SQLException, NotFoundException{
        String query = """
                SELECT id FROM generation WHERE name = ?
                """;
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
            throw new NotFoundException("Generation id is not existed!");
        return rs.getInt("id");
    }

    public static void insert(Connection con, UserProfileDTO data)
			throws SQLException, NotFoundException{

		String query = "INSERT INTO user_profile (account_id, full_name, sex, student_code, contact_email, generation_id, dob) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String account_id = getAccountIdByEmail(con, data.getContactEmail());
 		PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, account_id);
		stmt.setString(2, data.getFullName());
		stmt.setString(3, data.getSex().name());
		stmt.setString(4, data.getStudentCode());
		stmt.setString(5, data.getContactEmail());
		stmt.setInt(6, getGenerationId(con, data.getGeneration()));
		stmt.setDate(7, data.getDateOfBirth());
		int rowEffected = stmt.executeUpdate();
		if (rowEffected == 0) {
			throw new SQLException("Insert failed!");
		}
	}
}