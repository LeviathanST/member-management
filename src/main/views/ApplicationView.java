package views;

import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import constants.ResponseStatus;
import controllers.ApplicationController;
import dto.EventDto;
import dto.ResponseDTO;
import dto.SignUpDTO;
import dto.UserProfileDTO;
import constants.Sex;
import models.permissions.Permission;
import models.roles.Role;
import models.users.UserAccount;


public class ApplicationView extends View{

    public ApplicationView(Connection con) {
        super(con);
    }

    public void view( ) {
        String option;
        do {
            viewTitle("| APPLICATION TAB |", this.textIO);
            option = textIO.newStringInputReader()
                .withNumberedPossibleValues("UPDATE ACCOUNT", "DELETE USER ACCOUNT", "YOUR PROFILE", "GET ALL USER PROFILES/ ACCOUNTS", "CRUD ROLE", "ADD PERMISSION TO ROLE", "CRUD USER'S ROLE", "PERMISSION MANAGEMENT", "EVENTS", "BACK")
                .read("");
            clearScreen();
            switch (option){
                case "UPDATE ACCOUNT":
                    updateAccount(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "DELETE USER ACCOUNT":
                    deleteUserAccount(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "YOUR PROFILE":
                    profileView(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "GET ALL USER PROFILES/ ACCOUNTS":
                    getAllProfileAccounts(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "CRUD ROLE":
                    crudRoleView(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "ADD PERMISSION TO ROLE":
                    addPermissionToRoleView(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "CRUD USER'S ROLE":
                    crudUserRole(con);
                    waitTimeByMessage("Press enter to continue");
                    clearScreen();
                    break;
                case "PERMISSION MANAGEMENT":
                    crudPermission(con);
                    waitTimeByMessage("Press enter to continue");
                    clearScreen();
                    break;
                case "EVENTS":
                    event(con);
                    waitTimeByMessage("Press enter to continue");
                    clearScreen();
                    break;
                case "BACK":
                    AuthView menu = new AuthView(con);
                    menu.appCrewGuildView(con);
                    break;
            }
        } while (!option.equals("Back"));
    }

    public void event(Connection con) {
        viewTitle("| EVENTS |", textIO);
        String option = textIO.newStringInputReader().withNumberedPossibleValues("SHOW ALL EVENTS", "CREATE EVENT", "UPDATE EVENT", "DELETE EVENT").read();
        switch (option) {
            case "SHOW ALL EVENTS":
                showAllEvents(con);
                break;
        
            case "CREATE EVENT":
                createEvent(con);
                break;

            case "UPDATE EVENT":
                updateEvent(con);
                break;

            case "DELETE EVENT":
                deleteEvent(con);
                break;
            default:
                printError("Invalid value.");
                break;
        }
    }

    public void createEvent(Connection con) {
        String title = textIO.newStringInputReader().read("Enter title's event : ");
        String description = textIO.newStringInputReader().read("Enter description's event : ");
        String type = textIO.newStringInputReader().read("Enter type's event : ");
        String generation = textIO.newStringInputReader().read("Enter generation : ");
        String start = textIO.newStringInputReader().read("Enter date start (dd/MM/yyyy) : ");
        String end = textIO.newStringInputReader().read("Enter date start (dd/MM/yyyy) : ");
        EventDto event = new EventDto(generation, title, description, type);
        ResponseDTO<Object> response1 = ApplicationController.addEvent(con, event, start, end);
        if(response1.getStatus() != ResponseStatus.OK) {
            printError(response1.getMessage());
        } else textIO.getTextTerminal().println(response1.getMessage());
    }

    public List<EventDto> showAllEvents(Connection con) {
        ResponseDTO<List<EventDto>> response = ApplicationController.getAllEvent(con);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            for(EventDto i : response.getData()) {
                textIO.getTextTerminal().println("ID: " + i.getId());
                textIO.getTextTerminal().println("Title: " + i.getTitle());
                textIO.getTextTerminal().println("Description: " + i.getDescription());
                textIO.getTextTerminal().println("Generatio : " + i.getGeneration());
                textIO.getTextTerminal().println("Start At: " + i.getStartAt());
                textIO.getTextTerminal().println("End At: " + i.getEndAt());
                textIO.getTextTerminal().println("Type: " + i.getType());
                textIO.getTextTerminal().println();
            }
        }
        return response.getData();
    }

    public void updateEvent(Connection con) {
        List<EventDto> list = showAllEvents(con);
        int id = textIO.newIntInputReader().read("Enter id to update : ");
        if(id <= 0 || id > list.size()) {
            printError("Invalid value.");
        } else {
            String title = textIO.newStringInputReader().read("Enter new title's event : ");
            String description = textIO.newStringInputReader().read("Enter new description's event : ");
            String type = textIO.newStringInputReader().read("Enter new type's event : ");
            String generation = textIO.newStringInputReader().read("Enter generation : ");
            String start = textIO.newStringInputReader().read("Enter new date start (dd/MM/yyyy) : ");
            String end = textIO.newStringInputReader().read("Enter new date start (dd/MM/yyyy) : ");
            EventDto event = new EventDto(generation, title, description, type);
            ResponseDTO<Object> response = ApplicationController.updateEvent(con, event, id, start, end);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
            } 
        }
    }

    public void deleteEvent(Connection con) {
        List<EventDto> list = showAllEvents(con);
        int id = textIO.newIntInputReader().read("Enter id to update : ");
        if(id <= 0 || id > list.size()) {
            printError("Invalid value.");
        } else {
            ResponseDTO<Object> response = ApplicationController.deleteEvent(con, id);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
            } 
        }
    }

    public void getAllProfileAccounts(Connection con) {
        viewTitle("| GET ALL PROFILES/ ACCOUNTS |", textIO);
        String option = textIO.newStringInputReader().withNumberedPossibleValues("GET ALL PROFILES", "GET ALL ACCOUNTS").read("");
        switch (option) {
            case "GET ALL PROFILES":
                getAllUserProfiles(con);
                break;
            case "GET ALL ACCOUNTS":
                getAllAccounts(con);
                break;
            default:
                printError("Invalid value.");
                break;
        }
    }

    public void profileView(Connection con) {
        viewTitle("| PROFILE |", textIO);
        String option = textIO.newStringInputReader().withNumberedPossibleValues("SHOW YOUR PROFILE", "UPDATE YOUR PROFILE").read("");
        clearScreen();
        switch (option) {
            case "SHOW YOUR PROFILE":
                viewTitle("| YOUR PROFILE |", textIO);
                readProfile(con);
                break;
            case "UPDATE YOUR PROFILE":
                viewTitle("| UPDATE YOUR PROFILE |", textIO);
                updateProfile(con);
                break;
            default:
                printError("Invalid value.");
                break;
        }
    }
    public void deleteUserAccount(Connection con) {
        viewTitle("| DELETE USER ACCOUNT |", textIO);
        String username = textIO.newStringInputReader().read("Enter username to delete : ");
        ResponseDTO<Object> response = ApplicationController.deleteUserAccount(con, username);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else textIO.getTextTerminal().println(response.getMessage());
    }

    public void updateProfile(Connection con) {
        viewTitle("| UPDATE PROFILE |", textIO);
        UserProfileDTO user_profile = new UserProfileDTO();
        user_profile.setFullName(textIO.newStringInputReader().read("Enter your full name : "));
        user_profile.setSex(textIO.newEnumInputReader(Sex.class).read("Enter your sex : "));
        user_profile.setStudentCode(textIO.newStringInputReader().read("Enter your roll number : "));
        user_profile.setContactEmail(textIO.newStringInputReader().read("Enter your contact email : "));

        String dateStr = textIO.newStringInputReader()
                        .withPattern("^[0-9]{2}-[0-9]{2}-[0-9]{4}$") 
                        .read("Enter your birthdate (dd-MM-yyyy):");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date parsedDate;
        Date sqlDate;
        try {
            parsedDate = sdf.parse(dateStr);
            sqlDate = new java.sql.Date(parsedDate.getTime());
            user_profile.setDateOfBirth(sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ResponseDTO<Object> response = ApplicationController.updateUserProfile(con, user_profile);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else textIO.getTextTerminal().println(response.getMessage());
    }

    public void getAllUserProfiles(Connection con) {
        viewTitle("| GET ALL USER PROFILES |", textIO);
        List<UserProfileDTO> list = new ArrayList<>();
        ResponseDTO<List<UserProfileDTO>> response = ApplicationController.getAllUserProfiles(con);
        list = response.getData();
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            for(UserProfileDTO i : list) {
                textIO.getTextTerminal().println("Account ID: " + i.getAccountId());
                textIO.getTextTerminal().println("Full Name: " + i.getFullName());
                textIO.getTextTerminal().println("Sex: " + i.getSex());
                textIO.getTextTerminal().println("Student Code: " + i.getStudentCode());
                textIO.getTextTerminal().println("Contact Email: " + i.getContactEmail());
                textIO.getTextTerminal().println("Generation ID: " + i.getGenerationId());
                textIO.getTextTerminal().println("Date of Birth: " + i.getDateOfBirth());
            }
        }
    }

    public void updateAccount(Connection con) {
        viewTitle("| UPDATE ACCOUNT |", textIO);
        String username, password, email;
        username = textIO.newStringInputReader().read("Enter new user name : ");
        password = textIO.newStringInputReader().read("Enter new password : ");
        email = textIO.newStringInputReader().read("Enter new email : ");
        ResponseDTO<Object> response = ApplicationController.updateUserAccount(con, username, password, email);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else textIO.getTextTerminal().println(response.getMessage());
    }

    public void getAllAccounts(Connection con) {
        viewTitle("| GET ALL ACCOUNTS |", textIO);
        ResponseDTO<List<UserAccount>> response = ApplicationController.getAllUserAccounts(con);
        List<UserAccount> list = response.getData();
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            for(UserAccount i : list)
                textIO.getTextTerminal().println(i.getUsername());
        }
        
    }

    public void readProfile(Connection con) {
        viewTitle("| PROFILE |", textIO);
        UserProfileDTO profile = new UserProfileDTO();
        ResponseDTO<UserProfileDTO> response = new ResponseDTO<UserProfileDTO>(null, null, null);
        response = ApplicationController.readOneUserProfile(con, profile);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else textIO.getTextTerminal().println(response.getMessage());
        textIO.getTextTerminal().println("Account ID: " + profile.getAccountId());
        textIO.getTextTerminal().println("Full Name: " + profile.getFullName());
        textIO.getTextTerminal().println("Sex: " + profile.getSex());
        textIO.getTextTerminal().println("Student Code: " + profile.getStudentCode());
        textIO.getTextTerminal().println("Contact Email: " + profile.getContactEmail());
        textIO.getTextTerminal().println("Generation ID: " + profile.getGenerationId());
        textIO.getTextTerminal().println("Date of Birth: " + profile.getDateOfBirth());
    }
    
    public void listAllRole(Connection con) {
        viewTitle("| LIST OF ROLE |", this.textIO);
        ResponseDTO<List<Role>> response = ApplicationController.getAllRoles(con);
        List<Role> role = response.getData();
        textIO.getTextTerminal().println("There are " + role.size() + " roles.");
        for(int i = 0; i < role.size(); i++) {
            textIO.getTextTerminal().println(role.get(i).getId() + " : " + role.get(i).getName());
        }
    }

    public void crudRoleView(Connection con) {
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, con);
        String name;
        viewTitle("|CREATE ROLE | GET ALL ROLES | UPDATE ROLE | DELETE ROLE |", textIO);
        String option = textIO.newStringInputReader().withNumberedPossibleValues("GET ALL ROLES", "CREATE ROLE", "UPDATE ROLE", "DELETE ROLE", "BACK").read("");
        switch (option) {
            case "CREATE ROLE":
                viewTitle("| CREATE ROLE |", textIO);
                name = textIO.newStringInputReader().read("Enter role : ");
                response = ApplicationController.createRole(name, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;

            case "GET ALL ROLES":
                viewTitle("| GET ALL ROLES |", textIO);
                List<Role> list = ApplicationController.getAllRoles(con).getData();
                textIO.getTextTerminal().println("There are " + list.size() + " roles.");
                for(Role i : list)
                    textIO.getTextTerminal().println(i.getId() + " : " + i.getName());
                break;
        

            case "UPDATE ROLE":
                viewTitle("| UPDATE ROLE |", textIO);
                String oldName = textIO.newStringInputReader().read("Enter role's name to update : ");
                String newName = textIO.newStringInputReader().read("Enter new name to role : ");
                response = ApplicationController.updateRole(oldName, newName, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;

            case "DELETE ROLE":
                viewTitle("| DELETE ROLE |", textIO);
                name = textIO.newStringInputReader().read("Enter role's name to delete : ");
                response = ApplicationController.deleteRole(name, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;
            case "BACK":
                break;
            default:
                printError("Invalid value");
                break;
        }
    }

    public void addPermissionToRoleView(Connection con) {
        viewTitle("| ADD PERMISSION TO ROLE |", this.textIO);
        viewTitle("ROLES", textIO);
        listAllRole(con);
        int roleId = textIO.newIntInputReader().read("Enter role's id : ");
        List<Permission> list = ApplicationController.getAllPermissions(con).getData();
        for(Permission i : list) 
            textIO.getTextTerminal().println(i.getId() + " : " + i.getName());
        int permissionId = textIO.newIntInputReader().read("Enter permission's id to add : ");
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, null);
        response = ApplicationController.AddPermissionToRole(roleId, permissionId, con);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else textIO.getTextTerminal().println(response.getMessage());
    }

    public void crudUserRole(Connection con) {
        viewTitle("| SET USER ROLE | UPDATE USER ROLE |", textIO);
        String username;
        String roleName;
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, null);
        String option = textIO.newStringInputReader().withNumberedPossibleValues("SET UESR ROLE", "UPDATE UESR ROLE", "BACK").read("");
        switch (option) {
            case "SET USER ROLE":
                viewTitle("| SET USER ROLE |", textIO);
                username = textIO.newStringInputReader().read("Enter user name : ");
                roleName = textIO.newStringInputReader().read("Enter role name : ");
                response = ApplicationController.SetUserRole(username, roleName, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;
            

            case "UPDATE USER ROLE":
                viewTitle("| UPDATE USER ROLE |", textIO);
                username = textIO.newStringInputReader().read("Enter user name to updateb :");
                roleName = textIO.newStringInputReader().read("Enter new role name : ");
                response = ApplicationController.updateUserRole(username, roleName, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;

            case "BACK":
                break;
            default:
                printError("Invalid value");
                break;
        }
    }

    public void crudPermission(Connection con) {
        String permission, roleName;
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, null);
        viewTitle("| CREATE PERMISSION | GET ALL PERMISSIONS |UPDATE PERMISSION | DELETE PERMISSION |", textIO);
        String option = textIO.newStringInputReader().withNumberedPossibleValues("CREATE PERMISSION", "GET ALL PERMISSIONS", "UPDATE PERMISSION", "DELETE PERMISSION").read("");
        switch (option) {
            case "CREATE PERMISSION":
                viewTitle("| CREATE PERMISSION |", textIO);
                permission = textIO.newStringInputReader().read("Enter permission's name : ");
                response = ApplicationController.createPermission(permission, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;
        

            case "GET ALL PERMISSIONS":
                viewTitle("| GET ALL PERMISSIONS |", textIO);
                List<Permission> list = ApplicationController.getAllPermissions(con).getData();
                textIO.getTextTerminal().println("There are " + list.size() + " roles.");
                for(Permission i : list)
                    textIO.getTextTerminal().println(i.getName());
                break;

            case "UPDATE PERMISSION":
                viewTitle("| UPDATE PERMISSION |", textIO);
                roleName = textIO.newStringInputReader().read("Enter role's name : ");
                permission = textIO.newStringInputReader().read("Enter old permission : ");
                String newPermission = textIO.newStringInputReader().read("Enter new permission : ");
                response = ApplicationController.updatePermission(roleName, permission, newPermission, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;
            
            case "DELETE PERMISSION":
                viewTitle("| DELETE PERMISSION |", textIO);
                roleName =textIO.newStringInputReader().read("Enter role : ");
                permission = textIO.newStringInputReader().read("Enter permission to delete : ");
                response = ApplicationController.deletePermission(roleName, permission, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;
            default:
                break;
        }
    }


 




}
