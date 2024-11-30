package views;

import dto.LoginDTO;
import dto.ResponseDTO;
import dto.SignUpDTO;
import java.sql.Connection;
import java.lang.Object;
import constants.ResponseStatus;
import controllers.ApplicationController;
import controllers.AuthController;

public class AuthView extends View{
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
        if(res.getStatus() != ResponseStatus.OK) {
            printError(res.getMessage());
        }
        String option = textIO.newStringInputReader().withNumberedPossibleValues("APPLICATION", "CREW", "GUILD", "BACK").read("");
        do {
            switch (option) {
                case "APPLICATION":
                    ApplicationView app = new ApplicationView(con);
                    app.view();
                    clearScreen();
                    break;
                case "CREW":
                    CrewView crew = new CrewView(con);
                    crew.view(con);
                    clearScreen();
                    break;
                case "GUILD":
                    GuildView guild = new GuildView(con);
                    guild.view(con);
                    clearScreen();
                    break;
                case "BACK":
                    Auth_view();
                    break;
                default:
                    printError("Invalid value!");
                    break;
            }
        } while (!option.equalsIgnoreCase("back"));
    }


    public void signUpForm(Connection con, SignUpDTO signUp) {
        viewTitle("| SIGN UP |", textIO);
        signUp.setUsername(textIO.newStringInputReader().read("Enter your user name : "));
        signUp.setPassword(textIO.newStringInputReader().read("Enter your password : "));
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
            if(res.getStatus() != ResponseStatus.OK)
                printError(res.getMessage());
            clearScreen();
            Auth_view();
        }
    }

    public void logInForm(Connection con, LoginDTO logIn) {
        ResponseDTO<Boolean> res = AuthController.checkAccessToken();
        ResponseDTO<Boolean> checkProfile;
        if(res.getStatus() == ResponseStatus.OK) {
            clearScreen();
            checkProfile = ApplicationController.checkToInsertProfile();
            if(checkProfile.getStatus() != ResponseStatus.OK) {
                textIO.getTextTerminal().println("Missing profile, please insert you profile.");
                waitTime(2000);
                UserProfileView profileView = new UserProfileView(con);
                profileView.addUserProfile(con);
            } else appCrewGuildView(con);
        } else {
            viewTitle("| LOG IN |", textIO);
            logIn.setUsername(textIO.newStringInputReader().read("Enter your user name : "));
            logIn.setPassword(textIO.newStringInputReader().read("Enter your password : "));
            ResponseDTO<Object> response = AuthController.login(logIn);

            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                clearScreen();
                checkProfile = ApplicationController.checkToInsertProfile();
                if(checkProfile.getStatus() != ResponseStatus.OK) {
                    textIO.getTextTerminal().println("Missing profile, please insert you profile.");
                    waitTime(2000);
                    UserProfileView profileView = new UserProfileView(con);
                    profileView.addUserProfile(con);
                    appCrewGuildView(con);
                } else appCrewGuildView(con);
            }
        }
    }
}

