package controllers;

import constants.ResponseStatus;
import dto.UserCrewRoleDTO;
import dto.ResponseDTO;
import exceptions.NotFoundException;
import models.Crew;
import models.roles.CrewRole;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserCrewRoleController {
    public static ResponseDTO<Object> add(Connection connection, UserCrewRoleDTO userCrewRoleDTO) throws SQLException {
        try {
            CrewRole.insertCrewMember(connection, userCrewRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add %s to %s crews successfully!", userCrewRoleDTO.getUserName(), userCrewRoleDTO.getCrew_name()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> delete(Connection connection, UserCrewRoleDTO userCrewRoleDTO) throws SQLException {
        try {
            CrewRole.deleteCrewMember(connection, userCrewRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete %s from %s guild successfully!", userCrewRoleDTO.getUserName(), userCrewRoleDTO.getCrew_name()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> update(Connection connection, UserCrewRoleDTO userCrewRoleDTO) throws SQLException {
        try {
            CrewRole.deleteCrewMember(connection, userCrewRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update %s successfully!", userCrewRoleDTO.getUserName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllCrews(Connection connection) {
        try {
            List<String> data = Crew.getAllNameToList(connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crews successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        }
    }
}
