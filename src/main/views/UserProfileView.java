package views;

import java.sql.Connection;
import constants.Sex;
import controllers.ApplicationController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.lang.Object;
import java.sql.Date;
import dto.LoginDTO;
import dto.ResponseDTO;
import dto.SignUpDTO;
import dto.UserProfileDTO;

public class UserProfileView extends View{
    public UserProfileView(Connection con) {
        super(con);
    }

    public ResponseDTO<Object> insertUserProfile (Connection con, UserProfileDTO user_profile, SignUpDTO signUp) {


        ResponseDTO<Object> response = null;
        textIO.getTextTerminal().println("INSERT YOUR PROFILE");
        user_profile.setFullName(textIO.newStringInputReader().read("Enter your full name : "));
        user_profile.setSex(textIO.newEnumInputReader(Sex.class).read("Enter your sex : "));
        user_profile.setStudentCode(textIO.newStringInputReader().read("Enter your roll number : "));
        user_profile.setContactEmail(textIO.newStringInputReader().read("Enter your contact email : "));

        String dateStr = textIO.newStringInputReader()
                        .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$") 
                        .read("Enter your birthdate (dd-MM-yyyy):");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date parsedDate;
        Date sqlDate;
        try {
            parsedDate = sdf.parse(dateStr);
            sqlDate = new java.sql.Date(parsedDate.getTime());
            user_profile.setDateOfBirth(sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        response = ApplicationController.createOneUserProfile(con, user_profile, signUp);
        return response;
    }

    public ResponseDTO<Object> ReadUserProfile (Connection con, UserProfileDTO user_profile, LoginDTO logIn) {
        ResponseDTO<Object> response = ApplicationController.readOneUserProfile(con, user_profile);
        return response; 
    }

    

}
