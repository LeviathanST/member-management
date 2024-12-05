package services;

import dto.*;
import exceptions.*;
import models.Crew;
import models.events.CrewEvent;
import models.permissions.CrewPermission;
import models.roles.CrewRole;
import models.users.UserAccount;
import models.users.UserCrewRole;
import models.users.UserRole;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CrewService {

	// TODO: CRUD Crew
	public static void create(CrewDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			if (data.getName().isEmpty()) {
				throw new DataEmptyException("Crew Name is empty");
			}
			if (!isValidNameCrew(data.getName())) {
				throw new InvalidDataException("Invalid crew name");
			}
			String crew = normalizeNameDeleteSpace(data.getName());
			boolean checkPermissions = false;
			checkPermissions = AuthService.AppAuthorization("CRUDCrew");
			if (checkPermissions) {
				Crew.insert( crew);
			} else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Your crew name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create crew: %s" + e, data.getName()));
		}
    }

	public static void update(CrewDTO data, CrewDTO newData)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			if (newData.getName().isEmpty() ) {
				throw new DataEmptyException("Guild Name is empty");
			}
			if (!isValidNameCrew(newData.getName())) {
				throw new InvalidDataException("Invalid guild name");
			}
			String guild = normalizeNameDeleteSpace(data.getName());
			newData = new CrewDTO(Crew.getIdByName(data.getName()), guild);
			if (AuthService.AppAuthorization("CRUDCrew")) {
				Crew.update( newData.getId(), newData.getName());
			}
			else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update crew: %s", data.getName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }

	public static void delete(CrewDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			data = new CrewDTO(Crew.getIdByName(data.getName()), data.getName());
			if (AuthService.AppAuthorization("CRUDCrew")) {
				Crew.delete( data.getId());
			}
			else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete crew: %s", data.getName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }
	public static List<String> getMemberInCrew( String crew)
            throws SQLException, NotFoundException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			int crewId = Crew.getIdByName( crew);
			List<String> listUsername = new ArrayList<>();
			if (checkPermission("ViewCrew",crew)) {
				listUsername = Crew.getMemberInCrew( crewId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
			return listUsername;
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", crew));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", crew));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
		}
	}

	// TODO: CRUD Crew Role
	public static void insertCrewRole(CrewRoleDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			if (data.getRole().isEmpty()) {
				throw new DataEmptyException("Role is empty");
			}
			if (!isValidString(data.getRole()) ) {
				throw new InvalidDataException("Invalid role");
			}
			String role = normalizeName(data.getRole());
			data.setCrewId(Crew.getIdByName(data.getCrewName()));
			if (checkPermission("CRUDCrewRole",data.getCrewName())) {
				CrewRole.insertCrewRole(role, data.getCrewId());
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Your crew role is existed: %s", data.getCrewName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create crew role: %s", data.getCrewName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }

	public static void updateCrewRole(CrewRoleDTO data, CrewRoleDTO newData)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			if (newData.getRole().isEmpty()) {
				throw new DataEmptyException("New Role is empty");
			}
			if (!isValidString(newData.getRole()) ) {
				throw new InvalidDataException("Invalid role");
			}
			String newRole  = normalizeName(newData.getRole());

			newData.setCrewId(Crew.getIdByName(newData.getCrewName()));
			data.setCrewId(Crew.getIdByName(data.getCrewName()));
			data.setId(CrewRole.getIdByName(data.getCrewId(),data.getRole()));
			if (checkPermission("CRUDCrewRole",data.getCrewName())) {
				CrewRole.updateCrewRole(newRole, data.getId(),newData.getCrewId());
			}
			else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Disallow null values %s or %s", data.getCrewName(),newData.getCrewName()));
		}
		catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update crew role: %s", data.getCrewName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }

	public static void deleteCrewRole(CrewRoleDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			data.setCrewId(Crew.getIdByName(data.getCrewName()));
			data.setId(CrewRole.getIdByName(data.getCrewId(),data.getRole()));
			if (checkPermission("CRUDCrewRole",data.getCrewName())) {
				CrewRole.deleteCrewRole(data.getId());
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Disallow null values %s ", data.getCrewName()));
		}
		catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete crew role: %s", data.getCrewName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }

	// TODO: CRUD User Crew Role
	public static void addUserToCrew(UserCrewRoleDto data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {

			if (data.getUsername().isEmpty()) {
				throw new DataEmptyException("Username is empty");
			}
			if (!isValidString(data.getUsername())) {
				throw new InvalidDataException("Invalid username");
			}

			String usr = normalizeName(data.getUsername());

			int crewId = Crew.getIdByName(data.getCrew());
			data.setCrewRoleId(CrewRole.getIdByName(crewId,data.getRole()));
			data.setAccountId( UserAccount.getIdByUsername(usr));
			if (checkPermission("CRUDUserCrewRole",data.getCrew())){
				UserCrewRole.insertCrewMember( data.getAccountId(), data.getCrewRoleId());

			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("User Crew Role is existed: %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add user to crew: %s", data.getUsername()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }
	public static void updateUserToCrew(UserCrewRoleDto data, UserCrewRoleDto newData)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			if (newData.getUsername().isEmpty()) {
				throw new DataEmptyException("Username is empty");
			}
			if (!isValidString(newData.getUsername())) {
				throw new InvalidDataException("Invalid username");
			}
			String usr = normalizeName(newData.getUsername());
			int crewId = Crew.getIdByName(data.getCrew());

			newData.setAccountId( UserAccount.getIdByUsername(usr));
			int newCrewId = Crew.getIdByName(newData.getCrew());
			newData.setCrewRoleId(CrewRole.getIdByName(newCrewId,newData.getRole()));
			if (checkPermission("CRUDUserCrewRole",data.getCrew())){
				UserCrewRole.updateCrewMember( newData.getAccountId(), crewId,newData.getCrewRoleId());
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add user to crew: %s", data.getUsername()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }
	public static void deleteUserInCrew(UserCrewRoleDto data)
            throws SQLIntegrityConstraintViolationException, SQLException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			data.setAccountId( UserAccount.getIdByUsername(data.getUsername()));
			int crewId = Crew.getIdByName(data.getCrew());
			int crewRoleId = CrewRole.getIdByName(crewId,data.getRole());

			if (checkPermission("CRUDUserCrewRole",data.getCrew())){
				UserCrewRole.deleteCrewMember(data.getAccountId(),crewRoleId);

			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Disallow null values %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update user in guild: %s" + e, data.getUsername()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }
	public static List<UserCrewRoleDto> getAllUserCrewRolesByCrewID(String crew) throws SQLException, NotFoundException, NullPointerException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			int crewId = Crew.getIdByName(crew);
			List<UserCrewRoleDto> data = new ArrayList<>();
			if (checkPermission("CRUDUserCrewRole",crew)){
				data = UserCrewRole.getAllByCrewId(crew, crewId);

			}else {
				throw new NotHavePermission("You don't have permission");
			}

			if (data.isEmpty()) {
				throw new NullPointerException("Null Data");
			}
			return data;
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values %s", crew));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete user in guild: %s", crew));
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("Null Data");
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }
	// TODO: CRUD Crew Permission
	public static void addCrewPermission(String data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			if (data.isEmpty()) {
				throw new DataEmptyException("Crew Permission is empty");
			}
			if (!isValidString(data)) {
				throw new InvalidDataException("Invalid crew permission");
			}

			normalizeName(data);
			if (checkPermission("CRUDCrewPermission",null)){
				CrewPermission.insert(data);
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Your crew permission is existed: %s", data));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add crew permission: %s", data));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }
	public static void updateCrewPermission(String data, String newData) throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			if (newData.isEmpty() ) {
				throw new DataEmptyException("Crew Permission is empty");
			}
			if (!isValidString(newData)) {
				throw new InvalidDataException("Invalid crew permission");
			}
			newData = normalizeNameDeleteSpace(newData);
			if (checkPermission("CRUDCrewPermission",null)){
				CrewPermission.update( data, newData);
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values crew permission %s" , data));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update crew permission: %s", data));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }

	public static void deleteGuildPermission( String data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			if (checkPermission("CRUDCrewPermission",null)){
				CrewPermission.delete(data);
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values crew permission %s", data));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete crew permission: %s", data));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }

	// TODO: CRUD Permission To Crew Role
	public static void addPermissionToCrewRole(CrewRoleDTO crewRole, String permission)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			int crewId = Crew.getIdByName(crewRole.getCrewName());
			int crewRoleId = CrewRole.getIdByName(crewId,crewRole.getRole());

			int permissionId =  CrewPermission.getIdByName(permission);
			if (checkPermission("CRUDCrewRolePermission",crewRole.getCrewName())){
				CrewPermission.addPermissionToCrewRole(crewRoleId,permissionId );
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Your permission %s crew role is existed: %s", permission,crewRole.getCrewName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add permission to guild role: %s", crewRole.getCrewName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }

	public static void updatePermissionInCrewRole( CrewRoleDTO crewRole, String permission,String newPermission) throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			int crewId = Crew.getIdByName(crewRole.getCrewName());
			int crewRoleId = CrewRole.getIdByName(crewId,crewRole.getRole());

			int permissionId =  CrewPermission.getIdByName(permission);
			int newPermissionId =  CrewPermission.getIdByName(newPermission);
			if (checkPermission("CRUDCrewRolePermission",crewRole.getCrewName())){
				CrewPermission.updatePermissionInCrewRole(newPermissionId,permissionId,crewRoleId );
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Disallow null values permission %s crew role %s",permission,crewRole.getCrewName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update permission crew role: %s",crewRole.getCrewName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }

	public static void deletePermissionInCrewRole(CrewRoleDTO crewRole, String permission)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			int crewId = Crew.getIdByName(crewRole.getCrewName());
			int crewRoleId = CrewRole.getIdByName(crewId,crewRole.getRole());

			int permissionId =  CrewPermission.getIdByName(permission);
			if (checkPermission("CRUDCrewRolePermission",crewRole.getCrewName())){
				CrewPermission.deletePermissionInCrewRole(permissionId,crewRoleId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Disallow null values permission %s crew role %s",permission,crewRole.getCrewName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete permission crew role: %s", crewRole.getCrewName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }
	public static List<CrewPermission> getAllPermissionByAccountId(String crew, String userName)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			if (userName.isEmpty()) {
				throw new DataEmptyException("Username is empty");
			}
			int crewId = Crew.getIdByName(crew);
			String accountId = UserAccount.getIdByUsername(userName);
			List<CrewPermission> data = new ArrayList<>();
			if (checkPermission("ViewCrewRolePermission",crew)){
				data = CrewPermission.getAllByAccountIdAndCrewId(accountId,crewId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}

			return data;
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException("Disallow null values username");
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when view permission crew role: %s", crew));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }
	public static List<String> getAllPermissionByCrewId(String crew, String role)
            throws SQLException, IOException, ClassNotFoundException {
		try {
			int crewId = Crew.getIdByName(crew);

			int crewRoleId = CrewRole.getIdByName(crewId,role);
			List<String> data = CrewPermission.getAllByCrewRoleId(crewRoleId);

			return data;
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException("Disallow null values username");
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when view permission crew role: %s", crew));
		} catch (NullPointerException e) {
            throw new NullPointerException("Null Data");
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
	// TODO Crew Event
	public static void insertCrewEvent(CrewEventDto crewEvent, String dateStart, String dateEnd)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			if (crewEvent.getTitle().isEmpty()) {
				throw new DataEmptyException("Title is empty");
			} else if (crewEvent.getDescription().isEmpty()) {
				throw new DataEmptyException("Description is empty");
			} else if (crewEvent.getType().isEmpty()) {
				throw new DataEmptyException("Type is empty");
			} else if (dateStart.isEmpty()) {
				throw new DataEmptyException("Start date is empty");
			} else if (dateEnd.isEmpty()) {
				throw new DataEmptyException("End date is empty");
			}
			int crewId = Crew.getIdByName(crewEvent.getCrewName());
			Timestamp start = validTimeStamp(dateStart);
			Timestamp end = validTimeStamp(dateEnd);
			crewEvent = new CrewEventDto(crewId,crewEvent.getTitle(),crewEvent.getDescription(),crewEvent.getGenerationId(),start,end,crewEvent.getType());
			if (checkPermission("CRUDCrewEvent",crewEvent.getCrewName())){
				CrewEvent.insert( crewEvent);
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Your crew event is existed: %s" + e, crewEvent.getTitle()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create crew: %s" + e, crewEvent.getTitle()));
		} catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }

	public static void updateCrewEvent(CrewEventDto crewEvent, int crewEventId, String dateStart, String dateEnd)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			if (crewEvent.getTitle().isEmpty()) {
				throw new DataEmptyException("Title is empty");
			} else if (crewEvent.getDescription().isEmpty()) {
				throw new DataEmptyException("Description is empty");
			} else if (crewEvent.getType().isEmpty()) {
				throw new DataEmptyException("Type is empty");
			} else if (dateStart.isEmpty()) {
				throw new DataEmptyException("Start date is empty");
			} else if (dateEnd.isEmpty()) {
				throw new DataEmptyException("End date is empty");
			}
			int crewId = Crew.getIdByName(crewEvent.getCrewName());
			Timestamp start = validTimeStamp(dateStart);
			Timestamp end = validTimeStamp(dateEnd);
			crewEvent = new CrewEventDto(crewId,crewEvent.getTitle(),crewEvent.getDescription(),crewEvent.getGenerationId(),start,end,crewEvent.getType());
			if (checkPermission("CRUDCrewEvent",crewEvent.getCrewName())){
				CrewEvent.update( crewEvent,crewEventId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values %s", crewEvent.getTitle()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update crew event: %s", crewEvent.getTitle()));
		} catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }

	public static void deleteCrewEvent(int crewEventId, String crew)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission, IOException, ClassNotFoundException {
		try {
			if (checkPermission("CRUDCrewEvent",crew)){
				CrewEvent.delete( crewEventId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", crewEventId));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete crew event: %s", crewEventId));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
        }
    }
	public static List<CrewEventDto> getAllEvent()
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, IOException, ClassNotFoundException {
		try {
			List<CrewEventDto> data = CrewEvent.getAllCrewEvent();
			for (CrewEventDto crewEventDto : data) {
				crewEventDto.setCrewName(crewEventDto.getCrewName());
				crewEventDto.setGeneration(crewEventDto.getGeneration());
			}
			return data;
		} catch (SQLException e) {
			throw new SQLException("Error occurs when view crew event");
		}
	}


	public static boolean isValidString(String input){
		return input.matches("([A-Za-z]+\\s*)+");
	}

	public static boolean isValidNameCrew(String input){
		return input.matches("([A-Za-z0-9]+\\s*)+");
	}

	public static String normalizeName(String input) {
		String[] parts = input.toLowerCase().split(" ");
		StringBuilder normalized = new StringBuilder();
		if (parts.length > 1) {

			for (String part : parts) {
				if (!part.isEmpty()) {
					normalized.append(Character.toUpperCase(part.charAt(0)))
							.append(part.substring(1))
							.append(" ");
				}
			}
		} else {
			return normalized.append(Character.toUpperCase(input.charAt(0)))
					.append(input.substring(1)).toString();
		}
		return String.join(" ", normalized);
	}
	public static String normalizeNameDeleteSpace(String input) {
		String[] parts = input.toLowerCase().split(" ");
		StringBuilder normalized = new StringBuilder();
		for (String part : parts) {
			if (!part.isEmpty()) {
				normalized.append(Character.toUpperCase(part.charAt(0)))
						.append(part.substring(1));
			}
		}
		return normalized.toString().trim();
	}
	public static Timestamp validTimeStamp(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date parsedDate = sdf.parse(date);
        return new Timestamp(parsedDate.getTime());
	}
	public static String getAccountIDUser()

			throws SQLException, NotFoundException, TokenException, IOException {
		Path path = (Path) Paths.get("storage.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
		return accountId;
	}
	public static boolean checkPermission(String permission, String crewChose) throws SQLException, TokenException, NotFoundException, IOException, ClassNotFoundException {
		try {
			boolean  check = false;
			List<String> listRole = UserRole.getRoleByAccountId(getAccountIDUser());
			for (String role : listRole) {
				if (role.equals("President")){
					return true;
				}
			}
			check = AuthService.CrewAuthorization(Crew.getIdByName(crewChose),permission);

			return check;
		} catch (SQLException e) {
			throw new SQLException("Error occurs when query");
		} catch (TokenException e) {
			throw new TokenException("You don't have permission");
		}
	}
}
