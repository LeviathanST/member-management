package controllers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import exceptions.DataEmptyException;
import exceptions.UserProfileException;
import dto.TokenPairDTO;
import services.TokenService;
import models.permissions.Permission;
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
    // Neu la President hoac Vice president se co the xem het profile cua moi nguoi 
	// ResponseData<UserAccount> updateUserProfile(EditUserProfileDto data) (Chua lam)


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

    //Update user profile
    public static ResponseDTO<Object> updateUserProfile(Connection con, UserProfileDTO data) {
        try {
            boolean author = AuthService.AppAuthorization(con, "EditUserProfile");
            if(author == true) {
                ApplicationService.updateUserProfile(con, data);
                return new ResponseDTO<Object>(ResponseStatus.OK, "Update profile successfully!", data);
            } else return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "Update profile failed!", null);
        } catch (Exception e) {
            return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    //Get all user profile if role == "President" or role == "Vice president"
    public static ResponseDTO<List<UserProfileDTO>> getAllUserProfiles(Connection con) {
        try {
            List<UserProfileDTO> list = ApplicationService.getAllUserProfiles(con);
            return new ResponseDTO<List<UserProfileDTO>>(ResponseStatus.OK, "Get all user profiles successfully!", list);
        } catch (Exception e) {
           return new ResponseDTO<List<UserProfileDTO>>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    	// ResponseData<UserAccount> getAllUserAccounts()
    public static ResponseDTO<List<UserAccount>> getAllUserAccounts(Connection con) {
        try {
            boolean author = AuthService.AppAuthorization(con, "ViewUserInformation");
            if(author == true) {
                List<UserAccount> listAccounts = ApplicationService.getAllUserAccounts(con);
                return new ResponseDTO<List<UserAccount>>(ResponseStatus.OK, "Get all user accounts successfully!", listAccounts);
            } else return new ResponseDTO<List<UserAccount>>(ResponseStatus.BAD_REQUEST, "Dont have permission!", null);
        } catch (SQLException | TokenException | NotFoundException e) {
            return new ResponseDTO<List<UserAccount>>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateUserAccount(Connection con, String username, String password, String email) {
        try {
            boolean author = AuthService.AppAuthorization(con, "EditUserAccount");
            if(author == true) {
                ApplicationService.updateUserAccount(con, username, password, email);
                return new ResponseDTO<Object>(ResponseStatus.OK, "Update account successfully", null);
            } else return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission!", null);
        } catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		} catch(NotFoundException | TokenException e ) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> createOneUserProfile(Connection con, UserProfileDTO data, SignUpDTO signUp) {
        try {
			
			ApplicationService.insertProfileInternal(con, data, signUp);
            return new ResponseDTO<>(ResponseStatus.OK, "Create user profile successfully!", null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		} catch(UserProfileException | NotFoundException e ) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } 
    }

    public static ResponseDTO<Object> readOneUserProfile(Connection con, UserProfileDTO data) {
        try {
            Path path = (Path)Paths.get("auth.json");
			String accessToken = TokenService.loadFromFile(path).getAccessToken();
			String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
			ApplicationService.readUserProfileInternal(con, data, accountId);
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
            List<Role> list = ApplicationService.getAllRoles(connection).getData();
            return new ResponseDTO<List<Role>>(ResponseStatus.OK, "Get all roles successfully!", list);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> createRole(String data, Connection connection) {
        try {
            boolean author = AuthService.AppAuthorization(connection, "CreateRole");
            if(author == true) {
                ApplicationService.CreateRole(data,connection);
                return new ResponseDTO<>(ResponseStatus.OK, "Create role successfully!", null);
            } else return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to create role", null);
        } catch (SQLException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> deleteRole(String roleName, Connection connection) {
        try {
            boolean author = AuthService.AppAuthorization(connection, "DeleteRole");
            if(author == true) {
                ApplicationService.DeleteRole(connection, roleName);
                return new ResponseDTO<>(ResponseStatus.OK, "Delete role successfully!", null);
            } else return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to delete role", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updateRole(String oldName, String newName, Connection connection) {
        try {
            boolean author = AuthService.AppAuthorization(connection, "UpdateRole");
            if(author == true) {
                ApplicationService.UpdateRole(connection, oldName, newName);
                return new ResponseDTO<>(ResponseStatus.OK, "Update role successfully!", null);
            } else return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to update role", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> SetUserRole(String userName, String roleName, Connection connection) {
        try {
            boolean author = AuthService.AppAuthorization(connection, "SetUserRole");
            if(author == true) {
                ApplicationService.SetUserRole(userName, roleName, connection);
                return new ResponseDTO<>(ResponseStatus.OK, "Set user role successfully!", null);
            } else return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to set user role", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updateUserRole(String username,String roleIName, Connection connection) {
        try {
            boolean author = AuthService.AppAuthorization(connection, "UpdateUserRole");
            if(author == true) {
                ApplicationService.UpdateUserRoleDto(username, roleIName, connection);
                return new ResponseDTO<>(ResponseStatus.OK, "Update user role successfully!", null);
            } else return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to update role", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<Permission>> getAllPermissions(Connection con) {
        try {
            List<Permission> list = ApplicationService.getAllPermissions(con);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all permissions successfully!", list);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } 
    }
    public static ResponseDTO<Object> createPermission(String name,  Connection connection) {
        try {
            boolean author = AuthService.AppAuthorization(connection, "CreatePermission");
            if(author == true) {
                ApplicationService.CreatePermissionDto(name, connection);
                return new ResponseDTO<>(ResponseStatus.OK, "Create permission successfully!", null);
            } else return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to create permisison", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch(NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> deletePermission(String roleName, String name,  Connection connection) {
        try {
            boolean author = AuthService.AppAuthorization(connection, "DeletePermission");
            if(author == true) {
                ApplicationService.DeletePermissionDto(roleName, name, connection);
                return new ResponseDTO<>(ResponseStatus.OK, "Delete permission successfully!", null);
            } else return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to delete permission", null);
        } catch (SQLException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException e) {
            return new ResponseDTO<Object>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updatePermission(String roleName,String name,String newName, Connection connection) {
        try {
            boolean author = AuthService.AppAuthorization(connection, "UpdatePermission");
            if(author == true) {
                ApplicationService.UpdatePermissionDto(roleName, name, newName, connection);
                return new ResponseDTO<>(ResponseStatus.OK, "Update permission successfully!", null);
            } else return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to update permission", null);
        } catch (SQLException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException e) {
            return new ResponseDTO<Object>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> AddPermissionToRole(String roleName, String  permissionName, Connection connection){
        try {
            boolean author = AuthService.AppAuthorization(connection, "AddPermissionToRole");
            if(author == true) {
                ApplicationService.AddPermissionDto(roleName, permissionName, connection);
                return new ResponseDTO<>(ResponseStatus.OK, "Add permission to  role successfully!", null);
            } else return new ResponseDTO<Object>(ResponseStatus.BAD_REQUEST, "You dont have permission to add permission to role", null);
        } catch (SQLException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
}