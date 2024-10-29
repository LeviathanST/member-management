package views;
import constants.ResponseStatus;
import dto.GuildDTO;
import dto.ResponseDTO;
import exceptions.DataEmptyException;
import exceptions.InvalidDataException;
import exceptions.NotFoundException;
import models.roles.GuildRole;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import controllers.GuildController;

import java.sql.Connection;
import java.sql.SQLException;
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
            continueOrBack = textIO.newStringInputReader()
                    .withNumberedPossibleValues("Continue", "Back", "Back To Menu")
                    .read("");
            if (!continueOrBack.equals("Continue")) {
                break;
            }
        }while (true);
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
        ResponseDTO<List<String>> listGuilds = GuildController.getAllGuilds(connection);
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String guildOption = textIO.newStringInputReader()
                    .withNumberedPossibleValues(listGuilds.getData())
                    .read("");
            viewTitle("Input Guild Name", textIO);
            String guildName = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildDTO oldGuildDTO = new GuildDTO(guildOption);
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
            continueOrBack = textIO.newStringInputReader()
                    .withNumberedPossibleValues("Continue", "Back", "Back To Menu")
                    .read("");
            if (!continueOrBack.equals("Continue")) {
                break;
            }
        } while (true);
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
        ResponseDTO<List<String>> listGuilds = GuildController.getAllGuilds(connection);
        while (true) {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String guildOption = textIO.newStringInputReader()
                    .withNumberedPossibleValues(listGuilds.getData())
                    .read("");
            GuildDTO guildDTO = new GuildDTO(guildOption);
            response = GuildController.deleteGuild(connection,guildDTO);
            textIO.getTextTerminal().println(response.getStatus().toString());
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(500);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(500);
            }
            continueOrBack = textIO.newStringInputReader()
                    .withNumberedPossibleValues("Continue", "Back", "Back To Menu")
                    .read("");
            if (!continueOrBack.equals("Continue")) {
                break;
            }
        }
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
                break;
            case "Update Information Guild Role":
                break;
            case "Delete Guild Role":
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
        ResponseDTO<List<String>> listGuilds = GuildController.getAllGuilds(connection);
        ResponseDTO<List<GuildRole>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option,textIO);
        String guildOption = textIO.newStringInputReader()
                .withNumberedPossibleValues(listGuilds.getData())
                .read("");
        response = GuildController.getAllGuildRoles(connection,guildOption);
        for (GuildRole guild : response.getData()){
            textIO.getTextTerminal().println(guild.toString());
        }
        if(response.getStatus() != ResponseStatus.OK || listGuilds.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
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
}
