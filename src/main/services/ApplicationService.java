package services;

import config.Database;
import dto.TokenPairDTO;
import exceptions.*;
import models.Event;
import models.Permission;
import models.Role;
import models.UserAccount;
import models.UserProfile;
import repositories.GenerationRepository;
import repositories.events.EventRepository;
import repositories.permissions.PermissionRepository;
import repositories.roles.RoleRepository;
import repositories.users.UserAccountRepository;
import repositories.users.UserProfileRepository;
import repositories.users.UserRoleRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.*;

public class ApplicationService extends AuthService {
    public static void insertProfileInternal(UserProfile data)
            throws SQLException, UserProfileException, NotFoundException, ParseException, TokenException, IOException,
            ClassNotFoundException {

        Path path = (Path) Paths.get("storage.json");
        String accessToken = TokenService.loadFromFile(path).getAccessToken();
        String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
        data.setAccountId(accountId);
        data.setStudentCode((data.getStudentCode().toUpperCase()));
        data.setGenerationId(getMaxGenerationId());
        data.setFullName(normalizeFullname(data.getFullName()));
        if (data.getContactEmail().length() == 0)
            data.setContactEmail(null);

        UserProfileRepository.insert(data);
    }

    public static Boolean checkToInsertProfile()
            throws TokenException, ClassNotFoundException, SQLException, IOException {
        try {
            UserProfile data = new UserProfile();
            readUserProfileInternal(data);
            return true;
        } catch (NotFoundException e) {
            return false;
        }

    }

    public static void readUserProfileInternal(UserProfile data)
            throws SQLException, NotFoundException, TokenException, IOException, ClassNotFoundException {
        Path path = (Path) Paths.get("storage.json");
        String accessToken = TokenService.loadFromFile(path).getAccessToken();
        String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
        data.setAccountId(accountId);
        UserProfileRepository.read(data);
    }

    public static void updateUserProfile(UserProfile data)
            throws SQLException, TokenException, NotFoundException, UserProfileException, IOException,
            ClassNotFoundException {
        Path path = (Path) Paths.get("storage.json");
        String accessToken = TokenService.loadFromFile(path).getAccessToken();
        String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
        data.setAccountId(accountId);
        data.setStudentCode((data.getStudentCode().toUpperCase()));
        data.setGenerationId(getMaxGenerationId());
        data.setFullName(normalizeFullname(data.getFullName()));
        UserProfileRepository.update(data);
    }

    // TODO: Role
    public static List<Role> getAllRoles()
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException,
            ClassNotFoundException {
        List<Role> listRole = RoleRepository.getAll();
        return listRole;
    }

    public static void CreateRole(String name)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException,
            ClassNotFoundException {
        try {
            name = normalizedRolePermission(name);
            RoleRepository.createRole(name);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException(String.format("Your role is existed: %s", name));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when create role: %s", name));
        }
    }

    public static void UpdateRole(int roleId, String newName)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException,
            ClassNotFoundException {
        try {
            newName = normalizedRolePermission(newName);
            RoleRepository.updateRole(roleId, newName);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException(String.format("Disallow null values %s", newName));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when update role ID: %d", roleId));
        }
    }

    public static void DeleteRole(int roleId)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException,
            ClassNotFoundException {
        try {
            RoleRepository.deleteRole(roleId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException(String.format("Disallow null values %d", roleId));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when delete role id: %d", roleId));
        }
    }

    // TODO: User Role
    public static void SetUserRole(String userName, String roleName)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException,
            ClassNotFoundException {
        try {
            String accountId = UserAccountRepository.getIdByUsername(userName);
            roleName = normalizedRolePermission(roleName);
            int roleId = RoleRepository.getByName(roleName).getId();
            UserRoleRepository.insert(accountId, roleId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException(String.format("Your user role is existed: %s", userName));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when delete guild: %s", userName));
        }
    }

    public static void UpdateUserRoleDto(String username, String rolename)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException,
            ClassNotFoundException {
        try {
            String accountId = UserAccountRepository.getIdByUsername(username);
            int roleId = RoleRepository.getByName(rolename).getId();
            UserRoleRepository.update(accountId, roleId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }

    public static void CreatePermissionDto(String name)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException,
            ClassNotFoundException {
        try {
            name = normalizedRolePermission(name);
            PermissionRepository.insert(name);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }

    public static void AddPermissionDto(int roleId, int permissionId)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, IOException,
            ClassNotFoundException {
        try {
            PermissionRepository.addPermissionToRole(roleId, permissionId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Permission Role already exists");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }

    public static void UpdatePermissionDto(int permisisonId, String newPermissionId)
            throws SQLException, NotFoundException, IOException, ClassNotFoundException {
        try {
            PermissionRepository.update(permisisonId, newPermissionId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when update user role");
        }
    }

    public static void DeletePermissionDto(int permisisonId)
            throws SQLException, NotFoundException, IOException, ClassNotFoundException {
        try {
            PermissionRepository.delete(permisisonId);
        } catch (IOException | ClassNotFoundException e) {
            throw new SQLException("Disallow null values");
        } catch (SQLException e) {
            throw new SQLException("Error occurs when delete permission.");
        }
    }

    public static List<Permission> getAllPermissions()
            throws SQLException, IOException, ClassNotFoundException {
        List<Permission> list = new ArrayList<>();
        list = PermissionRepository.getAllPermission();
        return list;
    }

    public static List<UserAccount> getAllUserAccounts()
            throws SQLException, IOException, ClassNotFoundException {
        List<UserAccount> list = new ArrayList<>();
        list = UserAccountRepository.getAllUserAccounts();
        return list;
    }

    public static void updateUserAccount(String username, String password)
            throws SQLException, TokenException, IOException, ClassNotFoundException {

        Path path = (Path) Paths.get("storage.json");
        String accessToken = TokenService.loadFromFile(path).getAccessToken();
        String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
        int round = Integer.parseInt(Optional.ofNullable(System.getenv("ROUND_HASHING")).orElse("4"));
        password = AuthService.hashingPassword(password, round);
        UserAccountRepository.update(username, password, accountId);
    }

    public static void deleteUserAccount(String username)
            throws SQLException, NotFoundException, IOException, ClassNotFoundException {
        String accountId = UserAccountRepository.getIdByUsername(username);
        UserAccountRepository.delete(accountId);
    }

    public static List<UserProfile> getAllUserProfiles()
            throws TokenException, SQLException, NotFoundException, IOException, ClassNotFoundException {
        List<UserProfile> list = new ArrayList<>();
        list = UserProfileRepository.readAll();
        return list;
    }

    // TODO: Event
    public static void insertEvent(Event event, String dateStart, String dateEnd)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException,
            InvalidDataException, IOException, ClassNotFoundException {
        try {
            if (event.getTitle().isEmpty()) {
                throw new DataEmptyException("Title is empty");
            } else if (event.getDescription().isEmpty()) {
                throw new DataEmptyException("Description is empty");
            } else if (event.getType().isEmpty()) {
                throw new DataEmptyException("Type is empty");
            } else if (dateStart.isEmpty()) {
                throw new DataEmptyException("Start date is empty");
            } else if (dateEnd.isEmpty()) {
                throw new DataEmptyException("End date is empty");
            }
            Timestamp start = validTimeStamp(dateStart);
            Timestamp end = validTimeStamp(dateEnd);
            event = new Event(event.getTitle(), event.getDescription(), event.getGenerationId(), start,
                    end, event.getType());
            EventRepository.insert(event);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException(String.format("Your event is existed: %s", event.getTitle()));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when create event: %s", event.getTitle()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateEvent(Event event, int eventId, String dateStart, String dateEnd)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException,
            InvalidDataException, IOException, ClassNotFoundException {
        try {
            if (event.getTitle().isEmpty()) {
                throw new DataEmptyException("Title is empty");
            } else if (event.getDescription().isEmpty()) {
                throw new DataEmptyException("Description is empty");
            } else if (event.getType().isEmpty()) {
                throw new DataEmptyException("Type is empty");
            } else if (dateStart.isEmpty()) {
                throw new DataEmptyException("Start date is empty");
            } else if (dateEnd.isEmpty()) {
                throw new DataEmptyException("End date is empty");
            }
            Timestamp start = validTimeStamp(dateStart);
            Timestamp end = validTimeStamp(dateEnd);
            event = new Event(event.getTitle(), event.getDescription(), event.getGenerationId(), start,
                    end, event.getType());
            EventRepository.update(event, eventId);
        } catch (SQLIntegrityConstraintViolationException e) {

            throw new SQLException(String.format("Disallow null values %s", event.getTitle()));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when update event: %s", event.getTitle()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteEvent(int guildEventId)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException,
            InvalidDataException, IOException, ClassNotFoundException {
        try {
            EventRepository.delete(guildEventId);
        } catch (SQLIntegrityConstraintViolationException e) {

            throw new SQLException(String.format("Disallow null values id %s", guildEventId));
        } catch (SQLException e) {
            throw new SQLException(String.format("Error occurs when delete event: %s", guildEventId));
        }
    }

    public static List<Event> getAllEvent()
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException,
            InvalidDataException, IOException, ClassNotFoundException {
        try {
            List<Event> data = EventRepository.getAllEvent();
            for (Event event : data) {
                event.setGeneration(event.getGeneration());
            }
            return data;
        } catch (SQLException e) {
            throw new SQLException("Error occurs when view event");
        }
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

    public static int getMaxGenerationId() throws SQLException, NotFoundException, IOException, ClassNotFoundException {
        try (Connection con = Database.connection()) {
            String query = """
                    SELECT MAX(id) AS max_id FROM generation
                    """;
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next())
                throw new NotFoundException("Generation id is not existed!");
            return rs.getInt("max_id");
        }
    }

    public static void makeNewGeneration() throws ClassNotFoundException, SQLException, IOException, NotFoundException {
        int currentYear = Year.now().getValue();
        int generation = currentYear - 2002;
        List<Integer> list = GenerationRepository.getAllGeneration();

        for (Integer i : list) {
            if (i == generation) {
                return;
            }
        }

        GenerationRepository.insert(generation);

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
        Date parsedDate = sdf.parse(date);
        return new Timestamp(parsedDate.getTime());
    }

}
