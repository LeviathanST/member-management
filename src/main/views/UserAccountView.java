package views;

import java.sql.Connection;

import constants.ResponseStatus;
import controllers.ApplicationController;
import dto.ResponseDTO;

public class UserAccountView extends View {
    public UserAccountView(Connection con) {
        super(con);
    }

    public void delete() {
        clearScreen();
        viewTitle("| DELETE USER ACCOUNT |", textIO);
        String username = textIO.newStringInputReader().read("Enter username to delete : ");
        ResponseDTO<Object> response = ApplicationController.deleteUserAccount(username);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else textIO.getTextTerminal().println(response.getMessage());
    }

    public void update() {
        viewTitle("| UPDATE ACCOUNT |", textIO);
        String username, password;
        do {
            username = textIO.newStringInputReader().read("Enter your user name : ");
        } while (checkUserName(username) == false);
        do {
            password = textIO.newStringInputReader().read("Enter your password : ");
        } while (validatePassword(password) == false);
        ResponseDTO<Object> response = ApplicationController.updateUserAccount( username, password);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else textIO.getTextTerminal().println(response.getMessage());
    }

    public Boolean checkUserName(String userName) {
        if(userName == null || userName == "" || userName.contains(" ")) {
            printError("User name can not be null or contains space.");
            return false;
        }
        return true;
    }


    public Boolean validatePassword(String password) {
        Boolean isValidPassword = true;

		if (password == null) {
			printError("Password can not be null.");
            isValidPassword = false;
		}

		if (password.length() <= 8) {
            printError("Password must be longer than 8 characters.");
            isValidPassword = false;
		}

		if (!password.matches(".*[A-Z].*")) {
            printError("Password must contain at least one uppercase letter.");
            isValidPassword = false;
		}

		if (!password.matches(".*[a-z].*")) {
            printError("Password must contain at least one lowercase letter.");
            isValidPassword = false;
		}

		if (!password.matches(".*\\d.*")) {
            printError("Password must contain at least one digit.");
		}

		if (!password.matches(".*[@#$%^&+=!].*")) {
            printError("Password must contain at least one special character (@#$%^&+=!).");
            isValidPassword = false;
		}

		return isValidPassword;
	}
}
