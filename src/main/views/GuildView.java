package views;

import constants.ResponseStatus;
import controllers.GuildController;
import dto.*;
import kotlin.Pair;
import models.roles.GuildRole;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuildView extends View {
    public GuildView(Connection connection){
        super(connection);
    }

    public void view(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("GUILDS TAB",textIO);
        String option = textIO.newStringInputReader()
                .withNumberedPossibleValues("GUILD", "GUILD ROLE", "USER GUILD ROLE","GUILD EVENT","SEARCH","BACK")
                .read("");
        switch (option){
            case "GUILD":
                clearScreen();
                viewGuild(connection);
                break;
            case "GUILD ROLE":
                clearScreen();
                viewGuildRole(connection);
                break;
            case "USER GUILD ROLE":
                clearScreen();
                viewUserGuildRole(connection);
                break;
            case "GUILD EVENT":
                clearScreen();
                viewGuildEvent(connection);
                break;
            case "SEARCH":
                clearScreen();
                viewSearch(connection,option);
                break;
            case "BACK":
                clearScreen();
                AuthView authView = new AuthView(connection);
                authView.Auth_view();
                break;
        }
        textIO.dispose();
    }
    public String getGuildFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<String>> listGuilds = GuildController.getAllGuilds(connection);
        viewTitle("CHOOSE GUILD", textIO);
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
        viewTitle("CHOOSE GUILD ROLE", textIO);
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
        viewTitle("CHOOSE USERNAME", textIO);
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
        viewTitle("CHOOSE PERMISSION", textIO);
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
        viewTitle("CHOOSE PERMISSION", textIO);
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
        viewTitle("CHOOSE EVENT TO UPDATE", textIO);
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
        viewTitle("CHOOSE GENERATION", textIO);
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
        viewTitle("GUILD",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("VIEW GUILDS", "CREATE NEW GUILD", "UPDATE INFORMATION GUILD","DELETE GUILD","BACK")
                .read("");
        switch (options){
            case "VIEW GUILDS":
                viewListGuilds(connection,options);
                break;
            case "CREATE NEW GUILD":
                viewCreateGuild(connection,options);
                break;
            case "UPDATE INFORMATION GUILD":
                viewUpdateGuild(connection,options);
                break;
            case "DELETE GUILD":
                viewDeleteGuild(connection,options);
                break;
            case "BACK":
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewListGuilds(Connection connection, String option){
        ResponseDTO<List<String>> listMember ;
        ResponseDTO<UserProfileDTO> userprofile;
        TextIO textIO = TextIoFactory.getTextIO();
        String guildName = getGuildFromList(connection);
        listMember = GuildController.getMemberInGuild(connection,guildName);
        if(listMember.getStatus() != ResponseStatus.OK) {
            printError(listMember.getMessage());
        }
        else {
            textIO.getTextTerminal().println(listMember.getMessage());
            viewTitle(option,textIO);
            String username = textIO.newStringInputReader()
                    .withNumberedPossibleValues(listMember.getData())
                    .read("");
            userprofile = GuildController.getUserProfile(connection,username);
            textIO.getTextTerminal().println("FULL NAME: " + userprofile.getData().getFullName());
            textIO.getTextTerminal().println("SEX: " + userprofile.getData().getSex());
            textIO.getTextTerminal().println("STUDENT CODE: " + userprofile.getData().getStudentCode());
            textIO.getTextTerminal().println("CONTACT MAIL: " + userprofile.getData().getContactEmail());
            textIO.getTextTerminal().println("DATE OF BIRTH: " + userprofile.getData().getDateOfBirth());
            textIO.getTextTerminal().println("GENERATION: " + userprofile.getData().getGenerationName());
            textIO.getTextTerminal().println("---------------------------------------------------");
            if(userprofile.getStatus() != ResponseStatus.OK) {
                printError(userprofile.getMessage());
            } else {
                textIO.getTextTerminal().println(userprofile.getMessage());
            }
        }

        String BackToMenuOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("BACK", "BACK TO MENU")
                .read("");
        switch (BackToMenuOrBack) {
            case "BACK":
                viewGuild(connection);
                break;
            case "BACK TO MENU":
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
            viewTitle("INPUT GUILD NAME", textIO);
            String guildName = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildDTO guildDTO = new GuildDTO(guildName);
            response = GuildController.addGuild(connection,guildDTO);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());

            } else {
                textIO.getTextTerminal().println(response.getMessage());

            }
            continueOrBack = AskContinueOrGoBack();
        }while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewGuild(connection);
                break;
            case "BACK TO MENU":
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
            viewTitle("INPUT GUILD NAME", textIO);
            String guildName = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildDTO oldGuildDTO = new GuildDTO(guildUpdated);
            GuildDTO newGuildDTO = new GuildDTO(guildName);
            response = GuildController.updateGuild(connection,oldGuildDTO,newGuildDTO);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());

            } else {
                textIO.getTextTerminal().println(response.getMessage());

            }
            continueOrBack = AskContinueOrGoBack();
        } while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewGuild(connection);
                break;
            case "BACK TO MENU":
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

            } else {
                textIO.getTextTerminal().println(response.getMessage());

            }
            continueOrBack = AskContinueOrGoBack();
        } while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewGuild(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }

    //TODO: View Guild Role
    public void viewGuildRole(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("GUILD ROLE",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("VIEW GUILD ROLES", "ADD NEW GUILD ROLE", "UPDATE INFORMATION GUILD ROLE","DELETE GUILD ROLE","BACK")
                .read("");
        switch (options){
            case "VIEW GUILD ROLES":
                viewListGuildRoles(connection,options);
                break;
            case "ADD NEW GUILD ROLE":
                viewAddGuildRole(connection,options);
                break;
            case "UPDATE INFORMATION GUILD ROLE":
                viewUpdateGuildRole(connection,options);
                break;
            case "DELETE GUILD ROLE":
                viewDeleteGuildRole(connection,options);
                break;
            case "BACK":
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
                .withNumberedPossibleValues("BACK", "BACK TO MENU")
                .read("");
        switch (BackToMenuOrBack) {
            case "BACK":
                viewGuildRole(connection);
                break;
            case "BACK TO MENU":
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
            viewTitle("INPUT GUILD ROLE", textIO);
            String guildRole = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guildRole,guild);
            response = GuildController.addGuildRole(connection,guildRoleDTO);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());

            } else {
                textIO.getTextTerminal().println(response.getMessage());

            }
            continueOrBack = AskContinueOrGoBack();
        }while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewGuildRole(connection);
                break;
            case "BACK TO MENU":
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
            viewTitle("INPUT NEW GUILD ROLE", textIO);
            String newGuildRole = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guild.getSecond(),guild.getFirst());
            GuildRoleDTO newGuildRoleDTO = new GuildRoleDTO(newGuildRole,guild.getFirst());
            response = GuildController.updateGuildRole(connection,guildRoleDTO,newGuildRoleDTO);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());

            } else {
                textIO.getTextTerminal().println(response.getMessage());
            }
            continueOrBack = AskContinueOrGoBack();
        } while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewGuildRole(connection);
                break;
            case "BACK TO MENU":
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
            } else {
                textIO.getTextTerminal().println(response.getMessage());
            }
            continueOrBack = AskContinueOrGoBack();
        } while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewGuildRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    //TODO: View User Guild Role
    public void viewUserGuildRole(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("USER GUILD ROLE",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("VIEW USER GUILD ROLES", "ADD NEW USER GUILD ROLE", "UPDATE INFORMATION USER GUILD ROLE","DELETE USER GUILD ROLE","GUILD PERMISSION","BACK")
                .read("");
        switch (options){
            case "VIEW USER GUILD ROLES":
                viewListUserGuildRoles(connection,options);
                break;
            case "ADD NEW USER GUILD ROLE":
                viewAddUserGuildRole(connection,options);
                break;
            case "UPDATE INFORMATION USER GUILD ROLE":
                viewUpdateUserGuildRole(connection,options);
                break;
            case "DELETE USER GUILD ROLE":
                viewDeleteUserGuildRole(connection,options);
                break;
            case "GUILD PERMISSION":
                viewGuildPermission(connection);
            case "BACK":
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
                .withNumberedPossibleValues("BACK", "BACK TO MENU")
                .read("");
        switch (BackToMenuOrBack) {
            case "BACK":
                viewUserGuildRole(connection);
                break;
            case "BACK TO MENU":
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
            viewTitle("INPUT USERNAME", textIO);
            String username = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            Pair<String,String> pair = getGuildRoleFromList(connection);
            UserGuildRoleDTO userGuildRoleDTO = new UserGuildRoleDTO(pair.getFirst(),username,pair.getSecond());
            response = GuildController.addUserGuildRole(connection,userGuildRoleDTO);
            textIO.getTextTerminal().println(response.getStatus().toString());
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());

            } else {
                textIO.getTextTerminal().println(response.getMessage());

            }
            continueOrBack = AskContinueOrGoBack();
        }while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewUserGuildRole(connection);
                break;
            case "BACK TO MENU":
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
        } while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewUserGuildRole(connection);
                break;
            case "BACK TO MENU":
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
            if (userGuildRole == null){
                continueOrBack = AskContinueOrGoBack();
                break;
            }
            response = GuildController.deleteUserGuildRole(connection,userGuildRole);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(500);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(500);
            }
            continueOrBack = AskContinueOrGoBack();
        } while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewUserGuildRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    //TODO: View Guild Permission
    public void viewGuildPermission(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("GUILD PERMISSION",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues( "CRUD PERMISSION","CRUD PERMISSION TO GUILD ROLE","BACK")
                .read("");
        switch (options){
            case "CRUD PERMISSION":
                viewCRUDPermission(connection);
                break;
            case "CRUD PERMISSION TO GUILD ROLE":
                viewCRUDPermissionToGuildRole(connection);
                break;
            case "BACK":
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewCRUDPermission(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("PERMISSION",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("VIEW PERMISSION", "ADD PERMISSION","UPDATE PERMISSION","DELETE PERMISSION","BACK")
                .read("");
        switch (options){
            case "VIEW PERMISSION":
                viewPermission(connection,options);
                break;
            case "ADD PERMISSION":
                viewAddPermission(connection,options);
                break;
            case "UPDATE PERMISSION":
                viewUpdatePermission(connection,options);
                break;
            case "DELETE PERMISSION":
                viewDeletePermission(connection,options);
                break;
            case "BACK":
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
                .withNumberedPossibleValues("BACK", "BACK TO MENU")
                .read("");
        switch (BackToMenuOrBack) {
            case "BACK":
                viewCRUDPermission(connection);
                break;
            case "BACK TO MENU":
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
            viewTitle("INPUT PERMISSION", textIO);
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
        }while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewCRUDPermission(connection);
                break;
            case "BACK TO MENU":
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
            viewTitle("INPUT NEW PERMISSION", textIO);
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
        } while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewCRUDPermission(connection);
                break;
            case "BACK TO MENU":
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
        } while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewGuild(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    // TODO: CRUD Permission To Guild Role
    public void viewCRUDPermissionToGuildRole(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("PERMISSION OF GUILD ROLE",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("VIEW PERMISSION OF GUILD ROLE", "ADD PERMISSION TO GUILD ROLE","UPDATE PERMISSION IN GUILD ROLE","DELETE PERMISSION IN GUILD ROLE","BACK")
                .read("");
        switch (options){
            case "VIEW PERMISSION OF GUILD ROLE":
                viewPermissionByGuildId(connection,options);
                break;
            case "ADD PERMISSION TO GUILD ROLE":
                viewAddPermissionToGuildRole(connection,options);
                break;
            case "UPDATE PERMISSION IN GUILD ROLE":
                viewUpdatePermissionInGuildRole(connection,options);
                break;
            case "DELETE PERMISSION IN GUILD ROLE":
                viewDeletePermissionInGuildRole(connection,options);
                break;
            case "BACK":
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
            viewTitle("CHOOSE GUILD AND ROLE", textIO);
            Pair<String,String> guildAndRole = getGuildRoleFromList(connection);
            viewTitle("CHOOSE PERMISSION", textIO);
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
        }while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewCRUDPermissionToGuildRole(connection);
                break;
            case "BACK TO MENU":
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
            viewTitle("CHOOSE GUILD AND ROLE", textIO);
            Pair<String,String> guildAndRole = getGuildRoleFromList(connection);
            viewTitle("CHOOSE PERMISSION UPDATED", textIO);
            String permissionUpdated = getPermissionByGuildAndRoleFromList(connection,guildAndRole.getFirst(),guildAndRole.getSecond());
            viewTitle("CHOOSE NEW PERMISSION", textIO);
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
        } while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewCRUDPermissionToGuildRole(connection);
                break;
            case "BACK TO MENU":
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
            viewTitle("CHOOSE GUILD AND ROLE", textIO);
            Pair<String,String> guildAndRole = getGuildRoleFromList(connection);
            viewTitle("CHOOSE PERMISSION DELETED", textIO);
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
        } while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewCRUDPermissionToGuildRole(connection);
                break;
            case "BACK TO MENU":
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
                .withNumberedPossibleValues("BACK", "BACK TO MENU")
                .read("");
        switch (BackToMenuOrBack) {
            case "BACK":
                viewCRUDPermission(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    // TODO: Crew Event
    public void viewGuildEvent(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("GUILD EVENT",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("VIEW GUILD EVENT", "INSERT GUILD EVENT","UPDATE GUILD EVENT","DELETE GUILD EVENT","BACK")
                .read("");
        switch (options){
            case "VIEW GUILD EVENT":
                viewListGuildEvent(connection,options);
                break;
            case "INSERT GUILD EVENT":
                viewCreateGuildEvent(connection,options);
                break;
            case "UPDATE GUILD EVENT":
                viewUpdateGuildEvent(connection,options);
                break;
            case "DELETE GUILD EVENT":
                viewDeleteGuildEvent(connection,options);
                break;
            case "BACK":
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
            for (GuildEventDto guildEvent : response.getData()){
                textIO.getTextTerminal().println("CREW: " + guildEvent.getGuildName());
                textIO.getTextTerminal().println("TITLE: " + guildEvent.getTitle());
                textIO.getTextTerminal().println("DESCRIPTION: " + guildEvent.getDescription());
                textIO.getTextTerminal().println("START: " + guildEvent.getStartAt().toString());
                textIO.getTextTerminal().println("END " + guildEvent.getEndAt().toString());
                textIO.getTextTerminal().println("TYPE: " + guildEvent.getType());
                textIO.getTextTerminal().println("---------------------------------------------------");
            }
        }

        String BackToMenuOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("BACK", "BACK TO MENU")
                .read("");
        switch (BackToMenuOrBack) {
            case "BACK":
                viewGuildEvent(connection);
                break;
            case "BACK TO MENU":
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
                    .read("INPUT TITLE: ");
            String description=textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("INPUT DESCRIPTION: ");
            String start = textIO.newStringInputReader()
                    .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")
                    .read("ENTER YOUR DATE START (DD-MM-YYYY):");
            String end = textIO.newStringInputReader()
                    .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")
                    .read("ENTER YOUR DATE END (DD-MM-YYYY):");
            String type=textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("INPUT TYPE OF EVENT: ");
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
        }while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewGuildEvent(connection);
                break;
            case "BACK TO MENU":
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
                    .read("INPUT TITLE: ");
            String description=textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("INPUT DESCRIPTION: ");
            String start = textIO.newStringInputReader()
                    .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")
                    .read("ENTER YOUR DATE START (DD-MM-YYYY):");
            String end = textIO.newStringInputReader()
                    .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")
                    .read("ENTER YOUR DATE END (DD-MM-YYYY):");
            String type=textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("INPUT TYPE OF EVENT: ");
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
        } while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewGuildEvent(connection);
                break;
            case "BACK TO MENU":
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
            String guildName = getGuildFromList(connection);
            response = GuildController.deleteGuildEvent(connection,guildEventId,guildName);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
                waitTime(500);
            } else {
                textIO.getTextTerminal().println(response.getMessage());
                waitTime(500);
            }
            continueOrBack = AskContinueOrGoBack();
        } while (continueOrBack.equals("CONTINUE"));
        switch (continueOrBack){
            case "BACK":
                viewGuildEvent(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewSearch(Connection connection, String option) {
        ResponseDTO<List<UserProfileDTO>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        String username = textIO.newStringInputReader()
                .withDefaultValue(null)
                .read("INPUT USERNAME: ");
        response = GuildController.findByUsername(connection,username);
        viewTitle(option,textIO);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
            List<String> listUser = new ArrayList<>();
            for (UserProfileDTO userProfileDTO : response.getData()){
                listUser.add(userProfileDTO.getUserName() + " - " + userProfileDTO.getFullName() + " - " + userProfileDTO.getGenerationName());

            }
            String userOption = textIO.newStringInputReader()
                    .withNumberedPossibleValues(listUser)
                    .read("");
            String[] list = userOption.split(" - ");
            for (UserProfileDTO userProfileDTO : response.getData()){
                if (list[0].equals(userProfileDTO.getUserName())){
                    textIO.getTextTerminal().println("FULL NAME: " + userProfileDTO.getFullName());
                    textIO.getTextTerminal().println("SEX: " + userProfileDTO.getSex());
                    textIO.getTextTerminal().println("STUDENT CODE: " + userProfileDTO.getStudentCode());
                    textIO.getTextTerminal().println("CONTACT MAIL: " + userProfileDTO.getContactEmail());
                    textIO.getTextTerminal().println("DATE OF BIRTH: " + userProfileDTO.getDateOfBirth());
                    textIO.getTextTerminal().println("GENERATION: " + userProfileDTO.getGenerationName());
                    textIO.getTextTerminal().println("---------------------------------------------------");
                }

            }
        }

        String BackToMenuOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("BACK", "BACK TO MENU")
                .read("");
        switch (BackToMenuOrBack) {
            case "BACK":
                viewGuildEvent(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
}
