package views;

import java.sql.Connection;
import java.util.List;

import constants.ApplicationCommand;
import constants.EventCommand;
import constants.GetAllCommand;
import constants.PermissionCommand;
import constants.ProfileCommand;
import constants.ResponseStatus;
import constants.RoleCommand;
import controllers.ApplicationController;
import dto.ResponseDTO;
import models.Permission;
import constants.UserRoleCommand;

public class ApplicationView extends View {

    public ApplicationView(Connection con) {
        super(con);
    }

    public void view() {
        ApplicationCommand option;
        clearScreen();
        do {
            viewTitle("| APPLICATION TAB |", this.textIO);
            option = textIO.newEnumInputReader(ApplicationCommand.class).read("Enter your option : ");
            clearScreen();
            switch (option) {
                case UPDATE_ACCOUNT:
                    UserAccountView updateAccount = new UserAccountView(con);
                    updateAccount.update();
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case DELETE_USER_ACCOUNT:
                    UserAccountView deleteAccount = new UserAccountView(con);
                    deleteAccount.delete();
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case YOUR_PROFILE:
                    profileView(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case GET_ALL_USERS_PROFILES_OR_ACCOUNTS:
                    getAllProfileAccounts(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case CRUD_ROLE:
                    crudRoleView(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case ADD_PERMISSION_TO_ROLE:
                    addPermissionToRoleView(con);
                    waitTimeByMessage("Press enter to continue!");
                    clearScreen();
                    break;
                case CRUD_USER_ROLE:
                    crudUserRole(con);
                    waitTimeByMessage("Press enter to continue");
                    clearScreen();
                    break;
                case PERMISSION_MANAGEMENT:
                    crudPermission(con);
                    waitTimeByMessage("Press enter to continue");
                    clearScreen();
                    break;
                case EVENTS:
                    event(con);
                    waitTimeByMessage("Press enter to continue");
                    clearScreen();
                    break;
                case BACK:
                    AuthView menu = new AuthView(con);
                    menu.appCrewGuildView(con);
                    break;
                default:
                    break;
            }

        } while (option != ApplicationCommand.BACK);
    }

    public void event(Connection con) {
        clearScreen();
        viewTitle("| EVENTS |", textIO);
        EventCommand option;
        do {
            option = textIO.newEnumInputReader(EventCommand.class).read("Enter your choice : ");
            switch (option) {
                case SHOW_ALL_EVENTS:
                    EventView showEvent = new EventView(con);
                    showEvent.show();
                    break;

                case CREATE_EVENT:
                    EventView createEvent = new EventView(con);
                    createEvent.create();
                    break;

                case UPDATE_EVENT:
                    EventView updateEvent = new EventView(con);
                    updateEvent.update();
                    break;

                case DELETE_EVENT:
                    EventView deleteEvent = new EventView(con);
                    deleteEvent.delete();
                    break;
                case BACK:
                    this.view();
                    break;
                default:
                    printError("Invalid value.");
                    break;
            }
        } while (option != EventCommand.BACK);
    }

    public void getAllProfileAccounts(Connection con) {
        clearScreen();
        viewTitle("| GET ALL PROFILES/ ACCOUNTS |", textIO);
        GetAllCommand option;
        do {
            option = textIO.newEnumInputReader(GetAllCommand.class).read("Enter your choice : ");
            switch (option) {
                case GET_ALL_PROFILES:
                    GetAllProfilesAccounts getProfiles = new GetAllProfilesAccounts(con);
                    getProfiles.getAllProfiles();
                    break;
                case GET_ALL_ACCOUNTS:
                    GetAllProfilesAccounts getAccounts = new GetAllProfilesAccounts(con);
                    getAccounts.getAllAccounts();
                    break;
                case BACK:
                    this.view();
                    break;
                default:
                    printError("Invalid value.");
                    break;
            }
        } while (option != GetAllCommand.BACK);
    }

    public void profileView(Connection con) {
        clearScreen();
        viewTitle("| PROFILE |", textIO);
        ProfileCommand option;
        clearScreen();
        do {
            option = textIO.newEnumInputReader(ProfileCommand.class).read("Enter your choice : ");
            switch (option) {
                case SHOW_PROFILE:
                    viewTitle("| YOUR PROFILE |", textIO);
                    UserProfileView show = new UserProfileView(con);
                    show.showProfile();
                    break;
                case UPDATE_PROFILE:
                    viewTitle("| UPDATE YOUR PROFILE |", textIO);
                    UserProfileView updateView = new UserProfileView(con);
                    updateView.updateProfile();
                    break;
                case BACK:
                    view();
                    break;
                default:
                    printError("Invalid value.");
                    break;
            }
        } while (option != ProfileCommand.BACK);
    }

    public void crudRoleView(Connection con) {
        clearScreen();
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, con);
        RoleCommand option;
        do {
            option = textIO.newEnumInputReader(RoleCommand.class).read("Enter your choice : ");
            switch (option) {
                case CREATE_ROLE:
                    RoleView createRole = new RoleView(con);
                    createRole.create(response);
                    break;

                case GET_ALL_ROLES:
                    RoleView getAll = new RoleView(con);
                    getAll.getAllRoles();
                    break;

                case UPDATE_ROLE:
                    RoleView updateRole = new RoleView(con);
                    updateRole.update(response);
                    break;

                case DELETE_ROLE:
                    RoleView deleteRole = new RoleView(con);
                    deleteRole.delete(response);
                    break;
                case BACK:
                    this.view();
                    break;
                default:
                    printError("Invalid value");
                    break;
            }
        } while (option != RoleCommand.BACK);
    }

    public void addPermissionToRoleView(Connection con) {
        clearScreen();
        viewTitle("| ADD PERMISSION TO ROLE |", this.textIO);
        viewTitle("ROLES", textIO);
        RoleView getAll = new RoleView(con);
        getAll.getAllRoles();
        int roleId = textIO.newIntInputReader().read("Enter role's id : ");
        List<Permission> list = ApplicationController.getAllPermissions().getData();
        for (Permission i : list)
            textIO.getTextTerminal().println(i.getId() + " : " + i.getName());
        int permissionId = textIO.newIntInputReader().read("Enter permission's id to add : ");
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, null);
        response = ApplicationController.AddPermissionToRole(roleId, permissionId);
        if (response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else
            textIO.getTextTerminal().println(response.getMessage());
    }

    public void crudUserRole(Connection con) {
        clearScreen();
        viewTitle("| SET USER ROLE | UPDATE USER ROLE |", textIO);
        String username;
        String roleName;
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, null);
        UserRoleCommand option;
        do {
            option = textIO.newEnumInputReader(UserRoleCommand.class).read("Enter your choice :");
            switch (option) {
                case SET_USER_ROLE:
                    viewTitle("| SET USER ROLE |", textIO);
                    username = textIO.newStringInputReader().read("Enter user name : ");
                    roleName = textIO.newStringInputReader().read("Enter role name : ");
                    response = ApplicationController.SetUserRole(username, roleName);
                    if (response.getStatus() != ResponseStatus.OK) {
                        printError(response.getMessage());
                    } else
                        textIO.getTextTerminal().println(response.getMessage());
                    break;

                case UPDATE_USER_ROLE:
                    viewTitle("| UPDATE USER ROLE |", textIO);
                    username = textIO.newStringInputReader().read("Enter user name to updateb :");
                    roleName = textIO.newStringInputReader().read("Enter new role name : ");
                    response = ApplicationController.updateUserRole(username, roleName);
                    if (response.getStatus() != ResponseStatus.OK) {
                        printError(response.getMessage());
                    } else
                        textIO.getTextTerminal().println(response.getMessage());
                    break;

                case BACK:
                    this.view();
                    break;
                default:
                    printError("Invalid value");
                    break;
            }
        } while (option != UserRoleCommand.BACK);
    }

    public void crudPermission(Connection con) {
        clearScreen();
        PermissionView permissionView = new PermissionView(con);
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, null);
        viewTitle("| CREATE PERMISSION | GET ALL PERMISSIONS |UPDATE PERMISSION | DELETE PERMISSION |", textIO);
        PermissionCommand option;
        do {
            option = textIO.newEnumInputReader(PermissionCommand.class).read("Enter your choice : ");
            switch (option) {
                case CREATE_PERMISSION:
                    permissionView.create(response);
                    break;
                case GET_ALL_PERMISSION:
                    permissionView.getAllPermission();
                    break;
                case UPDATE_PERMISSION:
                    permissionView.update(response);
                    break;
                case DELETE_PERMISSION:
                    permissionView.delete(response);
                    break;
                case BACK:
                    this.view();
                    break;
                default:
                    break;
            }
        } while (option != PermissionCommand.BACK);

    }

}
