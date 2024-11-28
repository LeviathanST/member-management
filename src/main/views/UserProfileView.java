package views;

import java.sql.Connection;
import constants.ResponseStatus;
import constants.Sex;
import controllers.ApplicationController;
import java.lang.Object;
import dto.ResponseDTO;
import dto.SignUpDTO;
import dto.UserProfileDTO;


public class UserProfileView extends View{
    public UserProfileView(Connection con) {
        super(con);
    }

    public void addUserProfile(Connection con) {
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, null);
        do {
            viewTitle("| INSERT YOUR PROFILE |", textIO);
            UserProfileDTO user_profile = new UserProfileDTO();
            user_profile.setFullName(textIO.newStringInputReader().read("Enter your full name : "));
            user_profile.setSex(textIO.newEnumInputReader(Sex.class).read("Enter your sex : "));
            user_profile.setStudentCode(textIO.newStringInputReader().read("Enter your roll number : "));
            user_profile.setEmail(textIO.newStringInputReader().read("Enter your email : "));
            user_profile.setContactEmail(textIO.newStringInputReader().withDefaultValue("\n").read("Enter your contact email (enter to skip): "));
            String dob = textIO.newStringInputReader().read("Enter your birthday (dd-MM-yyy) : ");
            response = ApplicationController.createOneUserProfile(user_profile, dob);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
            } else textIO.getTextTerminal().println(response.getMessage());
        } while (response.getStatus() != ResponseStatus.OK);
        clearScreen();
    }

   
}
