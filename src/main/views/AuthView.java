package views;

import dto.LoginDTO;
import dto.ResponseDTO;
import dto.SignUpDTO;
import dto.UserProfileDTO;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;
import exceptions.UserProfileException;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.lang.Object;
import constants.ResponseStatus;
import controllers.AuthController;

public class AuthView extends View{
    public AuthView(Connection con) {
        super(con);
    }

    public ResponseDTO<Object> Auth_view()
                throws DataEmptyException, UserProfileException, NotFoundException, ParseException, SQLException {

        ResponseDTO<Object> response = new ResponseDTO<Object>(ResponseStatus.NOT_FOUND, null, null);
        SignUpDTO signUp = new SignUpDTO();
        LoginDTO logIn = new LoginDTO();
        UserProfileDTO userProfile = new UserProfileDTO();
        int choice;
        do {
            clearScreen();
            choice = textIO.newIntInputReader().read("|-------------WELCOME-------------|\n" +
                                                         "|1 : Sign up                      |\n" + 
                                                         "|2: Log in                        |\n" + 
                                                         "|---------------------------------|\n" + 
                                                         "Enter your choice : ");
            clearScreen();
            switch (choice) {
                case 1:
                    response = SignUpForm(con, signUp);
                    break;
                case 2:
                    response = LogInForm(con, logIn);
                    break;
                default:
                printError("Invalid value.");
                    break;
            }
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTimeByMessage("Press enter to continue!");
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTimeByMessage("Press enter to continue!");
            }
        } while (response.getStatus() != ResponseStatus.OK);
        clearScreen();
        ResponseDTO<Object> status = new ResponseDTO<Object>(ResponseStatus.OK, "Login successfully", null);
        do {
            if(response.getMessage() == "Sign up successfully!") {
                UserProfileView userProfileView = new UserProfileView(con);
                status = userProfileView.insertUserProfile(con, userProfile, signUp);
            }
            if(status.getStatus() != ResponseStatus.OK){
                printError(status.getMessage());
                waitTimeByMessage("Press enter to continue!");
                clearScreen();
            }
            else textIO.getTextTerminal().println(status.getMessage());
            clearScreen();
        } while (status.getStatus() != ResponseStatus.OK);
        ApplicationView appView = new ApplicationView(con);
        appView.view();
        return response;
    }

    public ResponseDTO<Object> SignUpForm(Connection con, SignUpDTO data) {
        data.setUsername(textIO.newStringInputReader().read("Enter your user name : "));
        data.setPassword(textIO.newStringInputReader().read("Enter your password : "));
        data.setEmail(textIO.newStringInputReader().read("Enter your email : "));
        ResponseDTO<Object> status =  AuthController.signUp(con, data);
        return status;
    }

    public ResponseDTO<Object> LogInForm(Connection con, LoginDTO data) {
        data.setUsername(textIO.newStringInputReader().read("Enter your user name : "));
        data.setPassword(textIO.newStringInputReader().read("Enter your password : "));
        ResponseDTO<Object> status =  AuthController.login(con, data);
        return status;
    }
}

