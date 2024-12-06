package views;

import constants.GuildCommand;
import constants.GuildEventCommand;
import constants.GuildPermissionCommand;
import constants.GuildRoleCommand;
import constants.PermissionOfGuildRoleCommand;
import constants.ResponseStatus;
import constants.UserGuildRoleCommand;
import constants.ViewGuildCommand;
import constants.ViewGuildPermissionCommand;
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
        GuildCommand option;
        do {
            option = textIO.newEnumInputReader(GuildCommand.class).read("Enter your choice : ");
            switch (option){
                case GUILD:
                    clearScreen();
                    viewGuild(connection);
                    break;
                case GUILD_ROLE:
                    clearScreen();
                    viewGuildRole(connection);
                    break;
                case USER_GUILD_ROLE:
                    clearScreen();
                    viewUserGuildRole(connection);
                    break;
                case GUILD_EVENT:
                    clearScreen();
                    viewGuildEvent(connection);
                    break;
                case SEARCH:
                    clearScreen();
                    viewSearch(connection,option);
                    break;
                case BACK:
                    clearScreen();
                    AuthView authView = new AuthView(connection);
                    authView.appCrewGuildView(connection);
                    break;
            }
        } while (option != GuildCommand.BACK);
        textIO.dispose();
    }
    public String getGuildFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<String>> listGuilds = GuildController.getAllGuilds();
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
        response = GuildController.getAllGuildRoles(guildName);
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
        response = GuildController.getAllUserGuildRolesByGuildID(guildName);
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
        ResponseDTO<List<String>> listPermission = GuildController.getAllGuildPermissions();
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
        ResponseDTO<List<String>> listPermission = GuildController.getAllPermissionByGuildId(guildName,role);
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
        ResponseDTO<List<GuildEventDto>> response = GuildController.getAllGuildEvent();
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
        ResponseDTO<List<String>> listGeneration = GuildController.getAllGeneration();
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
        ViewGuildCommand options;
        do {
            options = textIO.newEnumInputReader(ViewGuildCommand.class).read("Enter your choice : ");
            switch (options){
                case VIEW_GUILDS:
                    viewListGuilds(connection,options);
                    break;
                case CREATE_NEW_GUILD:
                    viewCreateGuild(connection,options);
                    break;
                case UPDATE_INFORMATION_GUILD:
                    viewUpdateGuild(connection,options);
                    break;
                case DELETE_GUILD:
                    viewDeleteGuild(connection,options);
                    break;
                case BACK:
                    view(connection);
                    break;
            }
        } while (options != ViewGuildCommand.BACK);
        textIO.dispose();
    }
    public void viewListGuilds(Connection connection, ViewGuildCommand option){
        ResponseDTO<List<String>> listMember ;
        ResponseDTO<UserProfileDTO> userprofile;
        TextIO textIO = TextIoFactory.getTextIO();
        String guildName = getGuildFromList(connection);
        listMember = GuildController.getMemberInGuild(guildName);
        if(listMember.getStatus() != ResponseStatus.OK) {
            printError(listMember.getMessage());
        }
        else if (listMember.getData().isEmpty()){
            printError("Not member in this guild");
        }
        else {
            textIO.getTextTerminal().println(listMember.getMessage());
            viewTitle(option.toString(),textIO);
            String username = textIO.newStringInputReader()
                    .withNumberedPossibleValues(listMember.getData())
                    .read("");
            userprofile = GuildController.getUserProfile(username);
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
    public void viewCreateGuild(Connection connection, ViewGuildCommand option)  {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            viewTitle("INPUT GUILD NAME", textIO);
            String guildName = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildDTO guildDTO = new GuildDTO(guildName);
            response = GuildController.addGuild(guildDTO);
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

    public void viewUpdateGuild(Connection connection, ViewGuildCommand option)  {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            String guildUpdated = getGuildFromList(connection);
            viewTitle("INPUT GUILD NAME", textIO);
            String guildName = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildDTO oldGuildDTO = new GuildDTO(guildUpdated);
            GuildDTO newGuildDTO = new GuildDTO(guildName);
            response = GuildController.updateGuild(oldGuildDTO,newGuildDTO);
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
    public void viewDeleteGuild(Connection connection, ViewGuildCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            String guildDeleted = getGuildFromList(connection);
            GuildDTO guildDTO = new GuildDTO(guildDeleted);
            response = GuildController.deleteGuild(guildDTO);
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
        GuildRoleCommand options;
       do {
         options = textIO.newEnumInputReader(GuildRoleCommand.class).read("Enter your choice : ");
         switch (options){
             case VIEW_GUILDS_ROLES:
                 viewListGuildRoles(connection,options);
                 break;
             case ADD_NEW_GUILD_ROLE:
                 viewAddGuildRole(connection,options);
                 break;
             case UPDATE_INFORMATION_GUILD_ROLE:
                 viewUpdateGuildRole(connection,options);
                 break;
             case DELETE_GUILD_ROLE:
                 viewDeleteGuildRole(connection,options);
                 break;
             case BACK:
                 view(connection);
                 break;
         }
       } while (options != GuildRoleCommand.BACK);
        textIO.dispose();
    }
    public void viewListGuildRoles(Connection connection, GuildRoleCommand option) {
        ResponseDTO<List<GuildRole>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option.toString(),textIO);
        response = GuildController.getAllGuildRoles( getGuildFromList(connection));
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
    public void viewAddGuildRole(Connection connection, GuildRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            String guild = getGuildFromList(connection);
            viewTitle("INPUT GUILD ROLE", textIO);
            String guildRole = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guildRole,guild);
            response = GuildController.addGuildRole(guildRoleDTO);
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
    public void viewUpdateGuildRole(Connection connection, GuildRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            Pair<String,String> guild = getGuildRoleFromList(connection);
            viewTitle("INPUT NEW GUILD ROLE", textIO);
            String newGuildRole = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guild.getSecond(),guild.getFirst());
            GuildRoleDTO newGuildRoleDTO = new GuildRoleDTO(newGuildRole,guild.getFirst());
            response = GuildController.updateGuildRole(guildRoleDTO,newGuildRoleDTO);
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
    public void viewDeleteGuildRole(Connection connection, GuildRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            Pair<String,String> guild = getGuildRoleFromList(connection);
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guild.getSecond(),guild.getFirst());
            response = GuildController.deleteGuildRole(guildRoleDTO);
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
        UserGuildRoleCommand options;
       do {
         options = textIO.newEnumInputReader(UserGuildRoleCommand.class).read("Enter your choice : ");
         switch (options){
             case VIEW_USER_GUILD_ROLES:
                 viewListUserGuildRoles(connection,options);
                 break;
             case ADD_NEW_USER_GUIOLD_ROLE:
                 viewAddUserGuildRole(connection,options);
                 break;
             case UPDATE_INFORMATION_USER_GUILD_ROLE:
                 viewUpdateUserGuildRole(connection,options);
                 break;
             case DELETE_USER_GUILD_ROLE:
                 viewDeleteUserGuildRole(connection,options);
                 break;
             case GUILD_PERMISSION:
                 viewGuildPermission(connection);
             case BACK:
                 view(connection);
                 break;
         }
       } while (options != UserGuildRoleCommand.BACK);
        textIO.dispose();
    }
    public void viewListUserGuildRoles(Connection connection, UserGuildRoleCommand option) {
        ResponseDTO<List<UserGuildRoleDTO>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option.toString(),textIO);
        String guild = getGuildFromList(connection);
        response = GuildController.getAllUserGuildRolesByGuildID( guild);
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
    public void viewAddUserGuildRole(Connection connection, UserGuildRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            viewTitle("INPUT USERNAME", textIO);
            String username = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            Pair<String,String> pair = getGuildRoleFromList(connection);
            UserGuildRoleDTO userGuildRoleDTO = new UserGuildRoleDTO(pair.getFirst(),username,pair.getSecond());
            response = GuildController.addUserGuildRole(userGuildRoleDTO);
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
    public void viewUpdateUserGuildRole(Connection connection, UserGuildRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            UserGuildRoleDTO oldUserGuildRole = getUserGuildRoleFromList(connection);
            if (oldUserGuildRole == null){
                continueOrBack = AskContinueOrGoBack();
                break;
            }
            Pair<String,String> newGuildRole = getGuildRoleFromList(connection);
            UserGuildRoleDTO newUserGuildRole = new UserGuildRoleDTO(newGuildRole.getFirst(),oldUserGuildRole.getUsername(),newGuildRole.getSecond());
            response = GuildController.updateUserGuildRole(oldUserGuildRole,newUserGuildRole);
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
    public void viewDeleteUserGuildRole(Connection connection, UserGuildRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            UserGuildRoleDTO userGuildRole = getUserGuildRoleFromList(connection);
            if (userGuildRole == null){
                continueOrBack = AskContinueOrGoBack();
                break;
            }
            response = GuildController.deleteUserGuildRole(userGuildRole);
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
        ViewGuildPermissionCommand options;
        do {
            options = textIO.newEnumInputReader(ViewGuildPermissionCommand.class).read("Enter your choice : ");
            switch (options){
                case CRUD_PERMISSION:
                    viewCRUDPermission(connection);
                    break;
                case CRUD_PERMISSION_TO_GUILD_ROLE:
                    viewCRUDPermissionToGuildRole(connection);
                    break;
                case BACK:
                    view(connection);
                    break;
            }
        } while (options != ViewGuildPermissionCommand.BACK);
        textIO.dispose();
    }
    public void viewCRUDPermission(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("PERMISSION",textIO);
        GuildPermissionCommand options;
        do {
            options = textIO.newEnumInputReader(GuildPermissionCommand.class).read("Enter your choice  ");
            switch (options){
                case VIEW_PERMISSION:
                    viewPermission(connection,options);
                    break;
                case ADD_PERMISSION:
                    viewAddPermission(connection,options);
                    break;
                case UPDATE_PERMISISON:
                    viewUpdatePermission(connection,options);
                    break;
                case DELETE_PERMISSION:
                    viewDeletePermission(connection,options);
                    break;
                case BACK:
                    view(connection);
                    break;
            }
        } while (options != GuildPermissionCommand.BACK);
        textIO.dispose();
    }
    public void viewPermission(Connection connection, GuildPermissionCommand option){
        ResponseDTO<List<String>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        response = GuildController.getAllGuildPermissions();
        viewTitle(option.toString(),textIO);
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
    public void viewAddPermission(Connection connection, GuildPermissionCommand option)  {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            viewTitle("INPUT PERMISSION", textIO);
            String permission = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            response = GuildController.addGuildPermission(permission);
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

    public void viewUpdatePermission(Connection connection, GuildPermissionCommand option)  {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            String permissionUpdated = getPermissionFromList(connection);
            viewTitle("INPUT NEW PERMISSION", textIO);
            String permission = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            response = GuildController.updateGuildPermission(permissionUpdated,permission);
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
    public void viewDeletePermission(Connection connection, GuildPermissionCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            String permissionDeleted = getPermissionFromList(connection);
            response = GuildController.deleteGuildPermission(permissionDeleted);
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
        PermissionOfGuildRoleCommand options;
        do {
            options = textIO.newEnumInputReader(PermissionOfGuildRoleCommand.class).read("Enter your choice : ");
            switch (options){
                case VIEW_PERMISSION_OF_GUILD_ROLE:
                    viewPermissionByGuildId(connection,options);
                    break;
                case ADD_PERMISSION_TO_GUILD_ROLE:
                    viewAddPermissionToGuildRole(connection,options);
                    break;
                case UPDATE_INFORMATION_GUILD_ROLE:
                    viewUpdatePermissionInGuildRole(connection,options);
                    break;
                case DELETE_PERMISSION_IN_GUILD_ROLE:
                    viewDeletePermissionInGuildRole(connection,options);
                    break;
                case BACK:
                    view(connection);
                    break;
            }
        } while (options != PermissionOfGuildRoleCommand.BACK);
        textIO.dispose();
    }
    public void viewAddPermissionToGuildRole(Connection connection, PermissionOfGuildRoleCommand option)  {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            viewTitle("CHOOSE GUILD AND ROLE", textIO);
            Pair<String,String> guildAndRole = getGuildRoleFromList(connection);
            viewTitle("CHOOSE PERMISSION", textIO);
            String permissionAdded = getPermissionFromList(connection);
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guildAndRole.getSecond(),guildAndRole.getFirst());
            response = GuildController.addPermissionToGuildRole(guildRoleDTO,permissionAdded);
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

    public void viewUpdatePermissionInGuildRole(Connection connection, PermissionOfGuildRoleCommand option)  {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            viewTitle("CHOOSE GUILD AND ROLE", textIO);
            Pair<String,String> guildAndRole = getGuildRoleFromList(connection);
            viewTitle("CHOOSE PERMISSION UPDATED", textIO);
            String permissionUpdated = getPermissionByGuildAndRoleFromList(connection,guildAndRole.getFirst(),guildAndRole.getSecond());
            viewTitle("CHOOSE NEW PERMISSION", textIO);
            String newPermission = getPermissionFromList(connection);
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guildAndRole.getSecond(),guildAndRole.getFirst());
            response = GuildController.updatePermissionInGuildRole(guildRoleDTO,permissionUpdated,newPermission);
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
    public void viewDeletePermissionInGuildRole(Connection connection, PermissionOfGuildRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            viewTitle("CHOOSE GUILD AND ROLE", textIO);
            Pair<String,String> guildAndRole = getGuildRoleFromList(connection);
            viewTitle("CHOOSE PERMISSION DELETED", textIO);
            String permissionDeleted = getPermissionByGuildAndRoleFromList(connection,guildAndRole.getFirst(),guildAndRole.getSecond());
            GuildRoleDTO guildRoleDTO = new GuildRoleDTO(guildAndRole.getSecond(),guildAndRole.getFirst());
            response = GuildController.deletePermissionInGuildRole(guildRoleDTO,permissionDeleted);
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
    public void viewPermissionByGuildId(Connection connection, PermissionOfGuildRoleCommand option){
        ResponseDTO<List<String>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        Pair<String,String> guildAndRole = getGuildRoleFromList(connection);
        response = GuildController.getAllPermissionByGuildId(guildAndRole.getFirst(),guildAndRole.getSecond());
        viewTitle(option.toString(),textIO);
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
        GuildEventCommand options;
        do {
            options = textIO.newEnumInputReader(GuildEventCommand.class).read("Enter your choice : ");
            switch (options){
                case VIEW_GUILD_EVENT:
                    viewListGuildEvent(connection,options);
                    break;
                case ADD_GUILD_EVENT:
                    viewCreateGuildEvent(connection,options);
                    break;
                case UPDATE_GUILD_EVENT:
                    viewUpdateGuildEvent(connection,options);
                    break;
                case DELETE_GUILD_EVENT:
                    viewDeleteGuildEvent(connection,options);
                    break;
                case BACK:
                    view(connection);
                    break;
            }
        } while (options != GuildEventCommand.BACK);
        textIO.dispose();
    }
    public void viewListGuildEvent(Connection connection, GuildEventCommand option) {
        ResponseDTO<List<GuildEventDto>> response;
        response = GuildController.getAllGuildEvent();
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option.toString(),textIO);
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
    public void viewCreateGuildEvent(Connection connection, GuildEventCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
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
            response = GuildController.addGuildEvent( guildEvent,start,end);
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

    public void viewUpdateGuildEvent(Connection connection, GuildEventCommand option) {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
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
            response = GuildController.updateGuildEvent( guildEvent,guildEventId,start,end);
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
    public void viewDeleteGuildEvent(Connection connection, GuildEventCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            int guildEventId = getGuildEventIDFromList(connection);
            String guildName = getGuildFromList(connection);
            response = GuildController.deleteGuildEvent(guildEventId,guildName);
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
    public void viewSearch(Connection connection, GuildCommand option) {
        ResponseDTO<List<UserProfileDTO>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        String username = textIO.newStringInputReader()
                .withDefaultValue(null)
                .read("INPUT USERNAME: ");
        response = GuildController.findByUsername(username);
        viewTitle(option.toString(),textIO);
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