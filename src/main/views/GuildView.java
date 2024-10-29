package views;
import constants.ResponseStatus;
import dto.GuildDTO;
import dto.GuildRoleDTO;
import dto.ResponseDTO;
import dto.UserGuildRoleDTO;
import exceptions.DataEmptyException;
import exceptions.InvalidDataException;
import exceptions.NotFoundException;
import kotlin.Pair;
import models.roles.GuildRole;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import controllers.GuildController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuildView extends View {
    public GuildView (Connection connection){
        super(connection);
    }

    public void view(Connection connection) throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("Guilds Tab",textIO);
        String option = textIO.newStringInputReader()
                .withNumberedPossibleValues("Guild", "Guild Role", "User Guild Role","Back")
                .read("");
        switch (option){
            case "Guild":
                viewGuild(connection);
                break;
            case "Guild Role":
                viewGuildRole(connection);
                break;
            case "User Guild Role":
                break;
            case "Back":
                break;
        }
        textIO.dispose();
    }
    public String getGuildFromList(Connection connection) throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<String>> listGuilds = GuildController.getAllGuilds(connection);
        viewTitle("Choose Guild", textIO);
        String guildOption = textIO.newStringInputReader()
                .withNumberedPossibleValues(listGuilds.getData())
                .read("");
        if(listGuilds.getStatus() != ResponseStatus.OK) {
            printError(listGuilds.getMessage());
        } else {
            textIO.getTextTerminal().println(listGuilds.getMessage());
        }
        return guildOption;
    }
    public Pair<String,String> getGuildRoleFromList(Connection connection) throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<GuildRole>> response;
        String guildName = getGuildFromList(connection);
        viewTitle("Choose Guild Role", textIO);
        response = GuildController.getAllGuildRoles(connection,guildName);
        List<String> listRole = new ArrayList<>();
        for (GuildRole guildRole : response.getData()){
            listRole.add(guildRole.getName());
        }
        String guildRole = textIO.newStringInputReader()
                .withNumberedPossibleValues(listRole)
                .read("");
        textIO.getTextTerminal().println(response.getStatus().toString());
        if(response.getStatus() != ResponseStatus.OK ) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
        }
        return new Pair<>(guildName, guildRole);
    }
    public UserGuildRoleDTO getUserGuildRoleFromList(Connection connection) throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<UserGuildRoleDTO>> response;
        String guildName = getGuildFromList(connection);
        viewTitle("Choose Username", textIO);
        response = GuildController.getAllUserGuildRolesByGuildID(connection,guildName);
        List<String> listUserRole = new ArrayList<>();
        for (UserGuildRoleDTO userGuildRoleDTO : response.getData()){
            listUserRole.add(userGuildRoleDTO.getUsername() + " - " + userGuildRoleDTO.getRole());
        }
        String selectedUserRole = textIO.newStringInputReader()
                .withNumberedPossibleValues(listUserRole)
                .read("");
        textIO.getTextTerminal().println(response.getStatus().toString());

        if(response.getStatus() != ResponseStatus.OK ) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
        }
        List<String> parts = List.of(selectedUserRole.split(" - "));
        return new UserGuildRoleDTO(guildName,parts.get(0),parts.get(1));
    }
    //TODO: View Guild
    public void viewGuild(Connection connection) throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("Guild",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("View Guilds", "Create New Guild", "Update Information Guild","Delete Guild","Back")
                .read("");
        switch (options){
            case "View Guilds":
                viewListGuilds(connection,options);
                break;
            case "Create New Guild":
                viewCreateGuild(connection,options);
                break;
            case "Update Information Guild":
                viewUpdateGuild(connection,options);
                break;
            case "Delete Guild":
                viewDeleteGuild(connection,options);
                break;
            case "Back":
                view(connection);
                break;
        }
        options = textIO.newStringInputReader().read();
        textIO.dispose();
    }
    public ResponseDTO<List<String>> viewListGuilds(Connection connection, String option) throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        ResponseDTO<List<String>> response = null;
        response = GuildController.getAllGuilds(connection);
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option,textIO);
        for (String guild : response.getData()){
            textIO.getTextTerminal().println(guild);
        }
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
        }
        String BackToMenuOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("Back", "Back To Menu")
                .read("");
        switch (BackToMenuOrBack) {
            case "Back":
                viewGuild(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
        return response;
    }
    public ResponseDTO<Object> viewCreateGuild(Connection connection, String option) throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            viewTitle("Input Guild Name", textIO);
            String guildName = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildDTO guildDTO = new GuildDTO(guildName);
            response = GuildController.addGuild(connection,guildDTO);
            textIO.getTextTerminal().println(response.getStatus().toString());
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(500);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(500);
            }
            continueOrBack = AskContinueOrGoBack();
        }while (continueOrBack.equals("Continue"));
        switch (continueOrBack){
            case "Back":
                viewGuild(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
        return response;
    }

    public ResponseDTO<Object> viewUpdateGuild(Connection connection, String option) throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String guildUpdated = getGuildFromList(connection);
            viewTitle("Input Guild Name", textIO);
            String guildName = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildDTO oldGuildDTO = new GuildDTO(guildUpdated);
            GuildDTO newGuildDTO = new GuildDTO(guildName);
            response = GuildController.updateGuild(connection,oldGuildDTO,newGuildDTO);
            textIO.getTextTerminal().println(response.getStatus().toString());
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(500);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(500);
            }
            continueOrBack = AskContinueOrGoBack();
        } while (continueOrBack.equals("Continue"));
        switch (continueOrBack){
            case "Back":
                viewGuild(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
        return response;
    }
    public ResponseDTO<Object> viewDeleteGuild(Connection connection, String option) throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String guildDeleted = getGuildFromList(connection);
            GuildDTO guildDTO = new GuildDTO(guildDeleted);
            response = GuildController.deleteGuild(connection,guildDTO);
            textIO.getTextTerminal().println(response.getStatus().toString());
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(500);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(500);
            }
            continueOrBack = AskContinueOrGoBack();
        } while (continueOrBack.equals("Continue"));
        switch (continueOrBack){
            case "Back":
                viewGuild(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
        return response;
    }

    //TODO: View Guild Role
    public void viewGuildRole(Connection connection) throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("Guild Role",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("View Guild Roles", "Add New Guild Role", "Update Information Guild Role","Delete Guild Role","Back")
                .read("");
        switch (options){
            case "View Guild Roles":
                viewListGuildRoles(connection,options);
                break;
            case "Add New Guild Role":
                viewAddGuildRole(connection,options);
                break;
            case "Update Information Guild Role":
                viewUpdateGuildRole(connection,options);
                break;
            case "Delete Guild Role":
                viewDeleteGuildRole(connection,options);
                break;
            case "Back":
                view(connection);
                break;
        }
        options = textIO.newStringInputReader().read();
        textIO.dispose();
    }
    public ResponseDTO<List<GuildRole>> viewListGuildRoles(Connection connection, String option)
            throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        ResponseDTO<List<GuildRole>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option,textIO);
        response = GuildController.getAllGuildRoles(connection, getGuildFromList(connection));
        textIO.getTextTerminal().println(response.getStatus().toString());
        if(response.getStatus() != ResponseStatus.OK ) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
        }
        for (GuildRole guild : response.getData()){
            textIO.getTextTerminal().println(guild.getName());
        }
        String BackToMenuOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("Back", "Back To Menu")
                .read("");
        switch (BackToMenuOrBack) {
            case "Back":
                viewGuildRole(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
        return response;
    }
    public ResponseDTO<Object> viewAddGuildRole(Connection connection, String option)
            throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String guild = getGuildFromList(connection);
            viewTitle("Input Guild Role", textIO);
            String guildRole = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guildRole,guild);
            response = GuildController.addGuildRole(connection,guildRoleDTO);
            textIO.getTextTerminal().println(response.getStatus().toString());
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(500);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(500);
            }
            continueOrBack = AskContinueOrGoBack();
        }while (continueOrBack.equals("Continue"));
        switch (continueOrBack){
            case "Back":
                viewGuild(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
        return response;
    }
    public ResponseDTO<Object> viewUpdateGuildRole(Connection connection, String option)
            throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            Pair<String,String> guild = getGuildRoleFromList(connection);
            String newGuild = getGuildFromList(connection);
            viewTitle("Input New Guild Role", textIO);
            String newGuildRole = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guild.getSecond(),guild.getFirst());
            GuildRoleDTO newGuildRoleDTO = new GuildRoleDTO(newGuildRole,newGuild);
            response = GuildController.updateGuildRole(connection,guildRoleDTO,newGuildRoleDTO);
            textIO.getTextTerminal().println(response.getStatus().toString());
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(500);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(500);
            }
            continueOrBack = AskContinueOrGoBack();
        } while (continueOrBack.equals("Continue"));
        switch (continueOrBack){
            case "Back":
                viewGuild(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
        return response;
    }
    public ResponseDTO<Object> viewDeleteGuildRole(Connection connection, String option)
            throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            Pair<String,String> guild = getGuildRoleFromList(connection);
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guild.getSecond(),guild.getFirst());
            response = GuildController.deleteGuildRole(connection,guildRoleDTO);
            textIO.getTextTerminal().println(response.getStatus().toString());
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(500);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(500);
            }
            continueOrBack = AskContinueOrGoBack();
        } while (continueOrBack.equals("Continue"));
        switch (continueOrBack){
            case "Back":
                viewGuild(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
        return response;
    }
    //TODO: View User Guild Role
    public void viewUserGuildRole(Connection connection) throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("User Guild Role",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("View User Guild Roles", "Add New User Guild Role", "Update Information User Guild Role","Delete User Guild Role","Back")
                .read("");
        switch (options){
            case "View User Guild Roles":
                viewListUserGuildRoles(connection,options);
                break;
            case "Add New User Guild Role":
                viewAddUserGuildRole(connection,options);
                break;
            case "Update Information User Guild Role":
                viewUpdateUserGuildRole(connection,options);
                break;
            case "Delete User Guild Role":
                viewDeleteUserGuildRole(connection,options);
                break;
            case "Back":
                view(connection);
                break;
        }
        options = textIO.newStringInputReader().read();
        textIO.dispose();
    }
    public ResponseDTO<List<UserGuildRoleDTO>> viewListUserGuildRoles(Connection connection, String option)
            throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        ResponseDTO<List<UserGuildRoleDTO>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option,textIO);
        String guild = getGuildFromList(connection);
        response = GuildController.getAllUserGuildRolesByGuildID(connection, guild);
        textIO.getTextTerminal().println(response.getStatus().toString());
        if(response.getStatus() != ResponseStatus.OK ) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
        }
        viewTitle(guild,textIO);
        for (UserGuildRoleDTO userGuildRoleDTO : response.getData()){
            textIO.getTextTerminal().print(userGuildRoleDTO.getUsername());
            textIO.getTextTerminal().print(" | ");
            textIO.getTextTerminal().print(userGuildRoleDTO.getRole());
            textIO.getTextTerminal().println();
        }
        String BackToMenuOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("Back", "Back To Menu")
                .read("");
        switch (BackToMenuOrBack) {
            case "Back":
                viewGuildRole(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
        return response;
    }
    public ResponseDTO<Object> viewAddUserGuildRole(Connection connection, String option)
            throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            viewTitle("Input Username", textIO);
            String username = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            Pair<String,String> pair = getGuildRoleFromList(connection);
            UserGuildRoleDTO userGuildRoleDTO = new UserGuildRoleDTO(pair.getFirst(),username,pair.getSecond());
            response = GuildController.addUserGuildRole(connection,userGuildRoleDTO);
            textIO.getTextTerminal().println(response.getStatus().toString());
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(500);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(500);
            }
            continueOrBack = AskContinueOrGoBack();
        }while (continueOrBack.equals("Continue"));
        switch (continueOrBack){
            case "Back":
                viewGuild(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
        return response;
    }
    public ResponseDTO<Object> viewUpdateUserGuildRole(Connection connection, String option)
            throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            Pair<String,String> newGuildRole = getGuildRoleFromList(connection);
            UserGuildRoleDTO oldUserGuildRole = getUserGuildRoleFromList(connection);
            UserGuildRoleDTO newUserGuildRole = new UserGuildRoleDTO(newGuildRole.getFirst(),oldUserGuildRole.getUsername(),newGuildRole.getSecond());
            response = GuildController.updateUserGuildRole(connection,oldUserGuildRole,newUserGuildRole);
            textIO.getTextTerminal().println(response.getStatus().toString());
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(500);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(500);
            }
            continueOrBack = AskContinueOrGoBack();
        } while (continueOrBack.equals("Continue"));
        switch (continueOrBack){
            case "Back":
                viewGuild(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
        return response;
    }
    public ResponseDTO<Object> viewDeleteUserGuildRole(Connection connection, String option)
            throws SQLException, DataEmptyException, NotFoundException, InvalidDataException {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            UserGuildRoleDTO userGuildRole = getUserGuildRoleFromList(connection);
            response = GuildController.deleteUserGuildRole(connection,userGuildRole);
            textIO.getTextTerminal().println(response.getStatus().toString());
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(500);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(500);
            }
            continueOrBack = AskContinueOrGoBack();
        } while (continueOrBack.equals("Continue"));
        switch (continueOrBack){
            case "Back":
                viewGuild(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
        return response;
    }
}
