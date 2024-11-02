package views;
import constants.ResponseStatus;
import dto.*;
import kotlin.Pair;
import models.permissions.GuildPermission;
import models.roles.GuildRole;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import controllers.GuildController;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuildView extends View {
    public GuildView (Connection connection){
        super(connection);
    }

    public void view(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("Guilds Tab",textIO);
        String option = textIO.newStringInputReader()
                .withNumberedPossibleValues("Guild", "Guild Role", "User Guild Role","Guild Event","Back")
                .read("");
        switch (option){
            case "Guild":
                clearScreen();
                viewGuild(connection);
                break;
            case "Guild Role":
                clearScreen();
                viewGuildRole(connection);
                break;
            case "User Guild Role":
                clearScreen();
                viewUserGuildRole(connection);
                break;
            case "Guild Event":
                clearScreen();
                viewGuildEvent(connection);
                break;
            case "Back":
                break;
        }
        textIO.dispose();
    }
    public String getGuildFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<String>> listGuilds = GuildController.getAllGuilds(connection);
        viewTitle("Choose Guild", textIO);
        String guildOption = textIO.newStringInputReader()
                .withNumberedPossibleValues(listGuilds.getData())
                .read("");
        if(listGuilds.getStatus() != ResponseStatus.OK) {
            printError(listGuilds.getMessage());
        }
        return guildOption;
    }
    public Pair<String,String> getGuildRoleFromList(Connection connection) {
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
        if(response.getStatus() != ResponseStatus.OK ) {
            printError(response.getMessage());
        }
        return new Pair<>(guildName, guildRole);
    }
    public UserGuildRoleDTO getUserGuildRoleFromList(Connection connection)  {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<UserGuildRoleDTO>> response;
        String guildName = getGuildFromList(connection);
        response = GuildController.getAllUserGuildRolesByGuildID(connection,guildName);
        if(response.getStatus() != ResponseStatus.OK ) {
            printError(response.getMessage());
            return null;
        }
        viewTitle("Choose Username", textIO);
        List<String> listUserRole = new ArrayList<>();
        for (UserGuildRoleDTO userGuildRoleDTO : response.getData()){
            listUserRole.add(userGuildRoleDTO.getUsername() + " - " + userGuildRoleDTO.getRole());
        }
        String selectedUserRole = textIO.newStringInputReader()
                .withNumberedPossibleValues(listUserRole)
                .read("");
        textIO.getTextTerminal().println(response.getStatus().toString());


        List<String> parts = List.of(selectedUserRole.split(" - "));

        return new UserGuildRoleDTO(guildName,parts.get(0),parts.get(1));
    }
    public String getPermissionFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<String>> listPermission = GuildController.getAllGuildPermissions(connection);
        viewTitle("Choose Permission", textIO);
        String permission = textIO.newStringInputReader()
                .withNumberedPossibleValues(listPermission.getData())
                .read("");
        if(listPermission.getStatus() != ResponseStatus.OK) {
            printError(listPermission.getMessage());
        }
        return permission;
    }
    public String getPermissionByGuildAndRoleFromList(Connection connection, String guildName, String role) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<String>> listPermission = GuildController.getAllPermissionByGuildId(connection,guildName,role);
        viewTitle("Choose Permission", textIO);
        String permission = textIO.newStringInputReader()
                .withNumberedPossibleValues(listPermission.getData())
                .read("");
        if(listPermission.getStatus() != ResponseStatus.OK) {
            printError(listPermission.getMessage());
        }
        return permission;
    }
    public int getGuildEventIDFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<GuildEventDto>> response = GuildController.getAllGuildEvent(connection);
        HashMap<String,Integer> guildEvents = new HashMap<String, Integer>();
        List<String> infoList = new ArrayList<>();
        viewTitle("Choose Event To Update", textIO);
        for (GuildEventDto guildEvent : response.getData()){
            guildEvents.put(guildEvent.getGuildName() + " - " + guildEvent.getTitle() + " - " + guildEvent.getType() + " - " + guildEvent.getGeneration(),guildEvent.getId());
            infoList.add(guildEvent.getGuildName() + " - " + guildEvent.getTitle() + " - " + guildEvent.getType() + " - " + guildEvent.getGeneration());

        }
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        }
        String selectedEvent = textIO.newStringInputReader()
                .withNumberedPossibleValues(infoList)
                .read("");

        return guildEvents.get(selectedEvent);

    }
    public String getGenerationFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<String>> listGeneration = GuildController.getAllGeneration(connection);
        viewTitle("Choose Generation", textIO);
        if(listGeneration.getStatus() != ResponseStatus.OK) {
            printError(listGeneration.getMessage());
        }
        return textIO.newStringInputReader()
                .withNumberedPossibleValues(listGeneration.getData())
                .read("");
    }
    //TODO: View Guild
    public void viewGuild(Connection connection){
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
        textIO.dispose();
    }
    public void viewListGuilds(Connection connection, String option){
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
    }
    public void viewCreateGuild(Connection connection, String option)  {
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
    }

    public void viewUpdateGuild(Connection connection, String option)  {
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
    }
    public void viewDeleteGuild(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String guildDeleted = getGuildFromList(connection);
            GuildDTO guildDTO = new GuildDTO(guildDeleted);
            response = GuildController.deleteGuild(connection,guildDTO);
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
    }

    //TODO: View Guild Role
    public void viewGuildRole(Connection connection){
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
    public void viewListGuildRoles(Connection connection, String option) {
        ResponseDTO<List<GuildRole>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option,textIO);
        response = GuildController.getAllGuildRoles(connection, getGuildFromList(connection));
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
    }
    public void viewAddGuildRole(Connection connection, String option) {
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
                viewGuildRole(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    public void viewUpdateGuildRole(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            Pair<String,String> guild = getGuildRoleFromList(connection);
            viewTitle("Input New Guild Role", textIO);
            String newGuildRole = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guild.getSecond(),guild.getFirst());
            GuildRoleDTO newGuildRoleDTO = new GuildRoleDTO(newGuildRole,guild.getFirst());
            response = GuildController.updateGuildRole(connection,guildRoleDTO,newGuildRoleDTO);
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
                viewGuildRole(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    public void viewDeleteGuildRole(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            Pair<String,String> guild = getGuildRoleFromList(connection);
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guild.getSecond(),guild.getFirst());
            response = GuildController.deleteGuildRole(connection,guildRoleDTO);
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
                viewGuildRole(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    //TODO: View User Guild Role
    public void viewUserGuildRole(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("User Guild Role",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("View User Guild Roles", "Add New User Guild Role", "Update Information User Guild Role","Delete User Guild Role","Guild Permission","Back")
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
            case "Guild Permission":
                viewGuildPermission(connection);
            case "Back":
                view(connection);
                break;
        }
        options = textIO.newStringInputReader().read();
        textIO.dispose();
    }
    public void viewListUserGuildRoles(Connection connection, String option) {
        ResponseDTO<List<UserGuildRoleDTO>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option,textIO);
        String guild = getGuildFromList(connection);
        response = GuildController.getAllUserGuildRolesByGuildID(connection, guild);
        if(response.getStatus() != ResponseStatus.OK ) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
            viewTitle(guild,textIO);
            for (UserGuildRoleDTO userGuildRoleDTO : response.getData()){
                textIO.getTextTerminal().print(userGuildRoleDTO.getUsername());
                textIO.getTextTerminal().print(" | ");
                textIO.getTextTerminal().print(userGuildRoleDTO.getRole());
                textIO.getTextTerminal().println();
            }
        }

        String BackToMenuOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("Back", "Back To Menu")
                .read("");
        switch (BackToMenuOrBack) {
            case "Back":
                viewUserGuildRole(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    public void viewAddUserGuildRole(Connection connection, String option) {
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
                viewUserGuildRole(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    public void viewUpdateUserGuildRole(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            UserGuildRoleDTO oldUserGuildRole = getUserGuildRoleFromList(connection);
            if (oldUserGuildRole == null){
                continueOrBack = AskContinueOrGoBack();
                break;
            }
            Pair<String,String> newGuildRole = getGuildRoleFromList(connection);
            UserGuildRoleDTO newUserGuildRole = new UserGuildRoleDTO(newGuildRole.getFirst(),oldUserGuildRole.getUsername(),newGuildRole.getSecond());
            response = GuildController.updateUserGuildRole(connection,oldUserGuildRole,newUserGuildRole);
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
                viewUserGuildRole(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    public void viewDeleteUserGuildRole(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            UserGuildRoleDTO userGuildRole = getUserGuildRoleFromList(connection);
            response = GuildController.deleteUserGuildRole(connection,userGuildRole);
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
                viewUserGuildRole(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    //TODO: View Guild Permission
    public void viewGuildPermission(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("Guild Permission",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues( "CRUD Permission","CRUD Permission To Guild Role","Back")
                .read("");
        switch (options){
            case "CRUD Permission":
                viewCRUDPermission(connection);
                break;
            case "CRUD Permission To Guild Role":
                viewCRUDPermissionToGuildRole(connection);
                break;
            case "Back":
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewCRUDPermission(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("Permission",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("View Permission", "Add Permission","Update Permission","Delete Permission","Back")
                .read("");
        switch (options){
            case "View Permission":
                viewPermission(connection,options);
                break;
            case "Add Permission":
                viewAddPermission(connection,options);
                break;
            case "Update Permission":
                viewUpdatePermission(connection,options);
                break;
            case "Delete Permission":
                viewDeletePermission(connection,options);
                break;
            case "Back":
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewPermission(Connection connection, String option){
        ResponseDTO<List<String>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        response = GuildController.getAllGuildPermissions(connection);
        viewTitle(option,textIO);
        for (String permission : response.getData()){
            textIO.getTextTerminal().println(permission);
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
                viewCRUDPermission(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    public void viewAddPermission(Connection connection, String option)  {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            viewTitle("Input Permission", textIO);
            String permission = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            response = GuildController.addGuildPermission(connection,permission);
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
                viewCRUDPermission(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }

    public void viewUpdatePermission(Connection connection, String option)  {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String permissionUpdated = getPermissionFromList(connection);
            viewTitle("Input New Permission", textIO);
            String permission = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            response = GuildController.updateGuildPermission(connection,permissionUpdated,permission);
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
                viewCRUDPermission(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    public void viewDeletePermission(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String permissionDeleted = getPermissionFromList(connection);
            response = GuildController.deleteGuildPermission(connection,permissionDeleted);
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
    }
    // TODO: CRUD Permission To Guild Role
    public void viewCRUDPermissionToGuildRole(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("Permission Of Guild Role",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("View Permission Of Guild Role", "Add Permission To Guild Role","Update Permission In Guild Role","Delete Permission In Guild Role","Back")
                .read("");
        switch (options){
            case "View Permission Of Guild Role":
                viewPermissionByGuildId(connection,options);
                break;
            case "Add Permission To Guild Role":
                viewAddPermissionToGuildRole(connection,options);
                break;
            case "Update Permission In Guild Role":
                viewUpdatePermissionInGuildRole(connection,options);
                break;
            case "Delete Permission In Guild Role":
                viewDeletePermissionInGuildRole(connection,options);
                break;
            case "Back":
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewAddPermissionToGuildRole(Connection connection, String option)  {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            viewTitle("Choose Guild And Role", textIO);
            Pair<String,String> guildAndRole = getGuildRoleFromList(connection);
            viewTitle("Choose Permission", textIO);
            String permissionAdded = getPermissionFromList(connection);
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guildAndRole.getSecond(),guildAndRole.getFirst());
            response = GuildController.addPermissionToGuildRole(connection,guildRoleDTO,permissionAdded);
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
                viewCRUDPermissionToGuildRole(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }

    public void viewUpdatePermissionInGuildRole(Connection connection, String option)  {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            viewTitle("Choose Guild And Role", textIO);
            Pair<String,String> guildAndRole = getGuildRoleFromList(connection);
            viewTitle("Choose Permission Updated", textIO);
            String permissionUpdated = getPermissionByGuildAndRoleFromList(connection,guildAndRole.getFirst(),guildAndRole.getSecond());
            viewTitle("Choose New Permission", textIO);
            String newPermission = getPermissionFromList(connection);
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guildAndRole.getSecond(),guildAndRole.getFirst());
            response = GuildController.updatePermissionInGuildRole(connection,guildRoleDTO,permissionUpdated,newPermission);
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
                viewCRUDPermissionToGuildRole(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    public void viewDeletePermissionInGuildRole(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            viewTitle("Choose Guild And Role", textIO);
            Pair<String,String> guildAndRole = getGuildRoleFromList(connection);
            viewTitle("Choose Permission Deleted", textIO);
            String permissionDeleted = getPermissionByGuildAndRoleFromList(connection,guildAndRole.getFirst(),guildAndRole.getSecond());
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guildAndRole.getSecond(),guildAndRole.getFirst());
            response = GuildController.deletePermissionInGuildRole(connection,guildRoleDTO,permissionDeleted);
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
                viewCRUDPermissionToGuildRole(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    public void viewPermissionByGuildId(Connection connection, String option){
        ResponseDTO<List<String>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        Pair<String,String> guildAndRole = getGuildRoleFromList(connection);
        response = GuildController.getAllPermissionByGuildId(connection,guildAndRole.getFirst(),guildAndRole.getSecond());
        viewTitle(option,textIO);
        for (String permission : response.getData()){
            textIO.getTextTerminal().println(permission);
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
                viewCRUDPermission(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    // TODO: Crew Event
    public void viewGuildEvent(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("Guild Event",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("View Guild Event", "Insert Guild Event","Update Guild Event","Delete Guild Event","Back")
                .read("");
        switch (options){
            case "View Guild Event":
                viewListGuildEvent(connection,options);
                break;
            case "Insert Guild Event":
                viewCreateGuildEvent(connection,options);
                break;
            case "Update Guild Event":
                viewUpdateGuildEvent(connection,options);
                break;
            case "Delete Guild Event":
                viewDeleteGuildEvent(connection,options);
                break;
            case "Back":
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewListGuildEvent(Connection connection, String option) {
        ResponseDTO<List<GuildEventDto>> response;
        response = GuildController.getAllGuildEvent(connection);
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option,textIO);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
        }
        for (GuildEventDto guildEvent : response.getData()){
            textIO.getTextTerminal().println("Crew: " + guildEvent.getGuildName());
            textIO.getTextTerminal().println("Title: " + guildEvent.getTitle());
            textIO.getTextTerminal().println("Description: " + guildEvent.getDescription());
            textIO.getTextTerminal().println("Start " + guildEvent.getStartAt().toString());
            textIO.getTextTerminal().println("End " + guildEvent.getEndAt().toString());
            textIO.getTextTerminal().println("Type: " + guildEvent.getType());
            textIO.getTextTerminal().println("---------------------------------------------------");
        }
        String BackToMenuOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("Back", "Back To Menu")
                .read("");
        switch (BackToMenuOrBack) {
            case "Back":
                viewGuildEvent(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    public void viewCreateGuildEvent(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String guildName = getGuildFromList(connection);
            String generation = getGenerationFromList(connection);
            String title = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("Input Title: ");
            String description=textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("Input Description: ");
            String start = textIO.newStringInputReader()
                    .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")
                    .read("Enter your date start (dd-MM-yyyy):");
            String end = textIO.newStringInputReader()
                    .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")
                    .read("Enter your date end (dd-MM-yyyy):");
            String type=textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("Input Type Of Event: ");
            GuildEventDto guildEvent = new GuildEventDto(guildName,generation,title,description,type);
            response = GuildController.addGuildEvent(connection, guildEvent,start,end);
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
                viewGuildEvent(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }

    public void viewUpdateGuildEvent(Connection connection, String option) {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            int guildEventId = getGuildEventIDFromList(connection);

            String guildName = getGuildFromList(connection);
            String generation = getGenerationFromList(connection);
            String title = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("Input Title: ");
            String description=textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("Input Description: ");
            String start = textIO.newStringInputReader()
                    .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")
                    .read("Enter your date start (dd-MM-yyyy):");
            String end = textIO.newStringInputReader()
                    .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")
                    .read("Enter your date end (dd-MM-yyyy):");
            String type=textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("Input Type Of Event: ");
            GuildEventDto guildEvent = new GuildEventDto(guildName,generation,title,description,type);
            response = GuildController.updateGuildEvent(connection, guildEvent,guildEventId,start,end);
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
                viewGuildEvent(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
    public void viewDeleteGuildEvent(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            int guildEventId = getGuildEventIDFromList(connection);
            response = GuildController.deleteGuildEvent(connection,guildEventId);
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
                viewGuildEvent(connection);
                break;
            case "Back To Menu":
                view(connection);
                break;
        }
    }
}
