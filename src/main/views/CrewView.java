package views;

import constants.CrewCommand;
import constants.CrewEventCommand;
import constants.CrewPermissionCommand;
import constants.CrewRoleCommand;
import constants.PermissionOfCrewCommand;
import constants.ResponseStatus;
import constants.UserCrewRoleCommand;
import constants.ViewCrewCommand;
import controllers.CrewController;
import controllers.GuildController;
import dto.*;
import kotlin.Pair;
import models.permissions.CrewPermission;
import models.roles.CrewRole;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrewView extends View {
    public CrewView(Connection connection){
        super(connection);
    }

    public void view(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("CREW TAB",textIO);
        CrewCommand option;
        do {
            option = textIO.newEnumInputReader(CrewCommand.class).read("Enter your choice : ");
            switch (option){
                case CREW:
                    clearScreen();
                    viewCrew(connection);
                    break;
                case CREW_ROLE:
                    clearScreen();
                    viewCrewRole(connection);
                    break;
                case ROLE_CREW_TO_USER:
                    clearScreen();
                    viewUserCrewRole(connection);
                    break;
                case CREW_EVENT:
                    clearScreen();
                    viewCrewEvent(connection);
                    break;
                case BACK:
                    clearScreen();
                    AuthView authView = new AuthView(connection);
                    authView.appCrewGuildView(connection);
                    break;
            }
        } while (option != CrewCommand.BACK);
        textIO.dispose();
    }

    public String getCrewFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<String>> listCrews = CrewController.getAllCrews();
        viewTitle("Choose Crew", textIO);
        String crewOption = textIO.newStringInputReader()
                .withNumberedPossibleValues(listCrews.getData())
                .read("");
        if(listCrews.getStatus() != ResponseStatus.OK) {
            printError(listCrews.getMessage());
        } else {
            textIO.getTextTerminal().println(listCrews.getMessage());
        }
        return crewOption;
    }

    public Pair<String,String> getCrewRoleFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<CrewRole>> response;
        String crewName = getCrewFromList(connection);
        viewTitle("Choose Crew Role", textIO);
        response = CrewController.getAllCrewRoles(crewName);
        List<String> listRole = new ArrayList<>();
        for (CrewRole crewRole : response.getData()){
            listRole.add(crewRole.getName());
        }
        String crewRole = textIO.newStringInputReader()
                .withNumberedPossibleValues(listRole)
                .read("");
        textIO.getTextTerminal().println(response.getStatus().toString());
        if(response.getStatus() != ResponseStatus.OK ) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
        }
        return new Pair<>(crewName, crewRole);
    }
    public UserCrewRoleDto getUserCrewRoleFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<UserCrewRoleDto>> response;
        String crewName = getCrewFromList(connection);

        response = CrewController.getAllUserCrewRolesByCrewID(crewName);
        if(response.getStatus() != ResponseStatus.OK ) {
            printError(response.getMessage());
            return null;
        }
        viewTitle("Choose Username", textIO);
        List<String> listUserRole = new ArrayList<>();
        for (UserCrewRoleDto userCrewRoleDto : response.getData()){
            listUserRole.add(userCrewRoleDto.getUsername() + " - " + userCrewRoleDto.getRole());
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
        return new UserCrewRoleDto(crewName,parts.get(0),parts.get(1));
    }
    public String getPermissionFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<String>> listPermission = CrewController.getAllCrewPermissions();
        viewTitle("Choose Permission", textIO);
        String permission = textIO.newStringInputReader()
                .withNumberedPossibleValues(listPermission.getData())
                .read("");
        if(listPermission.getStatus() != ResponseStatus.OK) {
            printError(listPermission.getMessage());
        } else {
            textIO.getTextTerminal().println(listPermission.getMessage());
        }
        return permission;
    }
    public String getPermissionByCrewAndRoleFromList(Connection connection, String crew, String role) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<String>> listPermission = CrewController.getAllPermissionByCrewId(crew,role);
        viewTitle("Choose Permission", textIO);
        String permission = textIO.newStringInputReader()
                .withNumberedPossibleValues(listPermission.getData())
                .read("");
        if(listPermission.getStatus() != ResponseStatus.OK) {
            printError(listPermission.getMessage());
        }
        return permission;
    }
    public int getCrewEventIDFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<CrewEventDto>> response = CrewController.getAllCrewEvent();
        HashMap<String,Integer> crewEvents = new HashMap<String, Integer>();
        List<String> crewEventList = new ArrayList<>();
        viewTitle("Choose Event To Update", textIO);
        for (CrewEventDto crewEventDto : response.getData()){
            crewEvents.put(crewEventDto.getCrewName() + " - " + crewEventDto.getTitle() + " - " + crewEventDto.getType() + " - " + crewEventDto.getGeneration(),crewEventDto.getId());
            crewEventList.add(crewEventDto.getCrewName() + " - " + crewEventDto.getTitle() + " - " + crewEventDto.getType() + " - " + crewEventDto.getGeneration());
        }
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
        }
        String selectedEvent = textIO.newStringInputReader()
                .withNumberedPossibleValues(crewEventList)
                .read("");
        return crewEvents.get(selectedEvent);

    }
    public String getGenerationFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<String>> listGeneration = CrewController.getAllGeneration();
        viewTitle("Choose Generation", textIO);
        if(listGeneration.getStatus() != ResponseStatus.OK) {
            printError(listGeneration.getMessage());
        } else {
            textIO.getTextTerminal().println(listGeneration.getMessage());
        }
        return textIO.newStringInputReader()
                .withNumberedPossibleValues(listGeneration.getData())
                .read("");
    }

    //TODO: View Crew
    public void viewCrew(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("CREW",textIO);
        ViewCrewCommand options;
        do {
            options = textIO.newEnumInputReader(ViewCrewCommand.class).read("Enter your choice : ");
            switch (options){
                case VIEW_CREW:
                    viewListCrews(connection,options);
                    break;
                case CREATE_CREW:
                    viewCreateCrew(connection,options);
                    break;
                case UPDATE_CREW:
                    viewUpdateCrew(connection,options);
                    break;
                case DELETE_CREW:
                    viewDeleteCrew(connection,options);
                    break;
                case BACK:
                    view(connection);
                    break;
            }
        } while (options != ViewCrewCommand.BACK);
        textIO.dispose();
    }
    public void viewListCrews(Connection connection, ViewCrewCommand option) {
        ResponseDTO<List<String>> listMember;
        ResponseDTO<UserProfileDTO> userprofile;
        TextIO textIO = TextIoFactory.getTextIO();
        String crewName = getCrewFromList(connection);
        listMember = CrewController.getMemberCrew(crewName);
        if (listMember.getStatus() != ResponseStatus.OK) {
            printError(listMember.getMessage());
        } else if (listMember.getData().isEmpty()) {
            printError("Not member in this guild");
        } else {
            textIO.getTextTerminal().println(listMember.getMessage());
            viewTitle(option.toString(), textIO);
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
            if (userprofile.getStatus() != ResponseStatus.OK) {
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
                viewCrew(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewCreateCrew(Connection connection, ViewCrewCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            viewTitle("INPUT CREW NAME", textIO);
            String crewName = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            CrewDTO crewDTO = new CrewDTO(crewName);
            response = CrewController.addCrew( crewDTO);
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
                viewCrew(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }

    public void viewUpdateCrew(Connection connection, ViewCrewCommand option) {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            String crewUpdated = getCrewFromList(connection);
            viewTitle("INPUT NEW CREW", textIO);
            String newCrew = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            CrewDTO oldCrewDTO = new CrewDTO(crewUpdated);
            CrewDTO newCrewDTO = new CrewDTO(newCrew);
            response = CrewController.updateCrew(oldCrewDTO, newCrewDTO);
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
                viewCrew(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewDeleteCrew(Connection connection, ViewCrewCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            String crewDeleted = getCrewFromList(connection);
            CrewDTO crewDTO = new CrewDTO(crewDeleted);
            response = CrewController.deleteCrew(crewDTO);
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
                viewCrew(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    //TODO: View Crew Role
    public void viewCrewRole(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("CREW ROLE",textIO);
        CrewRoleCommand options = textIO.newEnumInputReader(CrewRoleCommand.class).read("Enter your choice : ");
        switch (options){
            case VIEW_CREW_ROLE:
                viewListCrewRoles(connection,options);
                break;
            case ADD_NEW_CREW_ROLE:
                viewAddCrewRole(connection,options);
                break;
            case UPDATE_INFORMATION_CREW_ROLE:
                viewUpdateCrewRole(connection,options);
                break;
            case DELETE_CREW_ROLE:
                viewDeleteCrewRole(connection,options);
                break;
            case BACK:
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewListCrewRoles(Connection connection, CrewRoleCommand option) {
        ResponseDTO<List<CrewRole>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option.toString(),textIO);
        response = CrewController.getAllCrewRoles( getCrewFromList(connection));
        if(response.getStatus() != ResponseStatus.OK ) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
        }
        for (CrewRole crewRole : response.getData()){
            textIO.getTextTerminal().println(crewRole.getName());
        }
        String BackToMenuOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("BACK", "BACK TO MENU")
                .read("");
        switch (BackToMenuOrBack) {
            case "BACK":
                viewCrewRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewAddCrewRole(Connection connection, CrewRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            String crew = getCrewFromList(connection);
            viewTitle("INPUT NEW CREW ROLE", textIO);
            String crewRole = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            CrewRoleDTO crewRoleDTO = new CrewRoleDTO(crewRole,crew);
            response = CrewController.addCrewRole(crewRoleDTO);
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
                viewCrewRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewUpdateCrewRole(Connection connection, CrewRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            Pair<String,String> crew = getCrewRoleFromList(connection);
            viewTitle("INPUT NEW CREW ROLE", textIO);
            String newCrewRole = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            CrewRoleDTO crewRoleDTO = new CrewRoleDTO(crew.getSecond(),crew.getFirst());
            CrewRoleDTO newCrewRoleDTO = new CrewRoleDTO(newCrewRole, crew.getFirst());
            response = CrewController.updateCrewRole(crewRoleDTO,newCrewRoleDTO);
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
                viewCrewRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewDeleteCrewRole(Connection connection, CrewRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            Pair<String,String> crew = getCrewRoleFromList(connection);
            CrewRoleDTO crewRoleDTO = new CrewRoleDTO(crew.getSecond(),crew.getFirst());
            response = CrewController.deleteCrewRole( crewRoleDTO);
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
                viewCrewRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    //TODO: View User Crew Role
    public void viewUserCrewRole(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("USER CREW ROLE",textIO);
        UserCrewRoleCommand options;
        do {
            options = textIO.newEnumInputReader(UserCrewRoleCommand.class).read("Enter your choice :");
            switch (options){
                case VIEW_USER_CREW_ROLES:
                    viewListUserCrewRoles(connection,options);
                    break;
                case ADD_NEW_USER_CREW_ROE:
                    viewAddUserCrewRole(connection,options);
                    break;
                case UPDATE_INFORMATION_USER_CREW_ROLE:
                    viewUpdateUserCrewRole(connection,options);
                    break;
                case DELETE_USER_CREW_ROLE:
                    viewDeleteUserCrewRole(connection,options);
                    break;
                case CREW_PERMISSION:
                    viewCrewPermission(connection);
                    break;
                case BACK:
                    view(connection);
                    break;
            }
        } while (options != UserCrewRoleCommand.BACK);
        textIO.dispose();
    }
    public void viewListUserCrewRoles(Connection connection, UserCrewRoleCommand option) {
        ResponseDTO<List<UserCrewRoleDto>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option.toString(),textIO);
        String crew = getCrewFromList(connection);
        response = CrewController.getAllUserCrewRolesByCrewID( crew);
        if(response.getStatus() != ResponseStatus.OK ) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
        }
        viewTitle(crew,textIO);
        for (UserCrewRoleDto userCrewRoleDto : response.getData()){
            textIO.getTextTerminal().print(userCrewRoleDto.getUsername());
            textIO.getTextTerminal().print(" | ");
            textIO.getTextTerminal().print(userCrewRoleDto.getRole());
            textIO.getTextTerminal().println();
        }
        String BackToMenuOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("BACK", "BACK TO MENU")
                .read("");
        switch (BackToMenuOrBack) {
            case "BACK":
                viewUserCrewRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewAddUserCrewRole(Connection connection, UserCrewRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            viewTitle("INPUT USERNAME", textIO);
            String username = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            Pair<String,String> pair = getCrewRoleFromList(connection);
            UserCrewRoleDto userCrewRoleDto = new UserCrewRoleDto(pair.getFirst(),username,pair.getSecond());
            response = CrewController.addUserCrewRole(userCrewRoleDto);
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
                viewUserCrewRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewUpdateUserCrewRole(Connection connection, UserCrewRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            UserCrewRoleDto oldUserCrewRole = getUserCrewRoleFromList(connection);

            if (oldUserCrewRole == null){
                continueOrBack = AskContinueOrGoBack();
                break;
            }
            Pair<String,String> newCrewRole = getCrewRoleFromList(connection);
            UserCrewRoleDto newUserCrewRole = new UserCrewRoleDto(newCrewRole.getFirst(),oldUserCrewRole.getUsername(),newCrewRole.getSecond());
            response = CrewController.updateUserCrewRole(oldUserCrewRole, newUserCrewRole);
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
                viewUserCrewRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewDeleteUserCrewRole(Connection connection, UserCrewRoleCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            UserCrewRoleDto userCrewRole = getUserCrewRoleFromList(connection);
            if (userCrewRole == null){
                continueOrBack = AskContinueOrGoBack();
                break;
            }
            response = CrewController.deleteUserCrewRole(userCrewRole);
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
                viewUserCrewRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    //TODO: View Guild Permission
    public void viewCrewPermission(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("CREW PERMISSION",textIO);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("VIEW CREW PERMISSION", "CRUD PERMISSION","CRUD PERMISSION TO CREW ROLE","BACK")
                .read("");
        switch (options){
            case "VIEW CREW PERMISSION":
                viewPermissionByAccountIdAndCrewId(connection,options);
                break;
            case "CRUD PERMISSION":
                viewCRUDPermission(connection);
                break;
            case "CRUD PERMISSION TO CREW ROLE":
                viewCRUDPermissionToCrewRole(connection);
                break;
            case "BACK":
                view(connection);
                break;
        }
        textIO.dispose();
    }

    public void viewPermissionByAccountIdAndCrewId(Connection connection, String option){
        ResponseDTO<List<CrewPermission>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        String crew = getCrewFromList(connection);
        viewTitle("INPUT USERNAME", textIO);
        String username = textIO.newStringInputReader()
                .withDefaultValue(null)
                .read("");
        response = CrewController.getAllPermissionByAccountId(crew,username);
        viewTitle(option,textIO);
        for (CrewPermission permission : response.getData()){
            textIO.getTextTerminal().println(permission.getName());
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

    public void viewCRUDPermission(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("PERMISSION",textIO);
        CrewPermissionCommand options = textIO.newEnumInputReader(CrewPermissionCommand.class).read("Enter your choice : ");
        switch (options){
            case VIEW_PERMISSION:
                viewPermission(connection,options);
                break;
            case ADD_PERMISSION:
                viewAddPermission(connection,options);
                break;
            case UPDATE_PERMISSION:
                viewUpdatePermission(connection,options);
                break;
            case DELETE_PERMISISON:
                viewDeletePermission(connection,options);
                break;
            case BACK:
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewPermission(Connection connection, CrewPermissionCommand option){
        ResponseDTO<List<String>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        response = CrewController.getAllCrewPermissions();
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
    public void viewAddPermission(Connection connection, CrewPermissionCommand option)  {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            viewTitle("INPUT PERMISSION", textIO);
            String permission = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            response = CrewController.addCrewPermission(permission);
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

    public void viewUpdatePermission(Connection connection, CrewPermissionCommand option)  {
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
            response = CrewController.updateCrewPermission(permissionUpdated,permission);
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
    public void viewDeletePermission(Connection connection, CrewPermissionCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            String permissionDeleted = getPermissionFromList(connection);
            response = CrewController.deleteCrewPermission(permissionDeleted);
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
    // TODO: CRUD Permission To Crew Role
    public void viewCRUDPermissionToCrewRole(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("PERMISSION OF CREW ROLE",textIO);
        PermissionOfCrewCommand options;
        do {
            options = textIO.newEnumInputReader(PermissionOfCrewCommand.class).read("Enter your choice : ");
            switch (options){
                case VIEW_PERMISSION_OF_CREW_ROLE:
                    viewPermissionByCrewId(connection,options);
                    break;
                case ADD_PERMISSION_TO_CREW_ROLE:
                    viewAddPermissionToCrewRole(connection,options);
                    break;
                case UPDATE_PERMISSION_IN_CREW_ROLE:
                    viewUpdatePermissionInCrewRole(connection,options);
                    break;
                case DELETE_PERMISSION_IN_CREW_ROLE:
                    viewDeletePermissionInCrewRole(connection,options);
                    break;
                case BACK:
                    view(connection);
                    break;
            }
        } while (options != PermissionOfCrewCommand.BACK);
        textIO.dispose();
    }
    public void viewAddPermissionToCrewRole(Connection connection, PermissionOfCrewCommand option)  {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            viewTitle("CHOOSE CREW AND ROLE", textIO);
            Pair<String,String> crewAndRole = getCrewRoleFromList(connection);
            viewTitle("CHOOSE PERMISSION", textIO);
            String permissionAdded = getPermissionFromList(connection);
            CrewRoleDTO crewRole = new CrewRoleDTO(crewAndRole.getSecond(),crewAndRole.getFirst());
            response = CrewController.addPermissionToCrewRole(crewRole,permissionAdded);
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
                viewCRUDPermissionToCrewRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }

    public void viewUpdatePermissionInCrewRole(Connection connection, PermissionOfCrewCommand option)  {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            viewTitle("CHOOSE CREW AND ROLE", textIO);
            Pair<String,String> crewAndRole = getCrewRoleFromList(connection);
            viewTitle("CHOOSE PERMISSION UPDATED", textIO);
            String permissionUpdated = getPermissionByCrewAndRoleFromList(connection,crewAndRole.getFirst(),crewAndRole.getSecond());
            viewTitle("CHOOSE NEW PERMISSION", textIO);
            String newPermission = getPermissionFromList(connection);
            CrewRoleDTO crewRole = new CrewRoleDTO(crewAndRole.getSecond(),crewAndRole.getFirst());
            response = CrewController.updatePermissionInCrewRole(crewRole,permissionUpdated,newPermission);
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
                viewCRUDPermissionToCrewRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewDeletePermissionInCrewRole(Connection connection, PermissionOfCrewCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            viewTitle("CHOOSE CREW AND ROLE", textIO);
            Pair<String,String> crewAndRole = getCrewRoleFromList(connection);
            viewTitle("CHOOSE PERMISSION DELETED", textIO);
            String permissionDeleted = getPermissionByCrewAndRoleFromList(connection,crewAndRole.getFirst(),crewAndRole.getSecond());
            CrewRoleDTO crewRole = new CrewRoleDTO(crewAndRole.getSecond(),crewAndRole.getFirst());
            response = CrewController.deletePermissionInCrewRole(crewRole,permissionDeleted);
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
                viewCRUDPermissionToCrewRole(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewPermissionByCrewId(Connection connection, PermissionOfCrewCommand option){
        ResponseDTO<List<String>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option.toString(),textIO);
        Pair<String,String> crewAndRole = getCrewRoleFromList(connection);
        response = CrewController.getAllPermissionByCrewId(crewAndRole.getFirst(),crewAndRole.getSecond());
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());

            for (String permission : response.getData()){
                textIO.getTextTerminal().println(permission);
            }
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
            } else {
                textIO.getTextTerminal().println(response.getMessage());
            }
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
    public void viewCrewEvent(Connection connection){
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle("CREW EVENT",textIO);
        CrewEventCommand options = textIO.newEnumInputReader(CrewEventCommand.class).read("Enter your choice : ");
        switch (options){
            case VIEW_CREW_EVENT:
                viewListCrewEvent(connection,options);
                break;
            case ADD_CREW_EVENT:
                viewCreateCrewEvent(connection,options);
                break;
            case UPDATE_CREW_EVENT:
                viewUpdateCrewEvent(connection,options);
                break;
            case DELETE_CREW_EVENT:
                viewDeleteCrewEvent(connection,options);
                break;
            case BACK:
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewListCrewEvent(Connection connection, CrewEventCommand option) {
        ResponseDTO<List<CrewEventDto>> response;
        response = CrewController.getAllCrewEvent();
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option.toString(),textIO);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
        }
        for (CrewEventDto crewEvent : response.getData()){
            textIO.getTextTerminal().println("CREW: " + crewEvent.getCrewName());
            textIO.getTextTerminal().println("TITLE: " + crewEvent.getTitle());
            textIO.getTextTerminal().println("DESCRIPTION: " + crewEvent.getDescription());
            textIO.getTextTerminal().println("START: " + crewEvent.getStartAt().toString());
            textIO.getTextTerminal().println("END: " + crewEvent.getEndAt().toString());
            textIO.getTextTerminal().println("TYPE: " + crewEvent.getType());
            textIO.getTextTerminal().println("---------------------------------------------------");
        }
        String BackToMenuOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("BACK", "BACK TO MENU")
                .read("");
        switch (BackToMenuOrBack) {
            case "BACK":
                viewCrewEvent(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewCreateCrewEvent(Connection connection, CrewEventCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            String crewName = getCrewFromList(connection);
            String generation = getGenerationFromList(connection);
            String title = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("INPUT TITLE");
            String description=textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("INPUT DESCRIPTION");
            String start = textIO.newStringInputReader()
                    .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")
                    .read("ENTER YOUR DATE START (DD-MM-YYYY):");
            String end = textIO.newStringInputReader()
                    .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")
                    .read("ENTER YOUR DATE END (DD-MM-YYYY):");
            String type=textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("TYPE OF EVENT");
            CrewEventDto crewEvent = new CrewEventDto(crewName,generation,title,description,type);
            response = CrewController.addCrewEvent( crewEvent,start,end);
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
                viewCrewEvent(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }

    public void viewUpdateCrewEvent(Connection connection, CrewEventCommand option) {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            int crewEventId = getCrewEventIDFromList(connection);
            String crewName = getCrewFromList(connection);
            String generation = getGenerationFromList(connection);
            String title = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("INPUT TITLE");
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
            CrewEventDto crewEvent = new CrewEventDto(crewName,generation,title,description,type);
            response = CrewController.updateCrewEvent( crewEvent,crewEventId,start,end);
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
                viewCrewEvent(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }
    public void viewDeleteCrewEvent(Connection connection, CrewEventCommand option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option.toString(), textIO);
            String crew = getCrewFromList(connection);
            int crewEventId = getCrewEventIDFromList(connection);
            response = CrewController.deleteCrewEvent(crewEventId,crew);
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
                viewCrewEvent(connection);
                break;
            case "BACK TO MENU":
                view(connection);
                break;
        }
    }

}