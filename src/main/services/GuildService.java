package services;


import dto.GuildDTO;
import dto.GuildRoleDTO;
import dto.UserGuildRoleDTO;
import exceptions.DataEmptyException;
import exceptions.InvalidDataException;
import exceptions.NotFoundException;
import models.Guild;
import models.roles.GuildRole;
import models.users.UserAccount;
import models.users.UserGuildRole;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class GuildService {

	// TODO: CRUD Guild
	public static void create(Connection con, GuildDTO data)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException {
		try {
			if (data.getName().isEmpty()) {
				throw new DataEmptyException("Guild Name is empty");
			}
			if (!isValidString(data.getName())) {
				throw new InvalidDataException("Invalid guild name");
			}

			normalizeName(data.getName());
			Guild.insert(con, data.getName());
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(String.format("Your guild name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild: %s", data.getName()));
		}
	}

	public static void update(Connection con, GuildDTO data, GuildDTO newData) throws SQLException, SQLIntegrityConstraintViolationException,NotFoundException, DataEmptyException, InvalidDataException {
		try {
			if (newData.getName().isEmpty() ) {
				throw new DataEmptyException("Guild Name is empty");
			}
			if (!isValidString(newData.getName())) {
				throw new InvalidDataException("Invalid guild name");
			}
			normalizeName(data.getName());
			normalizeName(newData.getName());
			newData = new GuildDTO(Guild.getIdByName(con,data.getName()), newData.getName());
			Guild.update(con, newData.getId(), newData.getName());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild: %s", data.getName()));
		}
	}

	public static void delete(Connection con, GuildDTO data)
			throws SQLException, SQLIntegrityConstraintViolationException,NotFoundException, DataEmptyException, InvalidDataException {
		try {
			data = new GuildDTO(Guild.getIdByName(con,data.getName()), data.getName());
			Guild.delete(con, data.getId());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", data.getName()));
		}
	}

	// TODO: CRUD Guild Role
	public static void insertGuildRole(Connection con, GuildRoleDTO data)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException,DataEmptyException,InvalidDataException {
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
			normalizeName(data.getRole());
			normalizeName(data.getGuildName());
			data.setGuildId(Guild.getIdByName(con,data.getGuildName()));
			GuildRole.insertGuildRole(con,data.getRole(), data.getGuildId());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Your guild name is existed: %s", data.getGuildName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild role: %s", data.getGuildName()));
		}
	}

	public static void updateGuildRole(Connection con, GuildRoleDTO data, GuildRoleDTO newData)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException,InvalidDataException {
		try {
			if (newData.getGuildName().isEmpty()) {
				throw new DataEmptyException("New Guild Name is empty");
			} else if (newData.getRole().isEmpty()) {
				throw new DataEmptyException("New Role is empty");
			} else if (isValidString(newData.getRole()) ) {
				throw new InvalidDataException("Invalid role");
			} else if (isValidString(newData.getGuildName())) {
				throw new InvalidDataException("Invalid guild name");
			}
			normalizeName(newData.getRole());
			normalizeName(newData.getGuildName());
			newData.setGuildId(Guild.getIdByName(con,newData.getGuildName()));
			data.setGuildId(Guild.getIdByName(con,data.getGuildName()));
			data.setId(GuildRole.getIdByName(con,data.getGuildId(),data.getRole()));
			GuildRole.updateGuildRole(con,newData.getRole(), data.getId(),newData.getGuildId());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values %s or %s", data.getGuildName(),newData.getGuildName()));
		}
		catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild role: %s", data.getGuildName()));
		}
	}

	public static void deleteGuildRole(Connection con, GuildRoleDTO data)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException {
		try {
			data.setGuildId(Guild.getIdByName(con,data.getGuildName()));
			data.setId(GuildRole.getIdByName(con,data.getGuildId(),data.getRole()));
			GuildRole.deleteGuildRole(con,data.getId());
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values %s ", data.getGuildName()));
		}
		catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild role: %s", data.getGuildName()));
		}
	}

	// TODO: CRUD User Guild Role
	public static void addUserToGuild(Connection con, UserGuildRoleDTO data)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException, DataEmptyException, InvalidDataException {
		try {
			if (data.getUsername().isEmpty()) {
				throw new DataEmptyException("Username is empty");
			} else if (data.getRole().isEmpty()) {
				throw new DataEmptyException("Role is empty");
			} else if (data.getGuild().isEmpty()) {
				throw new DataEmptyException("Guild is empty");
			} else if (isValidString(data.getUsername())) {
				throw new InvalidDataException("Invalid username");
			} else if (isValidString(data.getRole())) {
				throw new InvalidDataException("Invalid role");
			} else if (isValidString(data.getGuild())) {
				throw new InvalidDataException("Invalid guild");
			}
			normalizeName(data.getUsername());
			normalizeName(data.getRole());
			normalizeName(data.getGuild());
			int guildId = Guild.getIdByName(con,data.getGuild());
			data.setGuildRoleId(GuildRole.getIdByName(con,guildId,data.getRole()));
			data.setAccountId( UserAccount.getIdByUsername(con,data.getUsername()));
			UserGuildRole.insertGuildMember(con, data.getAccountId(), data.getGuildRoleId());
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("User Guild Role is existed: %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when add user to guild: %s", data.getUsername()));
		}
	}
	public static void updateUserInGuild(Connection con, UserGuildRoleDTO data, UserGuildRoleDTO newData)
			throws SQLIntegrityConstraintViolationException, SQLException, NotFoundException, DataEmptyException, InvalidDataException {
		try {
			if (newData.getUsername().isEmpty()) {
				throw new DataEmptyException("Username is empty");
			} else if (newData.getRole().isEmpty()) {
				throw new DataEmptyException("Role is empty");
			} else if (newData.getGuild().isEmpty()) {
				throw new DataEmptyException("Guild is empty");
			} else if (isValidString(newData.getUsername())) {
				throw new InvalidDataException("Invalid username");
			} else if (isValidString(newData.getRole())) {
				throw new InvalidDataException("Invalid role");
			} else if (isValidString(newData.getGuild())) {
				throw new InvalidDataException("Invalid guild");
			}
			normalizeName(newData.getUsername());
			normalizeName(newData.getRole());
			normalizeName(newData.getGuild());
			int guildId = Guild.getIdByName(con,data.getGuild());
			data.setAccountId( UserAccount.getIdByUsername(con,data.getUsername()));

			int newGuildId = Guild.getIdByName(con,newData.getGuild());
			newData.setGuildRoleId(GuildRole.getIdByName(con,guildId,newData.getRole()));

			UserGuildRole.updateGuildMember(con, data.getAccountId(), guildId,newData.getGuildRoleId());
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update user in guild: %s", data.getUsername()));
		}
	}
	public static void deleteUserInGuild(Connection con, UserGuildRoleDTO data) throws SQLIntegrityConstraintViolationException, SQLException, NotFoundException {
		try {
			data.setAccountId( UserAccount.getIdByUsername(con,data.getUsername()));
			int guildId = Guild.getIdByName(con,data.getGuild());
			UserGuildRole.deleteGuildMember(con,data.getAccountId(),guildId);
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLException(String.format("Disallow null values %s", data.getUsername()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update user in guild: %s", data.getUsername()));
		}
	}

	public static boolean isValidString(String input){
		return input.matches("[A-Za-z]+");
	}

	public static String normalizeName(String input) {
		String[] parts = input.toLowerCase().split(" ");
		StringBuilder normalized = new StringBuilder();
		for (String part : parts) {
			if (!part.isEmpty()) {
				normalized.append(Character.toUpperCase(part.charAt(0)))
						.append(part.substring(1))
						.append(" ");
			}
		}
		return normalized.toString().trim();
	}


}
