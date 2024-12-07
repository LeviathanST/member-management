package views;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import constants.ResponseStatus;
import controllers.ApplicationController;
import dto.ResponseDTO;
import models.UserAccount;
import models.UserProfile;
import repositories.users.UserAccountRepository;

public class GetAllProfilesAccounts extends View {
    public GetAllProfilesAccounts(Connection con) {
        super(con);
    }

    public void getAllProfiles() {
        clearScreen();
        viewTitle("| GET ALL USER PROFILES |", textIO);
        List<UserProfile> list = new ArrayList<>();
        ResponseDTO<List<UserProfile>> response = ApplicationController.getAllUserProfiles();
        list = response.getData();
        if (response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            for (UserProfile i : list) {
                textIO.getTextTerminal().println("Account ID: " + i.getAccountId());
                textIO.getTextTerminal().println("Full Name: " + i.getFullName());
                textIO.getTextTerminal().println("Sex: " + i.getSex());
                textIO.getTextTerminal().println("Student Code: " + i.getStudentCode());
                textIO.getTextTerminal().println("Email: " + i.getEmail());
                textIO.getTextTerminal().println("Contact Email: " + i.getContactEmail());
                textIO.getTextTerminal().println("Generation ID: " + i.getGenerationId());
                textIO.getTextTerminal().println("Date of Birth: " + i.getDateOfBirth());
            }
        }
    }

    public void getAllAccounts() {
        clearScreen();
        viewTitle("| GET ALL ACCOUNTS |", textIO);
        ResponseDTO<List<UserAccount>> response = ApplicationController.getAllUserAccounts();
        List<UserAccount> list = response.getData();
        if (response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            for (UserAccount i : list)
                textIO.getTextTerminal().println(i.getUsername());
        }

    }
}
