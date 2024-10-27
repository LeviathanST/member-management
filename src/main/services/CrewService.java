package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import dto.CrewDTO;
import dto.UserCrewRoleDto;
import models.Crew;

public class CrewService {
	public static void create(Connection con, CrewDTO data)
			throws SQLException, SQLIntegrityConstraintViolationException {
		try {
			Crew.insert(con, data.getName());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Your crew name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create crew: %s", data.getName()));
		}
	}

	public static void update() {
	}

	public static void delete() {
	}

	public static void addUserToCrew(Connection con, UserCrewRoleDto data) {
	}

}
