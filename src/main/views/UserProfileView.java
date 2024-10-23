package views;

import data.ProfileData;
import constants.Sex;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProfileView extends AuthView{

    public UserProfileView() {}

    public void ProfileView(ProfileData data) {
        data.setFullName( normalizeUsername(textIO.newStringInputReader().read("Enter Full Name:")));
        data.setSex(textIO.newEnumInputReader(Sex.class).read("Enter Sex:"));
        data.setStudentCode(normalizeStudentCode(textIO.newStringInputReader().read("Enter Student Code:")));
        data.setContactEmail(textIO.newStringInputReader().read("Enter contact email : "));
        data.setGeneration( textIO.newStringInputReader().read("Enter Generation :"));

        // Nhập ngày sinh (Date format theo dd/MM/yyyy)
        String dobString = textIO.newStringInputReader().read("Enter Date of Birth (dd/MM/yyyy):");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date parsed = sdf.parse(dobString);
            Date date_of_birth = new Date(parsed.getTime()); // chuyển đổi từ java.util.Date sang java.sql.Date
            data.setDateOfBirth(date_of_birth);
        } catch (ParseException e) {
            textIO.getTextTerminal().println("Invalid date format, setting default to null.");
        }
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


    public static String normalizeUsername(String username) {
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
