package views;

import java.sql.Connection;
import java.sql.SQLException;
import constants.Sex;
import controllers.UserProfileController;
import java.text.SimpleDateFormat;
import java.lang.Object;

import dto.LoginDTO;
import dto.ResponseDTO;
import dto.SignUpDTO;
import dto.UserProfileDTO;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;
import exceptions.UserProfileException;
import models.users.UserAccount;

public class UserProfileView extends View{
    public UserProfileView(Connection con) {
        super(con);
    }

    public ResponseDTO<Object> insertUserProfile (Connection con, UserProfileDTO user_profile, SignUpDTO sign_up) throws 
                DataEmptyException, UserProfileException, NotFoundException, java.text.ParseException, SQLException {

        String account_id = UserAccount.getIdByUsername(con, sign_up.getUsername());
        ResponseDTO<Object> response = null;
        user_profile.setAccountId(account_id);
        textIO.getTextTerminal().println("INSERT YOUR PROFILE");
        user_profile.setFullName(textIO.newStringInputReader().read("Enter your full name : "));
        user_profile.setSex(textIO.newEnumInputReader(Sex.class).read("Enter your sex : "));
        user_profile.setStudentCode(textIO.newStringInputReader().read("Enter your roll number : "));
        user_profile.setContactEmail(textIO.newStringInputReader().read("Enter your contact email : "));

        String dateStr = textIO.newStringInputReader()
                        .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$") 
                        .read("Enter your birthdate (dd-MM-yyyy):");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date parsedDate = sdf.parse(dateStr);
        java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
        user_profile.setDateOfBirth(sqlDate);

        response = UserProfileController.CreateOne(con, user_profile);
        return response;
    }

    public ResponseDTO<Object> ReadUserProfile (Connection con, UserProfileDTO user_profile, LoginDTO log_in) throws SQLException, NotFoundException {
        String account_id = UserAccount.getIdByUsername(con, log_in.getUsername());
        user_profile.setAccountId(account_id);
        ResponseDTO<Object> response = UserProfileController.ReadOne(con, user_profile);
        return response; 
    }

}
