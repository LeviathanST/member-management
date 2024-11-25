package services;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Optional;

import dto.*;
import exceptions.*;
import models.Generation;
import models.events.Event;
import models.permissions.Permission;
import models.roles.Role;
import models.users.UserAccount;
import models.users.UserProfile;
import models.users.UserRole;
import java.sql.Date;


public class ApplicationService extends AuthService{
    public static void insertProfileInternal(Connection con, UserProfileDTO data, SignUpDTO signUp, String date)
            throws SQLException, UserProfileException, NotFoundException, ParseException, TokenException, IOException, ClassNotFoundException {
        
        String accountId = UserAccount.getIdByUsername( signUp.getUsername());
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
                errors += "Invalid student code!\n";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            long millis = dateFormat.parse(date).getTime();
            Date sqlDate = new Date(millis);
            data.setDateOfBirth(sqlDate);
        
            if (!isAgeBetween18And22(data.getDateOfBirth())) {
                errors += "Invalid date!\n";
            }
        } catch (ParseException e) {
            errors += "Invalid date!\n";
        }
        
        if(errors != "")
                throw new UserProfileException(errors);
        UserProfile.insert( data);
    }


    public static void readUserProfileInternal(Connection con, UserProfileDTO data)
            throws SQLException, NotFoundException, TokenException, IOException, ClassNotFoundException {
        Path path = (Path)Paths.get("storage.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
        data.setAccountId(accountId);
        UserProfile.read( data);
        
    }


    public static void updateUserProfile(Connection con, UserProfileDTO data)
            throws SQLException, TokenException, NotFoundException, UserProfileException, IOException, ClassNotFoundException {
        Path path = (Path)Paths.get("storage.json");
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
        UserProfile.update( data);
    }
    // TODO: Role
    public static List<Role> getAllRoles(Connection con)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException, ClassNotFoundException {
            List<Role> listRole = Role.getAll();
            return listRole;
    }
    public static void CreateRole(String name,Connection con)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException, ClassNotFoundException {
        try {
            name = normalizedRolePermission(name);
            Role.createRole( name);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException(String.format("Your role is existed: %s", name));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when create role: %s", name));
        }
    }
    public static void UpdateRole(Connection connection,int roleId, String newName)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException, ClassNotFoundException {
        try {
            newName = normalizedRolePermission(newName);
            Role.updateRole( roleId, newName);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException(String.format("Disallow null values %s",newName));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when update role ID: %d", roleId));
        }
    }
    public static void DeleteRole(Connection connection,int roleId)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException, ClassNotFoundException {
        try {
            Role.deleteRole( roleId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException(String.format("Disallow null values %d",roleId));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when delete role id: %d", roleId));
        }
    }
    // TODO: User Role
    public static void SetUserRole(String userName, String roleName, Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException, ClassNotFoundException {
        try {
            String accountId = UserAccount.getIdByUsername( userName);
            roleName = normalizedRolePermission(roleName);
            int roleId = Role.getByName( roleName).getId();
            UserRole.insert( accountId, roleId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException(String.format("Your user role is existed: %s", userName));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when delete guild: %s", userName));
        }
    }

    public static void UpdateUserRoleDto(String username, String rolename, Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException, ClassNotFoundException {
        try {
            String accountId = UserAccount.getIdByUsername( username);
            int roleId = Role.getByName( rolename).getId();
            UserRole.update( accountId, roleId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static void CreatePermissionDto(String name, Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException, ClassNotFoundException {
        try {
            name = normalizedRolePermission(name);
            Permission.insert(name);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static void AddPermissionDto(int roleId, int permissionId, Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException, ClassNotFoundException {
        try {
            Permission.addPermissionToRole(roleId,permissionId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Permission Role already exists");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static void UpdatePermissionDto(String roleName, int permisisonId,int newPermissionId, Connection connection) throws SQLException, NotFoundException, IOException, ClassNotFoundException {
        try {
            int roleId = Role.getByName( roleName).getId();
            Permission.updatePermissionToRole(roleId,permisisonId,newPermissionId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static void DeletePermissionDto(String roleName,int permisisonId, Connection connection) throws SQLException, NotFoundException, IOException, ClassNotFoundException {
        try {
            int roleId = Role.getByName( roleName).getId();
            Permission.deletePermissionRole(permisisonId,roleId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }
    public static List<Permission> getAllPermissions(Connection con) throws SQLException, IOException, ClassNotFoundException {
        List<Permission> list = new ArrayList<>();
        list = Permission.getAllPermission();
        return list;
    }
    public static List<UserAccount> getAllUserAccounts(Connection con) throws SQLException, IOException, ClassNotFoundException {
        List<UserAccount> list = new ArrayList<>();
        list = UserAccount.getAllUserAccounts();
        return list;
    }
    public static void updateUserAccount(Connection con, String username, String password, String email)
            throws SQLException, TokenException, IOException, ClassNotFoundException {
                    
        Path path = (Path)Paths.get("storage.json");
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
        UserAccount.update( username, password, email, accountId);
    }
    public static void deleteUserAccount(Connection con, String username) throws SQLException, NotFoundException, IOException, ClassNotFoundException {
        String accountId = UserAccount.getIdByUsername( username);
        UserAccount.delete( accountId);
    }
    public static List<UserProfileDTO> getAllUserProfiles(Connection con) throws TokenException, SQLException, NotFoundException, IOException, ClassNotFoundException {
        List<UserProfileDTO> list =  new ArrayList<>();
        list = UserProfile.readAll();
        return list;
    }

    // TODO: Event
    public static void insertEvent(Connection con, EventDto eventDto, String dateStart, String dateEnd)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, IOException, ClassNotFoundException {
        try {
            if (eventDto.getTitle().isEmpty()) {
                throw new DataEmptyException("Title is empty");
            } else if (eventDto.getDescription().isEmpty()) {
                throw new DataEmptyException("Description is empty");
            } else if (eventDto.getType().isEmpty()) {
                throw new DataEmptyException("Type is empty");
            } else if (dateStart.isEmpty()) {
                throw new DataEmptyException("Start date is empty");
            } else if (dateEnd.isEmpty()) {
                throw new DataEmptyException("End date is empty");
            }
            int generationId = Generation.getIdByName(eventDto.getGeneration());
            Timestamp start = validTimeStamp(dateStart);
            Timestamp end = validTimeStamp(dateEnd);
            eventDto = new EventDto(eventDto.getTitle(),eventDto.getDescription(),generationId,start,end,eventDto.getType());
            Event.insert( eventDto);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException(String.format("Your event is existed: %s", eventDto.getTitle()));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when create event: %s", eventDto.getTitle()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateEvent(Connection con, EventDto eventDto, int eventId,String dateStart, String dateEnd)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, IOException, ClassNotFoundException {
        try {
            if (eventDto.getTitle().isEmpty()) {
                throw new DataEmptyException("Title is empty");
            } else if (eventDto.getDescription().isEmpty()) {
                throw new DataEmptyException("Description is empty");
            } else if (eventDto.getType().isEmpty()) {
                throw new DataEmptyException("Type is empty");
            } else if (dateStart.isEmpty()) {
                throw new DataEmptyException("Start date is empty");
            } else if (dateEnd.isEmpty()) {
                throw new DataEmptyException("End date is empty");
            }
            int generationId = Generation.getIdByName(eventDto.getGeneration());
            Timestamp start = validTimeStamp(dateStart);
            Timestamp end = validTimeStamp(dateEnd);
            eventDto = new EventDto(eventDto.getTitle(),eventDto.getDescription(),generationId,start,end,eventDto.getType());
            Event.update( eventDto,eventId);
        } catch (SQLIntegrityConstraintViolationException e) {

            throw new SQLException(String.format("Disallow null values %s", eventDto.getTitle()));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when update event: %s", eventDto.getTitle()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteEvent(Connection con, int guildEventId)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, IOException, ClassNotFoundException {
        try {
            Event.delete( guildEventId);
        } catch (SQLIntegrityConstraintViolationException e) {

            throw new SQLException(String.format("Disallow null values id %s", guildEventId));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when delete event: %s", guildEventId));
        }
    }
    public static List<EventDto> getAllEvent(Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, IOException, ClassNotFoundException {
        try {
            List<EventDto> data = Event.getAllEvent();
            for (EventDto eventDto : data) {
                eventDto.setGeneration(eventDto.getGeneration());
            }
            return data;
        } catch (SQLException e) {
            throw new SQLException("Error occurs when view event");
        }
    }

    public static boolean isAgeBetween18And22(Date birthDate) {
        Calendar today = Calendar.getInstance();
        Calendar birthDateCalendar = Calendar.getInstance();
        birthDateCalendar.setTime(birthDate);

        int age = today.get(Calendar.YEAR) - birthDateCalendar.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < birthDateCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age >= 18 && age <= 22;
    }

    public static String normalizedRolePermission(String data) {
        String[] words = data.trim().toLowerCase().split("\\s+");
        StringBuilder normalizedString = new StringBuilder();

        // Ghi hoa chữ cái đầu của mỗi từ và ghép chúng lại
        for (String word : words) {
            if (!word.isEmpty()) {
                normalizedString.append(Character.toUpperCase(word.charAt(0)))
                                .append(word.substring(1));
            }
        }

        return normalizedString.toString();
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
    public static Timestamp validTimeStamp(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date parsedDate = sdf.parse(date);
        return new Timestamp(parsedDate.getTime());
    }

}

