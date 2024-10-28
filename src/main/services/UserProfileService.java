package services;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.ResultSet;
import models.users.UserProfile;
import dto.ResponseDTO;
import dto.UserProfileDTO;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;
import exceptions.UserProfileException;
import constants.ResponseStatus;


public class UserProfileService {
    public static void InsertProfileInternal(Connection con, UserProfileDTO data)  throws SQLException, DataEmptyException, UserProfileException, NotFoundException{
        data.setStudentCode((data.getStudentCode().toUpperCase()));
        data.setGenerationId(getMaxGenerationId(con));
        data.setFullName(normalizeFullname(data.getFullName()));
        if (data.getFullName() == null) 
			throw new DataEmptyException("Full name is empty!");
		else if (data.getSex() == null)
                throw new DataEmptyException("Sex is null!");
        else if (data.getStudentCode() == null)
                throw new DataEmptyException("Student code is null!");
        else if (data.getContactEmail() == null)
                throw new DataEmptyException("Contact email is null!");
        else if (isValidEmail(data.getContactEmail()) == false)
                throw new UserProfileException("Invalid email!");
        else if (isValidStudentCode(data.getStudentCode()) == false || data.getStudentCode() == null)
                throw new UserProfileException("Invalid student code!");
        UserProfile.insert(con, data);
    }


    public static ResponseDTO<Object> ReadUserProfileInternal(Connection con, UserProfileDTO data)  throws SQLException, NotFoundException{
        UserProfile.read(con, data);
        try {
            UserProfile.read(con, data);
            return new ResponseDTO<>(ResponseStatus.OK, "Update user profile successfully!", null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		} catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static int getMaxGenerationId(Connection con) throws SQLException, NotFoundException{
        String query = """
                SELECT MAX(id) AS max_id FROM generation
                """;
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
            throw new NotFoundException("Generation id is not existed!");
        return rs.getInt("max_id");
    }

    public static boolean isValidEmail(String email) {
        // Biểu thức chính quy kiểm tra định dạng email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        
        // Tạo Pattern từ regex
        Pattern pattern = Pattern.compile(emailRegex);
        
        // Nếu email rỗng hoặc null, trả về false
        if (email == null) {
            return false;
        }
        
        // Kiểm tra email với biểu thức chính quy
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidStudentCode(String student_code) {
        String regex = "[S][ASE]\\d{6}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(student_code);
        return matcher.matches();
    }


    public static String normalizeFullname(String username) {
        String[] parts = username.toLowerCase().split(" ");
        StringBuilder normalized = new StringBuilder();

        for (String part : parts) {
            if (!part.isEmpty()) {
                normalized.append(Character.toUpperCase(part.charAt(0)))
                          .append(part.substring(1)).append(" ");
            }
        }

        return normalized.toString().trim();
    }
}

