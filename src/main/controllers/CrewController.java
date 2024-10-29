package controllers;

import constants.ResponseStatus;
import dto.CrewDTO;
import dto.ResponseDTO;
import exceptions.NotFoundException;
import models.Crew;
import models.roles.CrewRole;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CrewController {
    public static ResponseDTO<Object> add(Connection connection, CrewDTO crewDTO) throws SQLException {
        try {
            CrewRole.insertCrewMember(connection, crewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add %s to %s crews successfully!", crewDTO.getUserName(), crewDTO.getCrew_name()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> delete(Connection connection, CrewDTO crewDTO) throws SQLException {
        try {
            CrewRole.deleteCrewMember(connection, crewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete %s from %s guild successfully!", crewDTO.getUserName(), crewDTO.getCrew_name()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> update(Connection connection, CrewDTO crewDTO) throws SQLException {
        try {
            CrewRole.deleteCrewMember(connection, crewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update %s successfully!", crewDTO.getUserName()), null);
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
