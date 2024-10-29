package controllers;

import dto.GuildDTO;
import dto.GuildRoleDTO;
import dto.ResponseDTO;
import exceptions.DataEmptyException;
import exceptions.InvalidDataException;
import exceptions.NotFoundException;
import models.Guild;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import constants.ResponseStatus;
import models.roles.GuildRole;
import services.GuildService;

public class GuildController {
    public static ResponseDTO<Object> addGuild(Connection connection, GuildDTO guildDTO) {
        try {
            GuildService.create(connection, guildDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add guild %s to database successfully!", guildDTO.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deleteGuild(Connection connection, GuildDTO guildDTO ) {
        try {
            GuildService.delete(connection, guildDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete %s from database successfully!", guildDTO.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateGuild(Connection connection,GuildDTO guildDTO, GuildDTO newGuildDTO ) {
        try {
            GuildService.update(connection, guildDTO, newGuildDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update %s successfully!", guildDTO.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
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

    //TODO: CRUD Guild Role
    public static ResponseDTO<Object> addGuildRole(Connection connection, GuildRoleDTO guildRoleDTO ) {
        try {
            GuildService.insertGuildRole(connection, guildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add guild role %s successfully!", guildRoleDTO.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updateGuildRole(Connection connection,GuildRoleDTO guildRoleDTO, GuildRoleDTO newGuildRoleDTO ) {
        try {
            GuildService.updateGuildRole(connection, guildRoleDTO, newGuildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update guild role %s successfully!", guildRoleDTO.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> deleteGuildRole(Connection connection, GuildRoleDTO guildRoleDTO ) {
        try {
            GuildService.deleteGuildRole(connection, guildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete %s from database successfully!", guildRoleDTO.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<GuildRole>> getAllGuildRoles(Connection connection, String guild) {
        try {
            int guildId = Guild.getIdByName(connection,guild);
            List<GuildRole> data = GuildRole.getAllByGuildId(connection,guildId);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guilds successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found Guild ID!", null);
        }
    }



}
