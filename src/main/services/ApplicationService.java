package services;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Optional;
import models.permissions.Permission;
import models.roles.Role;
import models.users.UserAccount;
import models.users.UserProfile;
import dto.ResponseDTO;
import dto.SignUpDTO;
import dto.UserProfileDTO;
import exceptions.NotFoundException;
import exceptions.TokenException;
import exceptions.UserProfileException;
import dto.TokenPairDTO;
import constants.ResponseStatus;
import models.users.UserRole;


public class ApplicationService extends AuthService{
    public static void insertProfileInternal(Connection con, UserProfileDTO data, SignUpDTO signUp)  
                throws SQLException, UserProfileException, NotFoundException{
        
        String errors = "";
        String accountId = UserAccount.getIdByUsername(con, signUp.getUsername());
        data.setAccountId(accountId);
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


    public static void readUserProfileInternal(Connection con, UserProfileDTO data) 
                             throws SQLException, NotFoundException, TokenException{
        Path path = (Path)Paths.get("auth.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
        data.setAccountId(accountId);
        UserProfile.read(con, data);
        
    }

    public static void updateUserProfile(Connection con, UserProfileDTO data) 
                             throws SQLException, TokenException, NotFoundException, UserProfileException {
        Path path = (Path)Paths.get("auth.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
        data.setAccountId(accountId);
        data.setStudentCode((data.getStudentCode().toUpperCase()));
        data.setGenerationId(getMaxGenerationId(con));
        data.setFullName(normalizeFullname(data.getFullName()));
        String errors = "";
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
        UserProfile.update(con, data);
    }
    // TODO: Role
    public static List<Role> getAllRoles(Connection con)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
            List<Role> listRole = Role.getAll(con);
            return listRole;
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
    public static void UpdateRole(Connection connection,String oldName, String newName)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
        try {
            Role.updateRole(connection, oldName, newName);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException(String.format("Disallow null values %s",newName));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when update role: %s", oldName));
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

    public static void UpdateUserRoleDto(String username, String rolename, Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException{
        try {
            String accountId = UserAccount.getIdByUsername(connection, username);
            int roleId = Role.getByName(connection, rolename).getId();
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
    public static void AddPermissionDto(String roleName, String permissionName, Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
        try {
            Role role = Role.getByName(connection, permissionName);
            int permissionId = Permission.getIdByName(connection, permissionName);
            Permission.addPermissionToRole(connection,role.getId(),permissionId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Permission Role already exists");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static void UpdatePermissionDto(String roleName, String name,String newName, Connection connection) throws SQLException, NotFoundException {
        try {
            int roleId = Role.getByName(connection, roleName).getId();
            int permissionId = Permission.getIdByName(connection, name);
            int newPermissionId = Permission.getIdByName(connection, newName);
            Permission.updatePermissionToRole(newPermissionId,permissionId,roleId,connection);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static void DeletePermissionDto(String roleName,String name, Connection connection) throws SQLException, NotFoundException {
        try {
            int permissionId = Permission.getIdByName(connection, name);
            int roleId = Role.getByName(connection, roleName).getId();
            Permission.deletePermissionRole(permissionId,roleId,connection);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static List<Permission> getAllPermissions(Connection con) throws SQLException {
        List<Permission> list = new ArrayList<>();
        list = Permission.getAllPermission(con);
        return list;
    }
    public static List<UserAccount> getAllUserAccounts(Connection con) throws SQLException {
        List<UserAccount> list = new ArrayList<>();
        list = UserAccount.getAllUserAccounts(con);
        return list;
    }
    public static void updateUserAccount(Connection con, String username, String password, String email) 
                throws SQLException, TokenException {
                    
        Path path = (Path)Paths.get("auth.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
        int round = Integer.parseInt(Optional.ofNullable(System.getenv("ROUND_HASHING")).orElse("4"));
        String[] errorsPassword = AuthService.validatePassword(password);
		String errors = "";
		
		if (username == null || username == "" || username.contains(" "))
			errors += "Your username musn't be empty or contains space!\n";

		if (errorsPassword.length != 0) {
			for (String tmp : errorsPassword)
				errors += tmp + "\n";
		}

		if (ApplicationService.isValidEmail(email) == false)
			errors += "Invalid email!";

		if(errors != "")
			throw new SQLException(errors);
        password = AuthService.hashingPassword(password, round);
        UserAccount.update(con, username, password, email, accountId);
    }
    public static void deleteUserAccount(Connection con, String username) throws SQLException, NotFoundException {
        String accountId = UserAccount.getIdByUsername(con, username);
        UserAccount.delete(con, accountId);  
    }
    public static List<UserProfileDTO> getAllUserProfiles(Connection con) throws TokenException, SQLException, NotFoundException {
        List<UserProfileDTO> list =  new ArrayList<>();
        list = UserProfile.readAll(con);
        return list;
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

