package views;

import dto.LoginDTO;
import dto.ResponseDTO;
import dto.SignUpDTO;
import java.sql.Connection;
import java.lang.Object;
import constants.ResponseStatus;
import controllers.AuthController;

public class AuthView extends View{
    public AuthView(Connection con) {
        super(con);
    }

    public ResponseDTO<Object> Auth_view(SignUpDTO sign_up, LoginDTO log_in) {
        ResponseDTO<Object> response = null;
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
                    response = SignUpForm(con, sign_up);
                    break;
                case 2:
                    response = LogInForm(con, log_in);
                    break;
                default:
                printError("Invalid value.");
                    break;
            }
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(5000);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(5000);
            }
        } while (response.getStatus() != ResponseStatus.OK);
        clearScreen();
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

