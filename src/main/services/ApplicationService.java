package services;


import java.sql.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.DataEmptyException;
import exceptions.InvalidDataException;
import models.permissions.Permission;
import models.roles.Role;
import models.users.UserAccount;
import models.users.UserProfile;
import dto.LoginDTO;
import dto.ResponseDTO;
import dto.SignUpDTO;
import dto.UserProfileDTO;
import exceptions.NotFoundException;
import exceptions.UserProfileException;
import constants.ResponseStatus;
import models.users.UserRole;


public class ApplicationService {
    public static void insertProfileInternal(Connection con, UserProfileDTO data, SignUpDTO signUp)  
                throws SQLException, UserProfileException, NotFoundException{
        
        String errors = "";
        String account_id = UserAccount.getIdByUsername(con, signUp.getUsername());
        data.setAccountId(account_id);
        data.setStudentCode((data.getStudentCode().toUpperCase()));
        data.setGenerationId(getMaxGenerationId(con));
        data.setFullName(normalizeFullname(data.getFullName()));
        if (data.getFullName() == null || isValidFullName(data.getFullName()) == false) 
			    errors += "Full name is empty or contains special character!\n";
		if (data.getSex() == null)
                errors += "Sex is null!\n";
        if (data.getStudentCode() == null)
                errors += "Student code is null!\n";
        if (data.getContactEmail() == null)
                errors += "Contact email is null!\n";
        if (isValidEmail(data.getContactEmail()) == false)
                errors += "Invalid email!\n";
        if (isValidStudentCode(data.getStudentCode()) == false || data.getStudentCode() == null)
                errors += "Invalid student code!";
        
        if(errors != "")
                throw new UserProfileException(errors);
        UserProfile.insert(con, data);
    }


    public static ResponseDTO<Object> readUserProfileInternal(Connection con, UserProfileDTO data, LoginDTO logIn) 
                             throws SQLException, NotFoundException{

        String accountId = UserAccount.getIdByUsername(con, logIn.getUsername());
        data.setAccountId(accountId);
        UserProfile.read(con, data);
        try {
            UserProfile.read(con, data);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		} catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    // TODO: Role
    public static ResponseDTO<List<Role>> getAllRoles(Connection con)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
        try {
            List<Role> listRole = Role.getAll(con);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all roles successfully!",listRole);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        }
    }
    public static void CreateRole(String name,Connection con)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException{
        try {
            Role.createRole(con, name);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException(String.format("Your role is existed: %s", name));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when create role: %s", name));
        }
    }
    public static void UpdateRole(Connection connection,String userName, String roleName)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
        try {
            Role.updateRole(connection, userName, roleName);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException(String.format("Disallow null values %s",userName));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when update guild: %s", userName));
        }
    }
    public static void DeleteRole(Connection connection,String nameRole)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
        try {
            Role.deleteRole(connection, nameRole);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException(String.format("Disallow null values %s",nameRole));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when delete guild: %s", nameRole));
        }
    }
    // TODO: User Role
    public static void SetUserRole(String userName, String roleName, Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
        try {
            String accountId = UserAccount.getIdByUsername(connection, userName);
            int roleId = Role.getByName(connection, roleName).getId();
            UserRole.insert(connection, accountId, roleId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException(String.format("Your user role is existed: %s", userName));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when delete guild: %s", userName));
        }
    }

    public static void UpdateUserRoleDto(String accountId, int roleId, Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException{
        try {
            UserRole.update(connection, accountId, roleId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static void CreatePermissionDto(String name, Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
        try {
            Permission.insert(name,connection);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static void AddPermissionDto(int roleId, int permissionId, Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
        try {
            Permission.addPermissionToRole(connection,roleId,permissionId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Permission Role already exists");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static void UpdatePermissionDto(int roleId, String name,String newName, Connection connection) throws SQLException {
        try {
            int permissionId = Permission.getIdByName(connection, name);
            int newPermissionId = Permission.getIdByName(connection, newName);
            Permission.updatePermissionToRole(newPermissionId,permissionId,roleId,connection);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static void DeletePermissionDto(int roleId,String name, Connection connection) throws SQLException {
        try {
            int permissionId = Permission.getIdByName(connection, name);
            Permission.deletePermissionRole(permissionId,roleId,connection);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static int getMaxGenerationId(Connection con) throws SQLException, NotFoundException{
        String query = """
                SELECT MAX(id) AS max_id FROM generation
                """;
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
            throw new NotFoundException("Generation id is not existed!");
        return rs.getInt("max_id");
    }

    public static boolean isValidFullName(String fullName) {

        // Full name has at least 2 
        String[] words = fullName.trim().split("\\s+");
        if (words.length < 2) {
            return false;
        }

        // Full name must not contains special character
        for (String word : words) {
            if (!word.matches("[a-zA-Z]+")) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        
        Pattern pattern = Pattern.compile(emailRegex);
        
        if (email == null) {
            return false;
        }
        
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidStudentCode(String student_code) {
        String regex = "[S][ASE]\\d{6}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(student_code);
        return matcher.matches();
    }


    public static String normalizeFullname(String username) {
        String[] parts = username.toLowerCase().split(" ");
        StringBuilder normalized = new StringBuilder();

        for (String part : parts) {
            if (!part.isEmpty()) {
                normalized.append(Character.toUpperCase(part.charAt(0)))
                          .append(part.substring(1)).append(" ");
            }
        }

        return normalized.toString().trim();
    }


}

