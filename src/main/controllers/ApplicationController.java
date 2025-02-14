package controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import constants.ResponseStatus;
import dto.ResponseDTO;
import exceptions.*;
import models.Event;
import models.Permission;
import models.Role;
import models.UserAccount;
import models.UserProfile;
import repositories.GenerationRepository;
import services.ApplicationService;
import services.AuthService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class ApplicationController {

    // Update user profile
    public static ResponseDTO<Object> updateUserProfile(UserProfile data) {
        try {

            ApplicationService.updateUserProfile(data);
            return new ResponseDTO<Object>(ResponseStatus.OK, "Update profile successfully!", data);

        } catch (Exception e) {
            return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    // Get all user profile if role == "President" or role == "Vice president"
    public static ResponseDTO<List<UserProfile>> getAllUserProfiles() {
        try {
            boolean author = AuthService.AppAuthorization("ViewUserInformation");
            if (author == true) {
                List<UserProfile> list = ApplicationService.getAllUserProfiles();
                return new ResponseDTO<List<UserProfile>>(ResponseStatus.OK, "Get all user profiles successfully!",
                        list);
            } else
                return new ResponseDTO<List<UserProfile>>(ResponseStatus.BAD_REQUEST,
                        "Get all user profiles failed!", null);
        } catch (Exception e) {
            return new ResponseDTO<List<UserProfile>>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateUserAccount(String username, String password) {
        try {

            ApplicationService.updateUserAccount(username, password);
            return new ResponseDTO<Object>(ResponseStatus.OK, "Update account successfully", null);

        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (TokenException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteUserAccount(String username) {
        try {
            boolean author = AuthService.AppAuthorization("DeleteUserAccount");
            if (author == true) {
                ApplicationService.deleteUserAccount(username);
                return new ResponseDTO<Object>(ResponseStatus.OK, "Delete account successfully", null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException | TokenException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Boolean> checkToInsertProfile() {
        try {
            Boolean check = ApplicationService.checkToInsertProfile();
            if (check == true)
                return new ResponseDTO<Boolean>(ResponseStatus.OK, "Already insert profile.", check);
            else
                return new ResponseDTO<Boolean>(ResponseStatus.NOT_FOUND, "There is no profile reference this account.",
                        check);
        } catch (IOException | TokenException | JWTVerificationException e) {
            return new ResponseDTO<Boolean>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (SQLException | ClassNotFoundException e) {
            return new ResponseDTO<Boolean>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }

    }

    public static ResponseDTO<Object> createOneUserProfile(UserProfile data) {
        try {

            ApplicationService.insertProfileInternal(data);
            return new ResponseDTO<>(ResponseStatus.OK, "Create user profile successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (UserProfileException | NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (ParseException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, "Invalid date", null);
        }
    }

    public static ResponseDTO<UserProfile> readOneUserProfile(UserProfile data) {
        try {
            ApplicationService.readUserProfileInternal(data);
            return new ResponseDTO<UserProfile>(ResponseStatus.OK, "Read user profile successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<UserProfile>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException e) {
            return new ResponseDTO<UserProfile>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (TokenException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<Role>> getAllRoles() {
        try {
            List<Role> list = ApplicationService.getAllRoles();
            return new ResponseDTO<List<Role>>(ResponseStatus.OK, "Get all roles successfully!", list);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> createRole(String data) {
        try {
            boolean author = AuthService.AppAuthorization("CrudRole");
            if (author == true) {
                ApplicationService.CreateRole(data);
                return new ResponseDTO<>(ResponseStatus.OK, "Create role successfully!", null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to create role",
                        null);
        } catch (SQLException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteRole(int roleId) {
        try {
            boolean author = AuthService.AppAuthorization("CrudRole");
            if (author == true) {
                ApplicationService.DeleteRole(roleId);
                return new ResponseDTO<>(ResponseStatus.OK, "Delete role successfully!", null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to delete role",
                        null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException | TokenException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateRole(int roleId, String newName) {
        try {
            boolean author = AuthService.AppAuthorization("CrudRole");
            if (author == true) {
                ApplicationService.UpdateRole(roleId, newName);
                return new ResponseDTO<>(ResponseStatus.OK, "Update role successfully!", null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to update role",
                        null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException | TokenException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> SetUserRole(String userName, String roleName) {
        try {
            boolean author = AuthService.AppAuthorization("SetUserRole");
            if (author == true) {
                ApplicationService.SetUserRole(userName, roleName);
                return new ResponseDTO<>(ResponseStatus.OK, "Set user role successfully!", null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to set user role",
                        null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException | TokenException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateUserRole(String username, String roleIName) {
        try {
            boolean author = AuthService.AppAuthorization("UpdateUserRole");
            if (author == true) {
                ApplicationService.UpdateUserRoleDto(username, roleIName);
                return new ResponseDTO<>(ResponseStatus.OK, "Update user role successfully!", null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to update role",
                        null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException | TokenException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<Permission>> getAllPermissions() {
        try {
            List<Permission> list = ApplicationService.getAllPermissions();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all permissions successfully!", list);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> createPermission(String name) {
        try {
            boolean author = AuthService.AppAuthorization("CrudPermission");
            if (author == true) {
                ApplicationService.CreatePermissionDto(name);
                return new ResponseDTO<>(ResponseStatus.OK, "Create permission successfully!", null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST,
                        "You dont have permission to create permisison", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException | TokenException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deletePermission(int permissionId) {
        try {
            boolean author = AuthService.AppAuthorization("CrudPermission");
            if (author == true) {
                ApplicationService.DeletePermissionDto(permissionId);
                return new ResponseDTO<>(ResponseStatus.OK, "Delete permission successfully!", null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST,
                        "You dont have permission to delete permission", null);
        } catch (SQLException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<Object>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updatePermission(int permissionId, String newPermission) {
        try {
            boolean author = AuthService.AppAuthorization("CrudPermission");
            if (author == true) {
                ApplicationService.UpdatePermissionDto(permissionId, newPermission);
                return new ResponseDTO<>(ResponseStatus.OK, "Update permission successfully!", null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST,
                        "You dont have permission to update permission", null);
        } catch (SQLException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<Object>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> AddPermissionToRole(int roleId, int permissionId) {
        try {
            boolean author = AuthService.AppAuthorization("AddPermissionToRole");
            if (author == true) {
                ApplicationService.AddPermissionDto(roleId, permissionId);
                return new ResponseDTO<>(ResponseStatus.OK, "Add permission to  role successfully!", null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST,
                        "You dont have permission to add permission to role", null);
        } catch (SQLException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    // TODO: Guild Event
    public static ResponseDTO<Object> addEvent(Event event, String dateStart, String dateEnd) {
        try {
            boolean author = AuthService.AppAuthorization("CrudEvent");
            if (author == true) {
                ApplicationService.insertEvent(event, dateStart, dateEnd);
                return new ResponseDTO<>(ResponseStatus.OK,
                        String.format("Add event %s successfully!", event.getTitle()), null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to create event",
                        null);

        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteEvent(int eventId) {
        try {
            boolean author = AuthService.AppAuthorization("CrudEvent");
            if (author == true) {
                ApplicationService.deleteEvent(eventId);
                return new ResponseDTO<>(ResponseStatus.OK,
                        String.format("Delete event %s successfully!", eventId), null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to delete event",
                        null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | TokenException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateEvent(Event event, int eventId, String dateStart, String dateEnd) {
        try {
            boolean author = AuthService.AppAuthorization("CrudEvent");
            if (author == true) {
                ApplicationService.updateEvent(event, eventId, dateStart, dateEnd);
                return new ResponseDTO<>(ResponseStatus.OK,
                        "Update event successfully!", null);
            } else
                return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to update event",
                        null);

        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<Event>> getAllEvent() {
        try {
            List<Event> data = ApplicationService.getAllEvent();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all event successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found event!", null);
        } catch (DataEmptyException | InvalidDataException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllGeneration() {
        try {
            List<String> data = GenerationRepository.getAllGenerations();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all generation successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found generation!", null);
        }
    }

    public static ResponseDTO<Object> makeNewGeneration() {
        try {
            ApplicationService.makeNewGeneration();
            return new ResponseDTO<Object>(ResponseStatus.OK, "Make new generation successfully.", null);
        } catch (Exception e) {
            return new ResponseDTO<Object>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
