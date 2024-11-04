package services;


import constants.ResponseStatus;
import dto.*;
import exceptions.DataEmptyException;
import exceptions.InvalidDataException;
import exceptions.NotFoundException;
import exceptions.TokenException;
import models.Generation;
import models.Guild;
import models.events.GuildEvent;
import models.permissions.CrewPermission;
import models.permissions.GuildPermission;
import models.roles.GuildRole;
import models.users.UserAccount;
import models.users.UserGuildRole;
import models.users.UserProfile;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GuildService {

	// TODO: CRUD Guild
	public static void create(Connection con, GuildDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			boolean checkPermissions = false;
			if (data.getName().isEmpty()) {
				throw new DataEmptyException("Guild Name is empty");
			}
			if (!isValidString(data.getName())) {
				throw new InvalidDataException("Invalid guild name");
			}
			checkPermissions = checkPermission(con, "CRUDGuild");
			if (checkPermissions) {
				data.setName(normalizeName(data.getName()));
				Guild.insert(con, data.getName());
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Your guild name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild: %s", data.getName()));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }

	public static void update(Connection con, GuildDTO data, GuildDTO newData)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			if (newData.getName().isEmpty() ) {
				throw new DataEmptyException("Guild Name is empty");
			}
			if (!isValidString(newData.getName())) {
				throw new InvalidDataException("Invalid guild name");
			}
			data.setName(normalizeName(data.getName()));
			newData.setName(normalizeName(newData.getName()));
			if (data.getName().equals(newData.getName())) {
				throw new SQLException("User Input Name Existed");
			}
			newData = new GuildDTO(Guild.getIdByName(con,data.getName()), newData.getName());

			if (checkPermission(con,"CRUDGuild")) {
				Guild.update(con, newData.getId(), newData.getName());
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Your name is exist: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild: %s", data.getName()));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }

	public static void delete(Connection con, GuildDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			data = new GuildDTO(Guild.getIdByName(con,data.getName()), data.getName());
			if (checkPermission(con,"CRUDGuild")) {
				Guild.delete(con, data.getId());
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", data.getName()));
		} catch (TokenException e) {
			throw new TokenException("Fail To Get Token");
        }
    }
	public static List<String> getMemberInGuild(Connection con, String guild)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			int guildId = Guild.getIdByName(con, guild);
			List<String> listUsername = new ArrayList<>();
			if (checkPermission(con,"ViewGuild")) {
				listUsername = Guild.getMemberInGuild(con, guildId);
			}
			return listUsername;
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", guild));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", guild));
		} catch (TokenException e) {
			throw new TokenException("Fail To Get Token");
		}
	}
	public static UserProfileDTO getUserProfile(Connection con, String username)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			String accountId = UserAccount.getIdByUsername(con, username);
			UserProfileDTO userProfile = new UserProfileDTO();
			userProfile.setAccountId(accountId);
			if (checkPermission(con,"ViewGuild")) {
				UserProfile.read(con,userProfile);
			}
			userProfile.setGenerationName(Generation.getNameById(con,userProfile.getGenerationId()));
			return userProfile;
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", username));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", username));
		} catch (TokenException e) {
			throw new TokenException("Fail To Get Token");
		}
	}

	// TODO: CRUD Guild Role
	public static void insertGuildRole(Connection con, GuildRoleDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			if (data.getGuildName().isEmpty()) {
				throw new DataEmptyException("Guild Name is empty");
			} else if (data.getRole().isEmpty()) {
				throw new DataEmptyException("Role is empty");
			}
			if (!isValidString(data.getRole()) ) {
				throw new InvalidDataException("Invalid role");
			} else if (!isValidString(data.getGuildName())) {
				throw new InvalidDataException("Invalid guild name");
			}
			data.setRole(normalizeName(data.getRole()));
			data.setGuildId(Guild.getIdByName(con,data.getGuildName()));
			if (checkPermission(con,"CRUDGuildRole")) {
				GuildRole.insertGuildRole(con,data.getRole(), data.getGuildId());
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Your guild name is existed: %s", data.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild role: %s", data.getGuildName()));
		} catch (TokenException e) {
			throw new TokenException("Fail To Get Token");
        }
    }

	public static void updateGuildRole(Connection con, GuildRoleDTO data, GuildRoleDTO newData)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			if (newData.getRole().isEmpty()) {
				throw new DataEmptyException("New Role is empty");
			}
			if (!isValidString(newData.getRole()) ) {
				throw new InvalidDataException("Invalid role");
			}

			newData.setRole(normalizeName(newData.getRole()));

			data.setGuildId(Guild.getIdByName(con,data.getGuildName()));
			data.setId(GuildRole.getIdByName(con,data.getGuildId(),data.getRole()));
			if (checkPermission(con,"CRUDGuildRole")){
				GuildRole.updateGuildRole(con,newData.getRole(), data.getId());
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Your role existed: %s", data.getGuildName()));
		}
		catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild role: %s" + e, data.getGuildName()));
		} catch (TokenException e) {
			throw new TokenException("Fail To Get Token");        }
    }

	public static void deleteGuildRole(Connection con, GuildRoleDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			data.setGuildId(Guild.getIdByName(con,data.getGuildName()));
			data.setId(GuildRole.getIdByName(con,data.getGuildId(),data.getRole()));
			if (checkPermission(con,"CRUDGuildRole")){
				GuildRole.deleteGuildRole(con,data.getId());
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Disallow null values %s ", data.getGuildName()));
		}
		catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild role: %s", data.getGuildName()));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }

	// TODO: CRUD User Guild Role
	public static void addUserToGuild(Connection con, UserGuildRoleDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			if (data.getUsername().isEmpty()) {
				throw new DataEmptyException("Username is empty");
			} else if (data.getRole().isEmpty()) {
				throw new DataEmptyException("Role is empty");
			} else if (data.getGuild().isEmpty()) {
				throw new DataEmptyException("Guild is empty");
			} else if (!isValidString(data.getUsername())) {
				throw new InvalidDataException("Invalid username");
			} else if (!isValidString(data.getRole())) {
				throw new InvalidDataException("Invalid role");
			} else if (!isValidString(data.getGuild())) {
				throw new InvalidDataException("Invalid guild");
			}

			data.setUsername(normalizeName(data.getUsername()));
			normalizeName(data.getRole());
			normalizeName(data.getGuild());

			int guildId = Guild.getIdByName(con,data.getGuild());
			data.setGuildRoleId(GuildRole.getIdByName(con,guildId,data.getRole()));
			data.setAccountId( UserAccount.getIdByUsername(con,data.getUsername()));
			if (checkPermission(con,"CRUDUserGuildRole")){
				UserGuildRole.insertGuildMember(con, data.getAccountId(), data.getGuildRoleId());

			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("User Guild Role is existed: %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add user to guild: %s", data.getUsername()));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }
	public static void updateUserInGuild(Connection con, UserGuildRoleDTO data, UserGuildRoleDTO newData)
            throws SQLIntegrityConstraintViolationException, SQLException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			if (newData.getUsername().isEmpty()) {
				throw new DataEmptyException("Username is empty");
			} else if (newData.getRole().isEmpty()) {
				throw new DataEmptyException("Role is empty");
			} else if (newData.getGuild().isEmpty()) {
				throw new DataEmptyException("Guild is empty");
			} else if (!isValidString(newData.getUsername())) {
				throw new InvalidDataException("Invalid username");
			} else if (!isValidString(newData.getRole())) {
				throw new InvalidDataException("Invalid role");
			} else if (!isValidString(newData.getGuild())) {
				throw new InvalidDataException("Invalid guild");
			}
			normalizeName(newData.getUsername());
			normalizeName(newData.getRole());
			normalizeName(newData.getGuild());
			int guildId = Guild.getIdByName(con,data.getGuild());

			newData.setAccountId( UserAccount.getIdByUsername(con,newData.getUsername()));
			int newGuildId = Guild.getIdByName(con,newData.getGuild());
			newData.setGuildRoleId(GuildRole.getIdByName(con,newGuildId,newData.getRole()));
			if (checkPermission(con,"CRUDUserGuildRole")){
				UserGuildRole.updateGuildMember(con, newData.getAccountId(), guildId,newData.getGuildRoleId());

			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update user in guild: %s", data.getUsername()));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }
	public static void deleteUserInGuild(Connection con, UserGuildRoleDTO data)
            throws SQLIntegrityConstraintViolationException, SQLException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			data.setAccountId( UserAccount.getIdByUsername(con,data.getUsername()));
			int guildId = Guild.getIdByName(con,data.getGuild());
			if (checkPermission(con,"CRUDUserGuildRole")){
				UserGuildRole.deleteGuildMember(con,data.getAccountId(),guildId);
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete user in guild: %s" + e, data.getUsername()));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }
	public static List<UserGuildRoleDTO> getAllUserGuildRolesByGuildID(Connection connection, String guild) throws SQLException, NotFoundException, NullPointerException, TokenException {
		try {
			int guildId = Guild.getIdByName(connection,guild);
			List<UserGuildRoleDTO> data = new ArrayList<>();
			if (checkPermission(connection,"ViewUserGuildRole")){
				data = UserGuildRole.getAllByGuildId(connection,guild,guildId);
			}
            if (data.isEmpty()) {
				throw new NullPointerException("Null Data");
			}
			return data;
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values %s", guild));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete user in guild: %s", guild));
        } catch (NullPointerException e) {
			throw new NullPointerException("Null Data");
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }

	// TODO: CRUD Guild Permission
	public static void addGuildPermission(Connection con, String data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			if (data.isEmpty()) {
				throw new DataEmptyException("Guild Permission is empty");
			}
			if (!isValidString(data)) {
				throw new InvalidDataException("Invalid guild permission");
			}

			data = normalizePermission(data);

			if (checkPermission(con,"CRUDGuildPermission")){
				GuildPermission.insert(data,con);
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Your guild permission is existed: %s", data));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add guild permission: %s", data));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }
	public static void updateGuildPermission(Connection con, String data, String newData) throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			if (newData.isEmpty() ) {
				throw new DataEmptyException("Guild Permission is empty");
			}
			if (!isValidString(newData)) {
				throw new InvalidDataException("Invalid guild permission");
			}
			data = normalizePermission(data);
			newData = normalizePermission(newData);
			if (checkPermission(con,"CRUDGuildPermission")){
				GuildPermission.update( data, newData,con);
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values guild permission %s", data));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild permission: %s", data));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }

	public static void deleteGuildPermission(Connection con, String data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			if (checkPermission(con,"CRUDGuildPermission")){
				GuildPermission.delete(data,con);
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values guild permission %s", data));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild permission: %s", data));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }
	// TODO: CRUD Permission To Guild Role
	public static void addPermissionToGuildRole(Connection con, GuildRoleDTO guildRole, String permission)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			int guildId = Guild.getIdByName(con,guildRole.getGuildName());
			int guildRoleId = GuildRole.getIdByName(con,guildId,guildRole.getRole());
			int permissionId =  GuildPermission.getIdByName(con,permission);
			if (checkPermission(con,"CRUDGuildRolePermission")){
				GuildPermission.addPermissionToGuildRole(con,guildRoleId,permissionId );
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Your permission %s guild role is existed: %s", permission,guildRole.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add permission to guild role: %s", guildRole.getGuildName()));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }

	public static void updatePermissionInGuildRole(Connection con, GuildRoleDTO guildRole, String permission,String newPermission) throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			int guildId = Guild.getIdByName(con,guildRole.getGuildName());
			int guildRoleId = GuildRole.getIdByName(con,guildId,guildRole.getRole());

			int permissionId =  GuildPermission.getIdByName(con,permission);
			int newPermissionId =  GuildPermission.getIdByName(con,newPermission);
			if (checkPermission(con,"CRUDGuildRolePermission")){
				GuildPermission.updatePermissionInGuildRole(newPermissionId,permissionId,guildRoleId,con );
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values permission %s guild role %s",permission,guildRole.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update permission guild role: %s", guildRole.getGuildName()));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }

	public static void deletePermissionInGuildRole(Connection con, GuildRoleDTO guildRole, String permission)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			int guildId = Guild.getIdByName(con,guildRole.getGuildName());
			int guildRoleId = GuildRole.getIdByName(con,guildId,guildRole.getRole());

			int permissionId =  GuildPermission.getIdByName(con,permission);
			if (checkPermission(con,"CRUDGuildRolePermission")){
				GuildPermission.deletePermissionInGuildRole(permissionId,guildRoleId, con);
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values permission %s guild role %s",permission,guildRole.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update permission guild role: %s", guildRole.getGuildName()));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }
	public static List<GuildPermission> getAllPermissionByAccountId(Connection connection, String guild, String userName)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			if (userName.isEmpty()) {
				throw new DataEmptyException("Username is empty");
			}
			int guildId = Guild.getIdByName(connection,guild);
			String accountId = UserAccount.getIdByUsername(connection,userName);
			List<GuildPermission> data = new ArrayList<>();
			if (checkPermission(connection,"ViewGuildRolePermission")){
				data = GuildPermission.getAllByAccountIdAndGuildId(connection,accountId,guildId);
			}
			return data;
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException("Disallow null values username");
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when view permission crew role: %s", guild));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }
	// TODO Crew Event
	public static void insertGuildEvent(Connection con, GuildEventDto guildEvent, String dateStart, String dateEnd)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			if (guildEvent.getTitle().isEmpty()) {
				throw new DataEmptyException("Title is empty");
			} else if (guildEvent.getDescription().isEmpty()) {
				throw new DataEmptyException("Description is empty");
			} else if (guildEvent.getType().isEmpty()) {
				throw new DataEmptyException("Type is empty");
			} else if (dateStart.isEmpty()) {
				throw new DataEmptyException("Start date is empty");
			} else if (dateEnd.isEmpty()) {
				throw new DataEmptyException("End date is empty");
			}
			int guildId = Guild.getIdByName(con,guildEvent.getGuildName());
			int generationId = Generation.getIdByName(con,guildEvent.getGeneration());
			Timestamp start = validTimeStamp(dateStart);
			Timestamp end = validTimeStamp(dateEnd);
			guildEvent = new GuildEventDto(guildId,guildEvent.getTitle(),guildEvent.getDescription(),generationId,start,end,guildEvent.getType());
			if (checkPermission(con,"CRUDGuildEvent")){
				GuildEvent.insert(con, guildEvent);
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Your guild event is existed: %s", guildEvent.getTitle()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild event: %s", guildEvent.getTitle()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }

	public static void updateGuildEvent(Connection con, GuildEventDto guildEvent, int guildEventId, String dateStart, String dateEnd)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			if (guildEvent.getTitle().isEmpty()) {
				throw new DataEmptyException("Title is empty");
			} else if (guildEvent.getDescription().isEmpty()) {
				throw new DataEmptyException("Description is empty");
			} else if (guildEvent.getType().isEmpty()) {
				throw new DataEmptyException("Type is empty");
			} else if (dateStart.isEmpty()) {
				throw new DataEmptyException("Start date is empty");
			} else if (dateEnd.isEmpty()) {
				throw new DataEmptyException("End date is empty");
			}
			int guildId = Guild.getIdByName(con,guildEvent.getGuildName());
			int generationId = Generation.getIdByName(con,guildEvent.getGeneration());
			Timestamp start = validTimeStamp(dateStart);
			Timestamp end = validTimeStamp(dateEnd);
			guildEvent = new GuildEventDto(guildId,guildEvent.getTitle(),guildEvent.getDescription(),generationId,start,end,guildEvent.getType());
			if (checkPermission(con,"CRUDGuildEvent")){
				GuildEvent.update(con, guildEvent,guildEventId);
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values %s", guildEvent.getTitle()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild event: %s" + e, guildEvent.getTitle()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }

	public static void deleteGuildEvent(Connection con, int guildEventId)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			if (checkPermission(con,"CRUDGuildEvent")){
				GuildEvent.delete(con, guildEventId);
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", guildEventId));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild event: %s", guildEventId));
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }
	public static List<GuildEventDto> getAllEvent(Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			List<GuildEventDto> data = new ArrayList<>();
			if (checkPermission(connection,"CRUDGuildEvent")){
				data = GuildEvent.getAllGuildEvent(connection);
			}
			for (GuildEventDto guildEventDto : data) {
				guildEventDto.setGuildName(guildEventDto.getGuildName());
				guildEventDto.setGeneration(guildEventDto.getGeneration());
			}
			return data;
		} catch (SQLException e) {
			throw new SQLException("Error occurs when view guild event");
		} catch (TokenException e) {
            throw new TokenException("Fail To Get Token");
        }
    }

	public static boolean isValidString(String input){
		return input.matches("([A-Za-z]+\\s*)+");
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
	public static Timestamp validTimeStamp(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date parsedDate = sdf.parse(date);
		return new Timestamp(parsedDate.getTime());
	}
	public static String normalizePermission(String input) {

		String[] parts = input.toLowerCase().split(" ");
		StringBuilder normalized = new StringBuilder();
		if (parts.length > 1) {

			for (String part : parts) {
				if (!part.isEmpty()) {
					normalized.append(Character.toUpperCase(part.charAt(0)))
							.append(part.substring(1));
				}
			}
		} else {
			return normalized.append(Character.toUpperCase(input.charAt(0)))
					.append(input.substring(1)).toString();
		}
		return String.join(" ", normalized);
	}
	public static String getAccountIDUser()
            throws SQLException, NotFoundException, TokenException {
		Path path = (Path) Paths.get("auth.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
		return accountId;
	}
	public static boolean checkPermission(Connection connection, String permission) throws SQLException, TokenException, NotFoundException {
		try {
			boolean  check= false;
			List<String> listGuild =  Guild.getAllGuildByAccountId(connection,getAccountIDUser());
			for (String guild : listGuild) {
				List<GuildPermission> permissionList = GuildPermission.getAllByAccountIdAndGuildId(connection,getAccountIDUser(),Guild.getIdByName(connection,guild));
				for (GuildPermission guildPermission : permissionList) {
					if (guildPermission.getName().equals(permission)){
						check = true;
					}
				}
			}
			return check;
		} catch (SQLException e) {
			throw new SQLException("Error occurs when query");
		} catch (TokenException e) {
			throw new TokenException("Fail Get Token");
		}
	}


}
