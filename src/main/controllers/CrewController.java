package controllers;

import constants.ResponseStatus;
import data.CrewData;
import data.ResponseData;
import exceptions.NotFoundException;
import models.Crew;
import models.roles.CrewRole;
import models.users.UserAccount;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CrewController {
    public static ResponseData<Object> add(Connection connection, String userName, String crewName, String crewRole) {
        try {
            String account_id = UserAccount.getIdByUsername(connection,userName);
            CrewData crewData = new CrewData(account_id, crewRole, crewName);
            CrewRole.insertCrewMember(connection, crewData);
            return new ResponseData<>(ResponseStatus.OK,
                    String.format("Add %s to %s crews successfully!", userName, crewName), null);
        } catch (SQLException e) {
            return new ResponseData<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseData<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseData<Object> delete(Connection connection, String userName, String crewName, String crewRole) {
        try {
            String account_id = UserAccount.getIdByUsername(connection,userName);
            CrewData crewData = new CrewData(account_id, crewRole, crewName);
            CrewRole.deleteCrewMember(connection, crewData);
            return new ResponseData<>(ResponseStatus.OK,
                    String.format("Delete %s from %s guild successfully!", userName, crewName), null);
        } catch (SQLException e) {
            return new ResponseData<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseData<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseData<Object> update(Connection connection, String userName, String crewName, String crewRole) {
        try {
            String account_id = UserAccount.getIdByUsername(connection,userName);
            CrewData crewData = new CrewData(account_id, crewRole, crewName);
            CrewRole.deleteCrewMember(connection, crewData);
            return new ResponseData<>(ResponseStatus.OK,
                    String.format("Update %s successfully!", userName), null);
        } catch (SQLException e) {
            return new ResponseData<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseData<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseData<List<String>> getAllCrews(Connection connection) {
        try {
            List<String> data = Crew.getAllNameToList(connection);
            return new ResponseData<>(ResponseStatus.OK, "Get all crews successfully!", data);
        } catch (SQLException e) {
            return new ResponseData<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        }
    }
}
