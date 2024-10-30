package controllers;


import constants.ResponseStatus;
import dto.CrewDTO;
import dto.ResponseDTO;
import dto.UserCrewRoleDto;
import exceptions.NotFoundException;
import models.Crew;
import models.roles.CrewRole;
import services.CrewService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;



public class CrewController {
    public static ResponseDTO<Object> create(Connection con, CrewDTO crewDTO) throws SQLException {
        try {
            CrewService.create(con, crewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add crew %s to database successfully!", crewDTO.getName()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> delete(Connection connection, CrewDTO crewDTO) throws SQLException {
        try {
            CrewService.delete(connection, crewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete %s from database successfully!", crewDTO.getName()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> update(Connection connection, CrewDTO crewDTO) throws SQLException {
        try {
            CrewService.update(connection, crewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update %s successfully!", crewDTO.getName()), null);
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