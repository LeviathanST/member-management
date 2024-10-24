package controllers;

import dto.GuildDTO;
import dto.ResponseDTO;
import exceptions.NotFoundException;
import models.Guild;
import models.roles.GuildRole;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import constants.ResponseStatus;

public class GuildController {
    public static ResponseDTO<Object> add(Connection connection, GuildDTO guildDTO) {
        try {
            GuildRole.insertGuildMember(connection, guildDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add %s to %s guild successfully!", guildDTO.getUserName(), guildDTO.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> delete(Connection connection, GuildDTO guildDTO ) {
        try {
            GuildRole.deleteCrewMember(connection, guildDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete %s from %s guild successfully!", guildDTO.getUserName(), guildDTO.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> update(Connection connection,GuildDTO guildDTO ) {
        try {
            GuildRole.deleteCrewMember(connection, guildDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update %s successfully!", guildDTO.getUserName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllGuilds(Connection connection) {
        try {
            List<String> data = Guild.getAllNameToList(connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guilds successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        }
    }
}
