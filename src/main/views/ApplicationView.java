package views;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import constants.ResponseStatus;
import dto.ResponseDTO;
import exceptions.NotFoundException;
import models.permissions.Permission;
import models.roles.Role;
import models.users.UserAccount;
import models.users.UserRole;

public class ApplicationView extends View{

    public ApplicationView(Connection con) {
        super(con);
    }

    public void view() throws SQLException, NotFoundException {
        ResponseDTO<Object> response = null;
        String option;
        do {
            viewTitle("APPLICATION TAB", this.textIO);
            option = textIO.newStringInputReader()
                .withNumberedPossibleValues("List all role", "CRUD role", "Add permission to role", "Update user's role", "Permission management","Back")
                .read("");
            clearScreen();
            switch (option){
                case "List all role":
                    listAllRole(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "CRUD role":
                    response = crudView(con);
                    if(response.getStatus() != ResponseStatus.OK)
                        printError(response.getMessage());
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "Add permission to role":
                    response = addPermissionRoleView(con);
                    if(response.getStatus() != ResponseStatus.OK)
                        printError(response.getMessage());
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "Update user's role":
                    response = updateUserRole(con);
                    if(response.getStatus() != ResponseStatus.OK)
                        printError(response.getMessage());
                    waitTimeByMessage("Press enter to continue");
                    clearScreen();
                    break;
                case "Permission management":
                    crudPermission(con);
                    break;
                case "Back":

                    break;
            }
        } while (!option.equals("Back"));
    }
    
    public void listAllRole(Connection con) throws SQLException {
        viewTitle("LIST OF ROLE", this.textIO);
        List<Role> role = Role.getAll(con);
        textIO.getTextTerminal().println("There are " + role.size() + " roles.");
        for(int i = 0; i < role.size(); i++) {
            textIO.getTextTerminal().println(role.get(i).getId() + " : " + role.get(i).getName());
        }
    }

    public ResponseDTO<Object> crudView(Connection con) {

        String nameRole;
        String option = textIO.newStringInputReader()
                    .withNumberedPossibleValues("Add role", "Edit role", "Delete role").read("");
        try {
            switch (option) {
                case "Add role":
                    nameRole = textIO.newStringInputReader().read("Enter name role to create : ");
                    Role.createRole(con, nameRole);
                    break;
                case "Edit role" :
                    nameRole = textIO.newStringInputReader().read("Enter name role to edit : ");
                    String newNameRole = textIO.newStringInputReader().read("Enter new name role : ");
                    Role.updateRole(con, nameRole, newNameRole);
                    break;
                case "Delete role":
                    nameRole = textIO.newStringInputReader().read("Enter name role to delete : ");
                    Role.deleteRole(con, nameRole);
                    break;
                default:
                    printError("Invalid value");
                    break;
            }
        } catch (SQLException e) {
            return new ResponseDTO<Object>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
        return new ResponseDTO<Object>(ResponseStatus.OK, "CRUD role are successfully!", null);
    }

    public ResponseDTO<Object> addPermissionRoleView(Connection con) throws SQLException {
        String nameRole, namePermission;
        List<Permission> listPermission = Permission.getAllPermission(con);
        viewTitle("LIST OF PERMISSION", textIO);
        for(Permission i : listPermission) 
            textIO.getTextTerminal().println(i.getName());
        List<Role> listRole = Role.getAll(con);
        viewTitle("LIST OF ROLE", textIO);
        for(Role i : listRole) 
            textIO.getTextTerminal().println(i.getName());
        try {
            namePermission = textIO.newStringInputReader().read("Enter permission's name : ");
            nameRole = textIO.newStringInputReader().read("Enter role's name : ");
            Role.addPermissionRole(con, namePermission, nameRole);
        } catch (SQLException e) {
            return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch(NotFoundException e) {
            return new ResponseDTO<Object>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
        return new ResponseDTO<Object>(ResponseStatus.OK, "Add permission to role is successfully!", null);
    }

    public ResponseDTO<Object> updateUserRole(Connection con) throws SQLException, NotFoundException {
        String userName = textIO.newStringInputReader().read("Enter user name to update : ");
        String account_id = UserAccount.getIdByUsername(con, userName);
        String nameRole = textIO.newStringInputReader().read("Enter new role to update :  ");
        Role role = Role.getByName(con, nameRole);
        try {
            UserRole.update(con, account_id, role.getId());
        } catch (SQLException e) {
            return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return new ResponseDTO<Object>(ResponseStatus.OK, "Update user's role successfully", null);
    }

    public ResponseDTO<Object> crudPermission(Connection con) throws SQLException, NotFoundException {
        String namePermission;
        Permission permission = new Permission();
        List<Permission> listPermission = Permission.getAllPermission(con);
        String option = textIO.newStringInputReader()
                    .withNumberedPossibleValues("List of role", "Add permission", "Edit permission", "Delete permission", "Back").read("");
        try {
            switch (option) {
                case "List of role":
                    viewTitle("LIST OF ROLE", textIO);
                    List<Role> listRole = Role.getAll(con);
                    for(Role i : listRole)
                        textIO.getTextTerminal().println(i.getName());
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "Add permission" :
                    namePermission = textIO.newStringInputReader().read("Enter name permission to add : ");
                    permission.insert(namePermission);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "Edit permission":
                    namePermission = textIO.newStringInputReader().read("Enter name role to edit : ");
                    String newName = textIO.newStringInputReader().read("Enter new name : ");
                    Permission tmp = null;
                    for(Permission i : listPermission)
                        if(i.getName().equalsIgnoreCase(namePermission))
                            tmp = i;
                    tmp.update(newName);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "Delete permission":
                    namePermission = textIO.newStringInputReader().read("Enter permission to delete : ");
                    permission.delete(namePermission);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case "Back":
                    // view();
                    break;
                default:
                    printError("Invalid value");
                    break;
            }
        } catch (SQLException e) {
            return new ResponseDTO<Object>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
        return new ResponseDTO<Object>(ResponseStatus.OK, "CRUD role are successfully!", null);
    }





}
