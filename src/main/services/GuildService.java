package services;


import dto.*;
import exceptions.*;
import models.Generation;
import models.Guild;
import models.events.GuildEvent;
import models.permissions.GuildPermission;
import models.roles.GuildRole;
import models.users.UserAccount;
import models.users.UserGuildRole;
import models.users.UserProfile;
import models.users.UserRole;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuildService {

	// TODO: CRUD Guild
	public static void create(GuildDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			boolean checkPermissions = false;
			if (data.getName().isEmpty()) {
				throw new DataEmptyException("Guild Name is empty");
			}
			if (!isValidString(data.getName())) {
				throw new InvalidDataException("Invalid guild name");
			}
			checkPermissions = AuthService.AppAuthorization("CRUDGuild");
			if (checkPermissions) {
				data.setName(normalizeName(data.getName()));
				Guild.insert(data.getName());
			} else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException | IOException | ClassNotFoundException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Your guild name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild: %s", data.getName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void update(GuildDTO data, GuildDTO newData)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
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
			newData = new GuildDTO(Guild.getIdByName(data.getName()), newData.getName());

			if (AuthService.AppAuthorization("CRUDGuild")) {
				Guild.update( newData.getId(), newData.getName());
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Your name is exist: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild: %s", data.getName()));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void delete(GuildDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			data = new GuildDTO(Guild.getIdByName(data.getName()), data.getName());
			if (AuthService.AppAuthorization("CRUDGuild")) {
				Guild.delete(data.getId());
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", data.getName()));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static List<String> getMemberInGuild(String guild)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			int guildId = Guild.getIdByName( guild);
			List<String> listUsername = new ArrayList<>();
			if (checkPermission("ViewGuild",guild)) {
				listUsername = Guild.getMemberInGuild( guildId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
			return listUsername;
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", guild));
		} catch (SQLException | IOException | ClassNotFoundException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", guild));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");		}
	}
	public static UserProfileDTO getUserProfile(String username)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			String accountId = UserAccount.getIdByUsername( username);
			UserProfileDTO userProfile = new UserProfileDTO();
			userProfile.setAccountId(accountId);
			UserProfile.read(userProfile);
			userProfile.setGenerationName(Generation.getNameById(userProfile.getGenerationId()));
			return userProfile;
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", username));
		} catch (SQLException | IOException | ClassNotFoundException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", username));
		}
	}

	// TODO: CRUD Guild Role
	public static void insertGuildRole(GuildRoleDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
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
			data.setGuildId(Guild.getIdByName(data.getGuildName()));
			if (checkPermission("CRUDGuildRole",data.getGuildName())) {
				GuildRole.insertGuildRole(data.getRole(), data.getGuildId());
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Your guild name is existed: %s", data.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild role: %s", data.getGuildName()));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void updateGuildRole(GuildRoleDTO data, GuildRoleDTO newData)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			if (newData.getRole().isEmpty()) {
				throw new DataEmptyException("New Role is empty");
			}
			if (!isValidString(newData.getRole()) ) {
				throw new InvalidDataException("Invalid role");
			}

			newData.setRole(normalizeName(newData.getRole()));

			data.setGuildId(Guild.getIdByName(data.getGuildName()));
			data.setId(GuildRole.getIdByName(data.getGuildId(),data.getRole()));
			if (checkPermission("CRUDGuildRole",data.getGuildName())){
				GuildRole.updateGuildRole(newData.getRole(), data.getId());
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Your role existed: %s", data.getGuildName()));
		}
		catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild role: %s" + e, data.getGuildName()));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");		}
    }

	public static void deleteGuildRole(GuildRoleDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			data.setGuildId(Guild.getIdByName(data.getGuildName()));
			data.setId(GuildRole.getIdByName(data.getGuildId(),data.getRole()));
			if (checkPermission("CRUDGuildRole",data.getGuildName())){
				GuildRole.deleteGuildRole(data.getId());
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Disallow null values %s ", data.getGuildName()));
		}
		catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild role: %s", data.getGuildName()));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }

	// TODO: CRUD User Guild Role
	public static void addUserToGuild(UserGuildRoleDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
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

			int guildId = Guild.getIdByName(data.getGuild());
			data.setGuildRoleId(GuildRole.getIdByName(guildId,data.getRole()));
			data.setAccountId( UserAccount.getIdByUsername(data.getUsername()));
			if (checkPermission("CRUDUserGuildRole",data.getGuild())){
				UserGuildRole.insertGuildMember( data.getAccountId(), data.getGuildRoleId());

			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("User Guild Role is existed: %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add user to guild: %s", data.getUsername()));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static void updateUserInGuild(UserGuildRoleDTO data, UserGuildRoleDTO newData)
            throws SQLIntegrityConstraintViolationException, SQLException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
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
			int guildId = Guild.getIdByName(data.getGuild());

			newData.setAccountId( UserAccount.getIdByUsername(newData.getUsername()));
			int newGuildId = Guild.getIdByName(newData.getGuild());
			newData.setGuildRoleId(GuildRole.getIdByName(newGuildId,newData.getRole()));
			if (checkPermission("CRUDUserGuildRole",data.getGuild())){
				UserGuildRole.updateGuildMember( newData.getAccountId(), guildId,newData.getGuildRoleId());

			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update user in guild: %s", data.getUsername()));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static void deleteUserInGuild(UserGuildRoleDTO data)
            throws SQLIntegrityConstraintViolationException, SQLException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			data.setAccountId( UserAccount.getIdByUsername(data.getUsername()));
			int guildId = Guild.getIdByName(data.getGuild());
			if (checkPermission("CRUDUserGuildRole",data.getGuild())){
				UserGuildRole.deleteGuildMember(data.getAccountId(),guildId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete user in guild: %s" + e, data.getUsername()));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static List<UserGuildRoleDTO> getAllUserGuildRolesByGuildID(String guild) throws SQLException, NotFoundException, NullPointerException, TokenException, NotHavePermission {
		try {
			int guildId = Guild.getIdByName(guild);
			List<UserGuildRoleDTO> data = new ArrayList<>();
			if (checkPermission("ViewUserGuildRole",guild)){
				data = UserGuildRole.getAllByGuildId(guild,guildId);
			}else {
				throw new NotHavePermission("You don't have permission");
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
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }

	// TODO: CRUD Guild Permission
	public static void addGuildPermission(String data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			if (data.isEmpty()) {
				throw new DataEmptyException("Guild Permission is empty");
			}
			if (!isValidString(data)) {
				throw new InvalidDataException("Invalid guild permission");
			}

			data = normalizePermission(data);

			if (checkPermission("CRUDGuildPermission",null)){
				GuildPermission.insert(data);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Your guild permission is existed: %s", data));
		} catch (SQLException | IOException | ClassNotFoundException e) {
			throw new SQLException(String.format("Error occurs when add guild permission: %s", data));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static void updateGuildPermission(String data, String newData) throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			if (newData.isEmpty() ) {
				throw new DataEmptyException("Guild Permission is empty");
			}
			if (!isValidString(newData)) {
				throw new InvalidDataException("Invalid guild permission");
			}
			data = normalizePermission(data);
			newData = normalizePermission(newData);
			if (checkPermission("CRUDGuildPermission",null)){
				GuildPermission.update( data, newData);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values guild permission %s", data));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild permission: %s", data));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void deleteGuildPermission(String data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			if (checkPermission("CRUDGuildPermission",null)){
				GuildPermission.delete(data);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values guild permission %s", data));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild permission: %s", data));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }
	// TODO: CRUD Permission To Guild Role
	public static void addPermissionToGuildRole(GuildRoleDTO guildRole, String permission)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			int guildId = Guild.getIdByName(guildRole.getGuildName());
			int guildRoleId = GuildRole.getIdByName(guildId,guildRole.getRole());
			int permissionId =  GuildPermission.getIdByName(permission);
			if (checkPermission("CRUDGuildRolePermission",guildRole.getGuildName())){
				GuildPermission.addPermissionToGuildRole(guildRoleId,permissionId );
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Your permission %s guild role is existed: %s", permission,guildRole.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add permission to guild role: %s", guildRole.getGuildName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	public static void updatePermissionInGuildRole( GuildRoleDTO guildRole, String permission,String newPermission) throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			int guildId = Guild.getIdByName(guildRole.getGuildName());
			int guildRoleId = GuildRole.getIdByName(guildId,guildRole.getRole());

			int permissionId =  GuildPermission.getIdByName(permission);
			int newPermissionId =  GuildPermission.getIdByName(newPermission);
			if (checkPermission("CRUDGuildRolePermission",guildRole.getGuildName())){
				GuildPermission.updatePermissionInGuildRole(newPermissionId,permissionId,guildRoleId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values permission %s guild role %s",permission,guildRole.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update permission guild role: %s", guildRole.getGuildName()));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void deletePermissionInGuildRole(GuildRoleDTO guildRole, String permission)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			int guildId = Guild.getIdByName(guildRole.getGuildName());
			int guildRoleId = GuildRole.getIdByName(guildId,guildRole.getRole());

			int permissionId =  GuildPermission.getIdByName(permission);
			if (checkPermission("CRUDGuildRolePermission",guildRole.getGuildName())){
				GuildPermission.deletePermissionInGuildRole(permissionId,guildRoleId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values permission %s guild role %s",permission,guildRole.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update permission guild role: %s", guildRole.getGuildName()));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static List<GuildPermission> getAllPermissionByAccountId(String guild, String userName)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			if (userName.isEmpty()) {
				throw new DataEmptyException("Username is empty");
			}
			int guildId = Guild.getIdByName(guild);
			String accountId = UserAccount.getIdByUsername(userName);
			List<GuildPermission> data = new ArrayList<>();
			if (checkPermission("ViewGuildRolePermission",guild)){
				data = GuildPermission.getAllByAccountIdAndGuildId(accountId,guildId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
			return data;
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException("Disallow null values username");
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when view permission crew role: %s", guild));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }
	// TODO Crew Event
	public static void insertGuildEvent( GuildEventDto guildEvent, String dateStart, String dateEnd)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
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
			int guildId = Guild.getIdByName(guildEvent.getGuildName());
			Timestamp start = validTimeStamp(dateStart);
			Timestamp end = validTimeStamp(dateEnd);
			guildEvent = new GuildEventDto(guildId,guildEvent.getTitle(),guildEvent.getDescription(),guildEvent.getGenerationId(),start,end,guildEvent.getType());
			if (checkPermission("CRUDGuildEvent",guildEvent.getGuildName())){
				GuildEvent.insert(guildEvent);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException | IOException | ClassNotFoundException e) {
			throw new SQLException(String.format("Your guild event is existed: %s", guildEvent.getTitle()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild event: %s", guildEvent.getTitle()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void updateGuildEvent(GuildEventDto guildEvent, int guildEventId, String dateStart, String dateEnd)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
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
			int guildId = Guild.getIdByName(guildEvent.getGuildName());
			Timestamp start = validTimeStamp(dateStart);
			Timestamp end = validTimeStamp(dateEnd);
			guildEvent = new GuildEventDto(guildId,guildEvent.getTitle(),guildEvent.getDescription(),guildEvent.getGenerationId(),start,end,guildEvent.getType());
			if (checkPermission("CRUDGuildEvent",guildEvent.getGuildName())){
				GuildEvent.update(guildEvent,guildEventId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values %s", guildEvent.getTitle()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild event: %s" + e, guildEvent.getTitle()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void deleteGuildEvent(int guildEventId, String guild)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			if (checkPermission("CRUDGuildEvent",guild)){
				GuildEvent.delete(guildEventId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", guildEventId));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild event: %s", guildEventId));
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static List<GuildEventDto> getAllEvent()
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			List<GuildEventDto> data = new ArrayList<>();
			data = GuildEvent.getAllGuildEvent();
			for (GuildEventDto guildEventDto : data) {
				guildEventDto.setGuildName(guildEventDto.getGuildName());
				guildEventDto.setGeneration(guildEventDto.getGeneration());
			}
			return data;
		} catch (SQLException | IOException | ClassNotFoundException e) {
			throw new SQLException("Error occurs when view guild event");
		}
    }
	// TODO: Search
	public static List<UserProfileDTO> findByUsername(String username)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			List<UserProfileDTO> data = new ArrayList<>();
			data = UserProfile.findByUsername(username);
			for (UserProfileDTO userProfileDTO : data) {
				userProfileDTO.setGenerationName(Generation.getNameById(userProfileDTO.getGenerationId()));
			}
			return data;
		} catch (SQLException | IOException | ClassNotFoundException e) {
			throw new SQLException("Error occurs when view guild event");
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
		Date parsedDate = sdf.parse(date);
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
            throws SQLException, NotFoundException, TokenException, IOException {
		Path path = (Path) Paths.get("storage.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
		return accountId;
	}
	public static boolean checkPermission(String permission, String guildChose) throws SQLException, TokenException, NotFoundException {
		try {
			boolean  check = false;

			List<String> listRole = UserRole.getRoleByAccountId(getAccountIDUser());
			for (String role : listRole) {
				if (role.equals("President")){
					return true;
				}
			}
			check = AuthService.GuildAuthorization(Guild.getIdByName(guildChose),permission);

			return check;
		} catch (SQLException e) {
			throw new SQLException("Error occurs when query");
		} catch (TokenException | IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");
		}
    }


}
