package views;

import constants.ResponseStatus;
import controllers.CrewController;
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
        String option = textIO.newStringInputReader()
                .withNumberedPossibleValues("CREW", "CREW ROLE", "ROLE CREW TO USER","CREW EVENT","BACK")
                .read("");
        switch (option){
            case "CREW":
                clearScreen();
                viewCrew(connection);
                break;
            case "CREW ROLE":
                clearScreen();
                viewCrewRole(connection);
                break;
            case "ROLE CREW TO USER":
                clearScreen();
                viewUserCrewRole(connection);
                break;
            case "CREW EVENT":
                clearScreen();
                viewCrewEvent(connection);
                break;
            case "BACK":
                clearScreen();
                AuthView authView = new AuthView(connection);
                authView.Auth_view();
                break;
        }
        textIO.dispose();
    }

    public String getCrewFromList(Connection connection) {
        TextIO textIO = TextIoFactory.getTextIO();
        ResponseDTO<List<String>> listCrews = CrewController.getAllCrews(connection);
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
        response = CrewController.getAllCrewRoles(connection,crewName);
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

        response = CrewController.getAllUserCrewRolesByCrewID(connection,crewName);
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
        ResponseDTO<List<String>> listPermission = CrewController.getAllCrewPermissions(connection);
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
        ResponseDTO<List<String>> listPermission = CrewController.getAllPermissionByCrewId(connection,crew,role);
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
        ResponseDTO<List<CrewEventDto>> response = CrewController.getAllCrewEvent(connection);
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
        ResponseDTO<List<String>> listGeneration = CrewController.getAllGeneration(connection);
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
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("VIEW CREW", "CREATE NEW CREW", "UPDATE INFORMATION","DELETE CREW","BACK")
                .read("");
        switch (options){
            case "VIEW CREW":
                viewListCrews(connection,options);
                break;
            case "CREATE NEW CREW":
                viewCreateCrew(connection,options);
                break;
            case "UPDATE INFORMATION":
                viewUpdateCrew(connection,options);
                break;
            case "DELETE CREW":
                viewDeleteCrew(connection,options);
                break;
            case "BACK":
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewListCrews(Connection connection, String option) {
        ResponseDTO<List<String>> response;
        response = CrewController.getAllCrews(connection);
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option,textIO);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            textIO.getTextTerminal().println(response.getMessage());
        }
        for (String crew : response.getData()){
            textIO.getTextTerminal().println(crew);
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
    public void viewCreateCrew(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            viewTitle("INPUT CREW NAME", textIO);
            String crewName = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            CrewDTO crewDTO = new CrewDTO(crewName);
            response = CrewController.addCrew(connection, crewDTO);
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

    public void viewUpdateCrew(Connection connection, String option) {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String crewUpdated = getCrewFromList(connection);
            viewTitle("INPUT NEW CREW", textIO);
            String newCrew = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            CrewDTO oldCrewDTO = new CrewDTO(crewUpdated);
            CrewDTO newCrewDTO = new CrewDTO(newCrew);
            response = CrewController.updateCrew(connection,oldCrewDTO, newCrewDTO);
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
    public void viewDeleteCrew(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String crewDeleted = getCrewFromList(connection);
            CrewDTO crewDTO = new CrewDTO(crewDeleted);
            response = CrewController.deleteCrew(connection,crewDTO);
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
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("VIEW CREW ROLE", "ADD NEW CREW ROLE", "UPDATE INFORMATION CREW ROLE","DELETE CREW ROLE","BACK")
                .read("");
        switch (options){
            case "VIEW CREW ROLE":
                viewListCrewRoles(connection,options);
                break;
            case "ADD NEW CREW ROLE":
                viewAddCrewRole(connection,options);
                break;
            case "UPDATE INFORMATION CREW ROLE":
                viewUpdateCrewRole(connection,options);
                break;
            case "DELETE CREW ROLE":
                viewDeleteCrewRole(connection,options);
                break;
            case "BACK":
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewListCrewRoles(Connection connection, String option) {
        ResponseDTO<List<CrewRole>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option,textIO);
        response = CrewController.getAllCrewRoles(connection, getCrewFromList(connection));
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
    public void viewAddCrewRole(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String crew = getCrewFromList(connection);
            viewTitle("INPUT NEW CREW ROLE", textIO);
            String crewRole = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            CrewRoleDTO crewRoleDTO = new CrewRoleDTO(crewRole,crew);
            response = CrewController.addCrewRole(connection,crewRoleDTO);
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
    public void viewUpdateCrewRole(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            Pair<String,String> crew = getCrewRoleFromList(connection);
            viewTitle("INPUT NEW CREW ROLE", textIO);
            String newCrewRole = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            CrewRoleDTO crewRoleDTO = new CrewRoleDTO(crew.getSecond(),crew.getFirst());
            CrewRoleDTO newCrewRoleDTO = new CrewRoleDTO(newCrewRole, crew.getFirst());
            response = CrewController.updateCrewRole(connection,crewRoleDTO,newCrewRoleDTO);
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
    public void viewDeleteCrewRole(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            Pair<String,String> crew = getCrewRoleFromList(connection);
            CrewRoleDTO crewRoleDTO = new CrewRoleDTO(crew.getSecond(),crew.getFirst());
            response = CrewController.deleteCrewRole(connection, crewRoleDTO);
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
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("VIEW USER CREW ROLES", "ADD NEW USER CREW ROLE", "UPDATE INFORMATION USER CREW ROLE","DELETE USER CREW ROLE","CREW PERMISSION","BACK")
                .read("");
        switch (options){
            case "VIEW USER CREW ROLES":
                viewListUserCrewRoles(connection,options);
                break;
            case "ADD NEW USER CREW ROLE":
                viewAddUserCrewRole(connection,options);
                break;
            case "UPDATE INFORMATION USER CREW ROLE":
                viewUpdateUserCrewRole(connection,options);
                break;
            case "DELETE USER CREW ROLE":
                viewDeleteUserCrewRole(connection,options);
                break;
            case "CREW PERMISSION":
                viewCrewPermission(connection);
                break;
            case "BACK":
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewListUserCrewRoles(Connection connection, String option) {
        ResponseDTO<List<UserCrewRoleDto>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option,textIO);
        String crew = getCrewFromList(connection);
        response = CrewController.getAllUserCrewRolesByCrewID(connection, crew);
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
    public void viewAddUserCrewRole(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            viewTitle("INPUT USERNAME", textIO);
            String username = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");
            Pair<String,String> pair = getCrewRoleFromList(connection);
            UserCrewRoleDto userCrewRoleDto = new UserCrewRoleDto(pair.getFirst(),username,pair.getSecond());
            response = CrewController.addUserCrewRole(connection,userCrewRoleDto);
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
    public void viewUpdateUserCrewRole(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            UserCrewRoleDto oldUserCrewRole = getUserCrewRoleFromList(connection);

            if (oldUserCrewRole == null){
                continueOrBack = AskContinueOrGoBack();
                break;
            }
            Pair<String,String> newCrewRole = getCrewRoleFromList(connection);
            UserCrewRoleDto newUserCrewRole = new UserCrewRoleDto(newCrewRole.getFirst(),oldUserCrewRole.getUsername(),newCrewRole.getSecond());
            response = CrewController.updateUserCrewRole(connection,oldUserCrewRole, newUserCrewRole);
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
    public void viewDeleteUserCrewRole(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            UserCrewRoleDto userCrewRole = getUserCrewRoleFromList(connection);
            if (userCrewRole == null){
                continueOrBack = AskContinueOrGoBack();
                break;
            }
            response = CrewController.deleteUserCrewRole(connection,userCrewRole);
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
        response = CrewController.getAllPermissionByAccountId(connection,crew,username);
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
        response = CrewController.getAllCrewPermissions(connection);
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
            response = CrewController.addCrewPermission(connection,permission);
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
            response = CrewController.updateCrewPermission(connection,permissionUpdated,permission);
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
            response = CrewController.deleteCrewPermission(connection,permissionDeleted);
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
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("VIEW PERMISSION OF CREW ROLE", "ADD PERMISSION TO CREW ROLE","UPDATE PERMISSION IN CREW ROLE","DELETE PERMISSION IN CREW ROLE","BACK")
                .read("");
        switch (options){
            case "VIEW PERMISSION OF CREW ROLE":
                viewPermissionByCrewId(connection,options);
                break;
            case "ADD PERMISSION TO CREW ROLE":
                viewAddPermissionToCrewRole(connection,options);
                break;
            case "UPDATE PERMISSION IN CREW ROLE":
                viewUpdatePermissionInCrewRole(connection,options);
                break;
            case "DELETE PERMISSION IN CREW ROLE":
                viewDeletePermissionInCrewRole(connection,options);
                break;
            case "BACK":
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewAddPermissionToCrewRole(Connection connection, String option)  {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            viewTitle("CHOOSE CREW AND ROLE", textIO);
            Pair<String,String> crewAndRole = getCrewRoleFromList(connection);
            viewTitle("CHOOSE PERMISSION", textIO);
            String permissionAdded = getPermissionFromList(connection);
            CrewRoleDTO crewRole = new CrewRoleDTO(crewAndRole.getSecond(),crewAndRole.getFirst());
            response = CrewController.addPermissionToCrewRole(connection,crewRole,permissionAdded);
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

    public void viewUpdatePermissionInCrewRole(Connection connection, String option)  {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            viewTitle("CHOOSE CREW AND ROLE", textIO);
            Pair<String,String> crewAndRole = getCrewRoleFromList(connection);
            viewTitle("CHOOSE PERMISSION UPDATED", textIO);
            String permissionUpdated = getPermissionByCrewAndRoleFromList(connection,crewAndRole.getFirst(),crewAndRole.getSecond());
            viewTitle("CHOOSE NEW PERMISSION", textIO);
            String newPermission = getPermissionFromList(connection);
            CrewRoleDTO crewRole = new CrewRoleDTO(crewAndRole.getSecond(),crewAndRole.getFirst());
            response = CrewController.updatePermissionInCrewRole(connection,crewRole,permissionUpdated,newPermission);
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
    public void viewDeletePermissionInCrewRole(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            viewTitle("CHOOSE CREW AND ROLE", textIO);
            Pair<String,String> crewAndRole = getCrewRoleFromList(connection);
            viewTitle("CHOOSE PERMISSION DELETED", textIO);
            String permissionDeleted = getPermissionByCrewAndRoleFromList(connection,crewAndRole.getFirst(),crewAndRole.getSecond());
            CrewRoleDTO crewRole = new CrewRoleDTO(crewAndRole.getSecond(),crewAndRole.getFirst());
            response = CrewController.deletePermissionInCrewRole(connection,crewRole,permissionDeleted);
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
    public void viewPermissionByCrewId(Connection connection, String option){
        ResponseDTO<List<String>> response;
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option,textIO);
        Pair<String,String> crewAndRole = getCrewRoleFromList(connection);
        response = CrewController.getAllPermissionByCrewId(connection,crewAndRole.getFirst(),crewAndRole.getSecond());
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
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("VIEW CREW EVENT", "INSERT CREW EVENT","UPDATE CREW EVENT","DELETE CREW EVENT","BACK")
                .read("");
        switch (options){
            case "VIEW CREW EVENT":
                viewListCrewEvent(connection,options);
                break;
            case "INSERT CREW EVENT":
                viewCreateCrewEvent(connection,options);
                break;
            case "UPDATE CREW EVENT":
                viewUpdateCrewEvent(connection,options);
                break;
            case "DELETE CREW EVENT":
                viewDeleteCrewEvent(connection,options);
                break;
            case "BACK":
                view(connection);
                break;
        }
        textIO.dispose();
    }
    public void viewListCrewEvent(Connection connection, String option) {
        ResponseDTO<List<CrewEventDto>> response;
        response = CrewController.getAllCrewEvent(connection);
        TextIO textIO = TextIoFactory.getTextIO();
        viewTitle(option,textIO);
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
    public void viewCreateCrewEvent(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack ;
        do  {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
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
            response = CrewController.addCrewEvent(connection, crewEvent,start,end);
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

    public void viewUpdateCrewEvent(Connection connection, String option) {
        ResponseDTO<Object> response ;
        String continueOrBack ;
        do{
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
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
            response = CrewController.updateCrewEvent(connection, crewEvent,crewEventId,start,end);
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
    public void viewDeleteCrewEvent(Connection connection, String option) {
        ResponseDTO<Object> response;
        String continueOrBack;
        do {
            TextIO textIO = TextIoFactory.getTextIO();
            viewTitle(option, textIO);
            String crew = getCrewFromList(connection);
            int crewEventId = getCrewEventIDFromList(connection);
            response = CrewController.deleteCrewEvent(connection,crewEventId,crew);
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
