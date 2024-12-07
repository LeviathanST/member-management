package views;

import constants.MenuCommand;
import constants.ResponseStatus;
import controllers.ApplicationController;
import controllers.AuthController;
import dto.LoginDTO;
import dto.ResponseDTO;
import dto.SignUpDTO;

import java.io.File;
import java.sql.Connection;

public class AuthView extends View {
    private SignUpDTO signUp = new SignUpDTO();
    private LoginDTO logIn = new LoginDTO();

    public AuthView(Connection con) {
        super(con);
    }

    public void Auth_view() {
        int choice;
        do {
            clearScreen();
            choice = textIO.newIntInputReader().read("|------------------WELCOME------------------|\n" +
                    "|1 : SIGN UP                                |\n" +
                    "|2 : LOG IN                                 |\n" +
                    "|-------------------------------------------|\n" +
                    "Enter your choice : ");
            clearScreen();
            switch (choice) {
                case 1:
                    signUpForm(con, signUp);
                    waitTimeByMessage("Press enter to continue!");
                    break;
                case 2:
                    logInForm(con, logIn);
                    waitTimeByMessage("Press enter to continue!");
                    break;
                default:
                    break;
            }
        } while (true);
    }

    public void appCrewGuildView(Connection con) {
        viewTitle("| MENU |", textIO);
        ResponseDTO<Object> res = ApplicationController.makeNewGeneration();
        if (res.getStatus() != ResponseStatus.OK) {
            printError(res.getMessage());
        }
        MenuCommand option = textIO.newEnumInputReader(MenuCommand.class).read("Enter your choice : ");
        do {
            switch (option) {
                case APPLICATION:
                    ApplicationView app = new ApplicationView(con);
                    app.view();
                    clearScreen();
                    break;
                case CREW :
                    CrewView crew = new CrewView(con);
                    crew.view(con);
                    clearScreen();
                    break;
                case GUILD: 
                    GuildView guild = new GuildView(con);
                    guild.view(con);
                    clearScreen();
                    break;
                case BACK: 
                    logOut();
                    break;
                default:
                    break;
            }
        } while (option != MenuCommand.BACK);
    }

    public void signUpForm(Connection con, SignUpDTO signUp) {
        viewTitle("| SIGN UP |", textIO);
        String userName, password;
        do {
            userName = textIO.newStringInputReader().read("Enter your user name : ");
        } while (checkUserName(userName) == false);
        do {
            password = textIO.newStringInputReader().read("Enter your password : ");
        } while (validatePassword(password) == false);
        signUp.setUsername(userName);
        signUp.setPassword(password);
        ResponseDTO<Object> response =  AuthController.signUp(signUp);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
            waitTimeByMessage("Press enter to continue!");
            Auth_view();
        } else {
            textIO.getTextTerminal().println(response.getMessage());
            waitTimeByMessage("Press enter to continue!");
            clearScreen();
            ResponseDTO<Object> res = AuthController.changeAccessToken(signUp);
            if (res.getStatus() != ResponseStatus.OK)
                printError(res.getMessage());
            clearScreen();
            Auth_view();
        }
    }

    public void logOut() {
        String filePath = "D:\\final\\member-management\\storage.json";

        File file = new File(filePath);

        if (file.exists()) {
            file.delete();
            textIO.getTextTerminal().println("Log out successfully.");
        } 
    }
    

    public void logInForm(Connection con, LoginDTO logIn) {
        String userName, password;
        ResponseDTO<Boolean> checkInsertProfile;
        ResponseDTO<Boolean> checkAccessToken = AuthController.checkAccessToken();
        if(checkAccessToken.getStatus() != ResponseStatus.OK) {
            viewTitle("| LOG IN |", textIO);
            do {
                userName = textIO.newStringInputReader().read("Enter your user name : ");
            } while (checkUserName(userName) == false);
            password = textIO.newStringInputReader().read("Enter your password : ");
            logIn.setUsername(userName);
            logIn.setPassword(password);
            ResponseDTO<Object> checkLogIn  = AuthController.login(logIn);
            if(checkLogIn.getStatus() != ResponseStatus.OK) {
                printError(checkLogIn.getMessage());
                waitTimeByMessage("Press enter to continue!");
                clearScreen();
            } else {
                clearScreen();
                checkInsertProfile = ApplicationController.checkToInsertProfile();
                if(checkInsertProfile.getStatus() != ResponseStatus.OK) {

                    textIO.getTextTerminal().println("Missing profile, please insert you profile.");
                    waitTime(2000);
                    UserProfileView profileView = new UserProfileView(con);
                    profileView.addUserProfile(con);
                    clearScreen();
                    appCrewGuildView(con);
                } else {

                    clearScreen();
                    appCrewGuildView(con);
                }
            }
        } else {
                clearScreen();
                checkInsertProfile = ApplicationController.checkToInsertProfile();
                if(checkInsertProfile.getStatus() != ResponseStatus.OK) {
                    textIO.getTextTerminal().println("Missing profile, please insert you profile.");
                    waitTime(2000);
                    UserProfileView profileView = new UserProfileView(con);
                    profileView.addUserProfile(con);
                    clearScreen();
                    appCrewGuildView(con);
                } else {
                    clearScreen();
                    appCrewGuildView(con);
                }
        }
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
