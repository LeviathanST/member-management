package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import exceptions.DataEmptyException;
import exceptions.UserProfileException;
import models.roles.Role;
import models.users.UserAccount;
import constants.ResponseStatus;
import dto.UserProfileDTO;
import dto.LoginDTO;
import dto.ResponseDTO;
import dto.SignUpDTO;
import exceptions.NotFoundException;
import exceptions.TokenException;
import services.ApplicationService;
import services.AuthService;
// TODO:
	// -- User
	// ResponseData<UserAccount> getAllUserAccounts()
	// ResponseData<UserAccount> updateUserAccount(EditUserAccount)
	// + EditUserAccount(accountId, UpdatedInformation data)
	// + UpdatedInformation(String password, Date dob, )
	// -> Idea 1: Taking whole old information as
	// paramater and let user choose a field to update in that param,
	// -> User 2: Let user choose many fields to update, then put all into a arrays
	// and foreach to exec update statement.
	// ResponseData<UserAccount> getUserProfile(String accountId)
	// ResponseData<UserAccount> updateUserProfile(EditUserProfileDto data)
	// + EditUserProfileDto(String accountId, UpdatedInformation data)
	// + UpdatedInformation() -> Idea: Same with UpdateUserAccount

	// TODO:
	// -- Role
	// ResponseData<List<Role>> getAllRoles()
	// ResponseData<<Object>> createRole(CreateRole data)
	// + CreateRole(String name)
	// ResponseData<Object> setUserRole(SetUserRoleDto data)
	// + SetUserRoleDto(String userName, String roleName)
	// ResponseData<Object> updateUserRole(UpdateUserRoleDto data)
	// + UpdateUserRoleDto(String accountId, int roleId)
	// ResponseData<Object> createPermission (CreatePermissionDto data)
	// + CreatePermissionDto(String name)
	// ResponseData<Object> addPermission (AddPermissionDto data)
	// + AddPermissionDto(int roleId, int perrmisionId)
	// ResponseData<Object> UpdatePermission (UpdatePermissionDto data)
	// + UpdatePermissionDto(int roleId, String newName)
	// ResponseData<Object> deletePermission (DeletePermisisonDto)
	// + DeletePermissionDto(int roleId) -> You can list all role id and name
	// before delete to visual

	// TODO:
	// -- Event
	// ResponseData<Object> getAllEvents()
	// ResponseData<Object> createEvent (CreateEventDto data)
	// + CreateEventDto()
	// ResponseData<Object> updateEvent (UpdateEventDto data)
	// + UpdateEventDto()
	// ResponseData<Object> deleteEvent (DeleteEventDto data)


public class ApplicationController {

    	// ResponseData<UserAccount> getAllUserAccounts()
    public static ResponseDTO<UserAccount> getAllUserAccounts(Connection con) {
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public static ResponseDTO<Object> createOneUserProfile(Connection con, UserProfileDTO data, SignUpDTO signUp) {
        try {
			ApplicationService.insertProfileInternal(con, data, signUp);
            return new ResponseDTO<>(ResponseStatus.OK, "Update user profile successfully!", null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		} catch(UserProfileException | NotFoundException e ) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> readOneUserProfile(Connection con, UserProfileDTO data, LoginDTO logIn) {
        try {
            AuthService.AppAuthorization(con, "Edit");
			ApplicationService.readUserProfileInternal(con, data, logIn);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		} catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch(TokenException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<Role>> getAllRoles(Connection connection){
        try {
            ApplicationService.getAllRoles(connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> createRole(String data, Connection connection) {
        try {
            ApplicationService.CreateRole(data,connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> deleteRole(String userName,String roleName, Connection connection) {
        try {
            ApplicationService.UpdateRole(connection,userName,roleName);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updateRole(String roleName, Connection connection) {
        try {
            ApplicationService.DeleteRole(connection,roleName);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> SetUserRole(String userName, String roleName, Connection connection) {
        try {
            ApplicationService.SetUserRole(userName,roleName,connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updateUserRole(String accountId,int roleId, Connection connection) {
        try {
            ApplicationService.UpdateUserRoleDto(accountId,roleId,connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> createPermission(String name,  Connection connection) {
        try {
            ApplicationService.CreatePermissionDto(name,connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> deletePermission(int roleId,String name,  Connection connection) {
        try {
            ApplicationService.DeletePermissionDto(roleId,name,connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updatePermission(int roleId,String name,String newName, Connection connection) {
        try {
            ApplicationService.UpdatePermissionDto(roleId,name,newName,connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> AddPermissionToRole(int roleId,int permissionId,Connection connection){
        try {
            ApplicationService.AddPermissionDto(roleId,permissionId,connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
}