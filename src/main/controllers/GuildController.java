package controllers;

import data.GuildData;
import data.ResponseData;
import exceptions.NotFoundException;
import models.Guild;
import models.roles.GuildRole;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import constants.ResponseStatus;

public class GuildController {
    public static ResponseData<Object> add(Connection connection, String userName, String guildName, String guildRole) {
        try {
            GuildData guildData = new GuildData(userName, guildName, guildRole);
            GuildRole.insertGuildMember(connection, guildData);
            return new ResponseData<>(ResponseStatus.OK,
                    String.format("Add %s to %s guild successfully!", userName, guildName), null);
        } catch (SQLException e) {
            return new ResponseData<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseData<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseData<Object> delete(Connection connection, String userName, String guildName,
            String guildRole) {
        try {
            GuildData guildData = new GuildData(userName, guildName, guildRole);
            GuildRole.deleteCrewMember(connection, guildData);
            return new ResponseData<>(ResponseStatus.OK,
                    String.format("Delete %s from %s guild successfully!", userName, guildName), null);
        } catch (SQLException e) {
            return new ResponseData<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseData<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseData<Object> update(Connection connection, String userName, String guildName,
            String guildRole) {
        try {
            GuildData guildData = new GuildData(userName, guildName, guildRole);
            GuildRole.deleteCrewMember(connection, guildData);
            return new ResponseData<>(ResponseStatus.OK,
                    String.format("Update %s successfully!", userName, guildName), null);
        } catch (SQLException e) {
            return new ResponseData<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseData<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseData<List<String>> getAllGuilds(Connection connection) {
        try {
            List<String> data = Guild.getAllNameToList(connection);
            return new ResponseData<>(ResponseStatus.OK, "Get all guilds successfully!", data);
        } catch (SQLException e) {
            return new ResponseData<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        }
    }
}
