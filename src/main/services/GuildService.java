package services;


import dto.GuildDTO;
import dto.UserGuildRoleDTO;
import exceptions.NotFoundException;
import models.Guild;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class GuildService {
	public static void create(Connection con, GuildDTO data)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
		try {
			Guild.insert(con, data.getName());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Your guild name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild: %s", data.getName()));
		}
	}

	public static void update(Connection con, GuildDTO data) throws SQLException, SQLIntegrityConstraintViolationException,NotFoundException {
		try {
			Guild.update(con, data.getId(), data.getName());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Your guild name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild: %s", data.getName()));
		}
	}

	public static void delete(Connection con, GuildDTO data) throws SQLException, SQLIntegrityConstraintViolationException,NotFoundException {
		try {
			Guild.delete(con, data.getId());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Your guild name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", data.getName()));
		}
	}

	public static void addUserToCrew(Connection con, UserGuildRoleDTO data) {
	}

}
