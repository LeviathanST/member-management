package services;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.users.UserProfile;
import data.ProfileData;
import exceptions.DataEmptyException;
import exceptions.ProfileException;


public class ProfileService {
    public static void InsertProfileInternal(Connection con, ProfileData data)  throws SQLException, DataEmptyException, ProfileException{
        data.setStudentCode(normalizeStudentCode(data.getStudentCode()));
        data.setFullName(normalizeFullname(data.getFullName()));
        if (data.getFullName() == null) 
			throw new DataEmptyException("Full name is empty!");
		else if (data.getSex() == null)
                throw new DataEmptyException("Sex is null!");
        else if (data.getStudentCode() == null)
                throw new DataEmptyException("Student code is null!");
        else if (data.getContactEmail() == null)
                throw new DataEmptyException("Contact email is null!");
        else if (data.getGeneration() == null)
                throw new DataEmptyException("Generation is null!");
        else if (isValidEmail(data.getContactEmail()) == false)
                throw new ProfileException("Invalid email!");
        else if(isValidGeneration(data.getGeneration()) == false)
                throw new ProfileException("Invalid generation!");
        UserProfile.insert(con, data);
        System.out.println("Update profile successfully!");
    }

    public static boolean isValidGeneration(String generation) {
        String regex = "[F]\\d{2}";
        Pattern pattern = Pattern.compile(regex);
        if(generation == null || generation.length() != 3) 
            return false;
        Matcher matcher = pattern.matcher(regex);
        return matcher.matches();
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

    public static String normalizeStudentCode(String student_code) {
        char[] tmp = student_code.toCharArray();
        String ans = student_code;
        if((tmp[0] != 's' && tmp[0] != 'S') || (tmp[1] != 'e' && tmp[1] != 'E') || tmp.length != 8)
            ans = null;
        else {
            tmp[0] = 'S';
            tmp[1] = 'E';
            ans = "";
            for(char x : tmp)
                ans += x;
        } 
        return ans;
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
