package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import dto.CrewDTO;
import dto.UserCrewRoleDto;
import exceptions.NotFoundException;
import models.Crew;

public class CrewService {
	public static void create(Connection con, CrewDTO data)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
		try {
			Crew.insert(con, data.getName());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Your crew name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create crew: %s", data.getName()));
		}
	}

	public static void update(Connection con, CrewDTO data) throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
		try {
			Crew.update(con, data.getId(), data.getName());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Your crew name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update crew: %s", data.getName()));
		}
	}

	public static void delete(Connection con, CrewDTO data) throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException {
		try {
			Crew.delete(con, data.getId());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Your crew name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete crew: %s", data.getName()));
		}
	}

	public static void addUserToCrew(Connection con, UserCrewRoleDto data) {
	}

}
