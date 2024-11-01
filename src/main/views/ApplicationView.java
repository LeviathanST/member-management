package views;

import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import constants.ResponseStatus;
import controllers.ApplicationController;
import dto.ResponseDTO;
import dto.UserProfileDTO;
import constants.Sex;
import models.permissions.Permission;
import models.roles.Role;
import models.users.UserAccount;


public class ApplicationView extends View{

    public ApplicationView(Connection con) {
        super(con);
    }

    public void view() {
        String option;
        do {
            viewTitle("APPLICATION TAB", this.textIO);
            option = textIO.newStringInputReader()
                .withNumberedPossibleValues("UPDATE ACCOUNT", "READ YOUR PROFILE", "UPDATE YOUR PROFILE", "GET ALL USER PROFILES", "GET ALL USER ACCOUNTS" , "LIST ALL ROLE", "CRUD ROLE", "ADD PERMISSION TO ROLE", "CRUD USER'S ROLE", "PERMISSION MANAGEMENT","BACK")
                .read("");
            clearScreen();
            switch (option){
                case "UPDATE ACCOUNT":
                    updateAccount(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "READ YOUR PROFILE":
                    readProfile(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "UPDATE YOUR PROFILE":
                    updateProfile(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "GET ALL USER PROFILES":
                    getAllUserProfiles(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "GET ALL USER ACCOUNTS":
                    getAllAccounts(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "LIST ALL ROLE":
                    listAllRole(con);
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
                    break;
                case "BACK":
                    break;
            }
        } while (!option.equals("Back"));
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
        List<UserProfileDTO> list = new ArrayList<>();
        ResponseDTO<List<UserProfileDTO>> response = ApplicationController.getAllUserProfiles(con);
        list = response.getData();
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            for(UserProfileDTO i : list) {
                textIO.getTextTerminal().printf("UserProfile{full_name='%s', sex=%s, student_code='%s', contact_email='%s', generation_id=%d, date_of_birth=%s}\n",
                i.getFullName(), i.getSex().name(), i.getStudentCode(), i.getContactEmail(), i.getGenerationId(), i.getDateOfBirth());
                textIO.getTextTerminal().println();
            }
        }
    }

    public void updateAccount(Connection con) {
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
        UserProfileDTO profile = new UserProfileDTO();
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, null);
        response = ApplicationController.readOneUserProfile(con, profile);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else textIO.getTextTerminal().println(response.getMessage());
        textIO.getTextTerminal().printf("UserProfile{full_name='%s', sex=%s, student_code='%s', contact_email='%s', generation_id=%d, date_of_birth=%s}\n",
         profile.getFullName(), profile.getSex().name(), profile.getStudentCode(), profile.getContactEmail(), profile.getGenerationId(), profile.getDateOfBirth());
    }
    
    public void listAllRole(Connection con) {
        viewTitle("LIST OF ROLE", this.textIO);
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
        String option = textIO.newStringInputReader().withNumberedPossibleValues("CREATE ROLE", "UPDATE ROLE", "DELETE ROLE").read("");
        switch (option) {
            case "CREATE ROLE":

                name = textIO.newStringInputReader().read("Enter role : ");
                response = ApplicationController.createRole(name, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;

            case "GET ALL ROLES":
                List<Role> list = ApplicationController.getAllRoles(con).getData();
                textIO.getTextTerminal().println("There are " + list.size() + " roles.");
                for(Role i : list)
                    textIO.getTextTerminal().println(i.getId() + " : " + i.getName());
                break;
        

            case "UPDATE ROLE":
                
                String oldName = textIO.newStringInputReader().read("Enter role's name to update : ");
                String newName = textIO.newStringInputReader().read("Enter new name to role : ");
                response = ApplicationController.updateRole(oldName, newName, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;

            case "DELETE ROLE":
                name = textIO.newStringInputReader().read("Enter role's name to delete : ");
                response = ApplicationController.deleteRole(name, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;
            default:
                printError("Invalid value");
                break;
        }
    }

    public void addPermissionToRoleView(Connection con) {
        viewTitle("| ADD PERMISSION TO ROLE |", this.textIO);
        String roleName = textIO.newStringInputReader().read("Enter role's name : ");
        String permissionName = textIO.newStringInputReader().read("Enter permission to add : ");
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, null);
        response = ApplicationController.AddPermissionToRole(roleName, permissionName, con);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else textIO.getTextTerminal().println(response.getMessage());
    }

    public void crudUserRole(Connection con) {
        viewTitle("| SET USER ROLE | UPDATE USER ROLE |", textIO);
        String username;
        String roleName;
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, null);
        String option = textIO.newStringInputReader().withNumberedPossibleValues("SET UESR ROLE", "UPDATE UESR ROLE").read("");
        switch (option) {
            case "SET USER ROLE":
                username = textIO.newStringInputReader().read("Enter user name : ");
                roleName = textIO.newStringInputReader().read("Enter role name : ");
                response = ApplicationController.SetUserRole(username, roleName, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;
            

            case "UPDATE USER ROLE":
                username = textIO.newStringInputReader().read("Enter user name to updateb :");
                roleName = textIO.newStringInputReader().read("Enter new role name : ");
                response = ApplicationController.updateUserRole(username, roleName, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
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
                permission = textIO.newStringInputReader().read("Enter permission's name : ");
                response = ApplicationController.createPermission(permission, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;
        

            case "GET ALL PERMISSIONS":
                List<Permission> list = ApplicationController.getAllPermissions(con).getData();
                textIO.getTextTerminal().println("There are " + list.size() + " roles.");
                for(Permission i : list)
                    textIO.getTextTerminal().println(i.getName());
                break;

            case "UPDATE PERMISSION":
                roleName = textIO.newStringInputReader().read("Enter role's name : ");
                permission = textIO.newStringInputReader().read("Enter old permission : ");
                String newPermission = textIO.newStringInputReader().read("Enter new permission : ");
                response = ApplicationController.updatePermission(roleName, permission, newPermission, con);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
                break;
            
            case "DELETE PERMISSION":
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