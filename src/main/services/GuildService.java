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
	public static void create(Connection con, GuildDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			boolean checkPermissions = false;
			if (data.getName().isEmpty()) {
				throw new DataEmptyException("Guild Name is empty");
			}
			if (!isValidString(data.getName())) {
				throw new InvalidDataException("Invalid guild name");
			}
			checkPermissions = AuthService.AppAuthorization(con,"CRUDGuild");
			if (checkPermissions) {
				data.setName(normalizeName(data.getName()));
				Guild.insert(con, data.getName());
			} else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Your guild name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild: %s", data.getName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void update(Connection con, GuildDTO data, GuildDTO newData)
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
			newData = new GuildDTO(Guild.getIdByName(con,data.getName()), newData.getName());

			if (AuthService.AppAuthorization(con,"CRUDGuild")) {
				Guild.update(con, newData.getId(), newData.getName());
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Your name is exist: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild: %s", data.getName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void delete(Connection con, GuildDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			data = new GuildDTO(Guild.getIdByName(con,data.getName()), data.getName());
			if (AuthService.AppAuthorization(con,"CRUDGuild")) {
				Guild.delete(con, data.getId());
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", data.getName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static List<String> getMemberInGuild(Connection con, String guild)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			int guildId = Guild.getIdByName(con, guild);
			List<String> listUsername = new ArrayList<>();
			if (checkPermission(con,"ViewGuild",guild)) {
				listUsername = Guild.getMemberInGuild(con, guildId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
			return listUsername;
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", guild));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", guild));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");		}
	}
	public static UserProfileDTO getUserProfile(Connection con, String username)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			String accountId = UserAccount.getIdByUsername(con, username);
			UserProfileDTO userProfile = new UserProfileDTO();
			userProfile.setAccountId(accountId);
			UserProfile.read(con,userProfile);
			userProfile.setGenerationName(Generation.getNameById(con,userProfile.getGenerationId()));
			return userProfile;
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", username));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", username));
		}
	}

	// TODO: CRUD Guild Role
	public static void insertGuildRole(Connection con, GuildRoleDTO data)
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
			data.setGuildId(Guild.getIdByName(con,data.getGuildName()));
			if (checkPermission(con,"CRUDGuildRole",data.getGuildName())) {
				GuildRole.insertGuildRole(con,data.getRole(), data.getGuildId());
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Your guild name is existed: %s", data.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild role: %s", data.getGuildName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void updateGuildRole(Connection con, GuildRoleDTO data, GuildRoleDTO newData)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
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
			if (checkPermission(con,"CRUDGuildRole",data.getGuildName())){
				GuildRole.updateGuildRole(con,newData.getRole(), data.getId());
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(String.format("Your role existed: %s", data.getGuildName()));
		}
		catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild role: %s" + e, data.getGuildName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");		}
    }

	public static void deleteGuildRole(Connection con, GuildRoleDTO data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			data.setGuildId(Guild.getIdByName(con,data.getGuildName()));
			data.setId(GuildRole.getIdByName(con,data.getGuildId(),data.getRole()));
			if (checkPermission(con,"CRUDGuildRole",data.getGuildName())){
				GuildRole.deleteGuildRole(con,data.getId());
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Disallow null values %s ", data.getGuildName()));
		}
		catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild role: %s", data.getGuildName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }

	// TODO: CRUD User Guild Role
	public static void addUserToGuild(Connection con, UserGuildRoleDTO data)
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

			int guildId = Guild.getIdByName(con,data.getGuild());
			data.setGuildRoleId(GuildRole.getIdByName(con,guildId,data.getRole()));
			data.setAccountId( UserAccount.getIdByUsername(con,data.getUsername()));
			if (checkPermission(con,"CRUDUserGuildRole",data.getGuild())){
				UserGuildRole.insertGuildMember(con, data.getAccountId(), data.getGuildRoleId());

			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("User Guild Role is existed: %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add user to guild: %s", data.getUsername()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static void updateUserInGuild(Connection con, UserGuildRoleDTO data, UserGuildRoleDTO newData)
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
			int guildId = Guild.getIdByName(con,data.getGuild());

			newData.setAccountId( UserAccount.getIdByUsername(con,newData.getUsername()));
			int newGuildId = Guild.getIdByName(con,newData.getGuild());
			newData.setGuildRoleId(GuildRole.getIdByName(con,newGuildId,newData.getRole()));
			if (checkPermission(con,"CRUDUserGuildRole",data.getGuild())){
				UserGuildRole.updateGuildMember(con, newData.getAccountId(), guildId,newData.getGuildRoleId());

			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update user in guild: %s", data.getUsername()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static void deleteUserInGuild(Connection con, UserGuildRoleDTO data)
            throws SQLIntegrityConstraintViolationException, SQLException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			data.setAccountId( UserAccount.getIdByUsername(con,data.getUsername()));
			int guildId = Guild.getIdByName(con,data.getGuild());
			if (checkPermission(con,"CRUDUserGuildRole",data.getGuild())){
				UserGuildRole.deleteGuildMember(con,data.getAccountId(),guildId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete user in guild: %s" + e, data.getUsername()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static List<UserGuildRoleDTO> getAllUserGuildRolesByGuildID(Connection connection, String guild) throws SQLException, NotFoundException, NullPointerException, TokenException, NotHavePermission {
		try {
			int guildId = Guild.getIdByName(connection,guild);
			List<UserGuildRoleDTO> data = new ArrayList<>();
			if (checkPermission(connection,"ViewUserGuildRole",guild)){
				data = UserGuildRole.getAllByGuildId(connection,guild,guildId);
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
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }

	// TODO: CRUD Guild Permission
	public static void addGuildPermission(Connection con, String data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			if (data.isEmpty()) {
				throw new DataEmptyException("Guild Permission is empty");
			}
			if (!isValidString(data)) {
				throw new InvalidDataException("Invalid guild permission");
			}

			data = normalizePermission(data);

			if (checkPermission(con,"CRUDGuildPermission",null)){
				GuildPermission.insert(data,con);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Your guild permission is existed: %s", data));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add guild permission: %s", data));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static void updateGuildPermission(Connection con, String data, String newData) throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			if (newData.isEmpty() ) {
				throw new DataEmptyException("Guild Permission is empty");
			}
			if (!isValidString(newData)) {
				throw new InvalidDataException("Invalid guild permission");
			}
			data = normalizePermission(data);
			newData = normalizePermission(newData);
			if (checkPermission(con,"CRUDGuildPermission",null)){
				GuildPermission.update( data, newData,con);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values guild permission %s", data));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild permission: %s", data));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void deleteGuildPermission(Connection con, String data)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			if (checkPermission(con,"CRUDGuildPermission",null)){
				GuildPermission.delete(data,con);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values guild permission %s", data));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild permission: %s", data));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }
	// TODO: CRUD Permission To Guild Role
	public static void addPermissionToGuildRole(Connection con, GuildRoleDTO guildRole, String permission)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			int guildId = Guild.getIdByName(con,guildRole.getGuildName());
			int guildRoleId = GuildRole.getIdByName(con,guildId,guildRole.getRole());
			int permissionId =  GuildPermission.getIdByName(con,permission);
			if (checkPermission(con,"CRUDGuildRolePermission",guildRole.getGuildName())){
				GuildPermission.addPermissionToGuildRole(con,guildRoleId,permissionId );
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Your permission %s guild role is existed: %s", permission,guildRole.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add permission to guild role: %s", guildRole.getGuildName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void updatePermissionInGuildRole(Connection con, GuildRoleDTO guildRole, String permission,String newPermission) throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			int guildId = Guild.getIdByName(con,guildRole.getGuildName());
			int guildRoleId = GuildRole.getIdByName(con,guildId,guildRole.getRole());

			int permissionId =  GuildPermission.getIdByName(con,permission);
			int newPermissionId =  GuildPermission.getIdByName(con,newPermission);
			if (checkPermission(con,"CRUDGuildRolePermission",guildRole.getGuildName())){
				GuildPermission.updatePermissionInGuildRole(newPermissionId,permissionId,guildRoleId,con );
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values permission %s guild role %s",permission,guildRole.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update permission guild role: %s", guildRole.getGuildName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void deletePermissionInGuildRole(Connection con, GuildRoleDTO guildRole, String permission)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			int guildId = Guild.getIdByName(con,guildRole.getGuildName());
			int guildRoleId = GuildRole.getIdByName(con,guildId,guildRole.getRole());

			int permissionId =  GuildPermission.getIdByName(con,permission);
			if (checkPermission(con,"CRUDGuildRolePermission",guildRole.getGuildName())){
				GuildPermission.deletePermissionInGuildRole(permissionId,guildRoleId, con);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values permission %s guild role %s",permission,guildRole.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update permission guild role: %s", guildRole.getGuildName()));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static List<GuildPermission> getAllPermissionByAccountId(Connection connection, String guild, String userName)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			if (userName.isEmpty()) {
				throw new DataEmptyException("Username is empty");
			}
			int guildId = Guild.getIdByName(connection,guild);
			String accountId = UserAccount.getIdByUsername(connection,userName);
			List<GuildPermission> data = new ArrayList<>();
			if (checkPermission(connection,"ViewGuildRolePermission",guild)){
				data = GuildPermission.getAllByAccountIdAndGuildId(connection,accountId,guildId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
			return data;
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException("Disallow null values username");
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when view permission crew role: %s", guild));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }
	// TODO Crew Event
	public static void insertGuildEvent(Connection con, GuildEventDto guildEvent, String dateStart, String dateEnd)
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
			int guildId = Guild.getIdByName(con,guildEvent.getGuildName());
			int generationId = Generation.getIdByName(con,guildEvent.getGeneration());
			Timestamp start = validTimeStamp(dateStart);
			Timestamp end = validTimeStamp(dateEnd);
			guildEvent = new GuildEventDto(guildId,guildEvent.getTitle(),guildEvent.getDescription(),generationId,start,end,guildEvent.getType());
			if (checkPermission(con,"CRUDGuildEvent",guildEvent.getGuildName())){
				GuildEvent.insert(con, guildEvent);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Your guild event is existed: %s", guildEvent.getTitle()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild event: %s", guildEvent.getTitle()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void updateGuildEvent(Connection con, GuildEventDto guildEvent, int guildEventId, String dateStart, String dateEnd)
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
			int guildId = Guild.getIdByName(con,guildEvent.getGuildName());
			int generationId = Generation.getIdByName(con,guildEvent.getGeneration());
			Timestamp start = validTimeStamp(dateStart);
			Timestamp end = validTimeStamp(dateEnd);
			guildEvent = new GuildEventDto(guildId,guildEvent.getTitle(),guildEvent.getDescription(),generationId,start,end,guildEvent.getType());
			if (checkPermission(con,"CRUDGuildEvent",guildEvent.getGuildName())){
				GuildEvent.update(con, guildEvent,guildEventId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values %s", guildEvent.getTitle()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild event: %s" + e, guildEvent.getTitle()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }

	public static void deleteGuildEvent(Connection con, int guildEventId, String guild)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			if (checkPermission(con,"CRUDGuildEvent",guild)){
				GuildEvent.delete(con, guildEventId);
			}else {
				throw new NotHavePermission("You don't have permission");
			}
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", guildEventId));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild event: %s", guildEventId));
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");        }
    }
	public static List<GuildEventDto> getAllEvent(Connection connection)
            throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			List<GuildEventDto> data = new ArrayList<>();
			data = GuildEvent.getAllGuildEvent(connection);
			for (GuildEventDto guildEventDto : data) {
				guildEventDto.setGuildName(guildEventDto.getGuildName());
				guildEventDto.setGeneration(guildEventDto.getGeneration());
			}
			return data;
		} catch (SQLException e) {
			throw new SQLException("Error occurs when view guild event");
		}
    }
	// TODO: Search
	public static List<UserProfileDTO> findByUsername(Connection connection, String username)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException, TokenException {
		try {
			List<UserProfileDTO> data = new ArrayList<>();
			data = UserProfile.findByUsername(connection, username);
			for (UserProfileDTO userProfileDTO : data) {
				userProfileDTO.setGenerationName(Generation.getNameById(connection,userProfileDTO.getGenerationId()));
			}
			return data;
		} catch (SQLException e) {
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
            throws SQLException, NotFoundException, TokenException {
		Path path = (Path) Paths.get("storage.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
		return accountId;
	}
	public static boolean checkPermission(Connection connection, String permission, String guildChose) throws SQLException, TokenException, NotFoundException {
		try {
			boolean  check = false;

			List<String> listRole = UserRole.getRoleByAccountId(connection,getAccountIDUser());
			for (String role : listRole) {
				if (role.equals("President")){
					return true;
				}
			}
			check = AuthService.GuildAuthorization(connection,Guild.getIdByName(connection,guildChose),permission);

			return check;
		} catch (SQLException e) {
			throw new SQLException("Error occurs when query");
		} catch (TokenException e) {
			throw new TokenException("Can't get access token");
		}
    }


}
