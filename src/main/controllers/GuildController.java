package controllers;

import dto.GuildDTO;
import dto.GuildRoleDTO;
import dto.ResponseDTO;
import dto.UserGuildRoleDTO;
import exceptions.DataEmptyException;
import exceptions.InvalidDataException;
import exceptions.NotFoundException;
import models.Guild;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import constants.ResponseStatus;
import models.permissions.GuildPermission;
import models.roles.GuildRole;
import models.users.UserGuildRole;
import services.GuildService;

public class GuildController {
    // TODO: CRUD Guild
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
                    String.format("Delete role %s from guild successfully!", guildRoleDTO.getGuildName()), null);
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
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild roles successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found Guild ID!", null);
        }
    }
    // TODO: CRUD User Guild Role
    public static ResponseDTO<Object> addUserGuildRole(Connection connection, UserGuildRoleDTO userGuildRoleDTO ) {
        try {
            GuildService.addUserToGuild(connection, userGuildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add user %s guild role %s to guild %s successfully!",
                            userGuildRoleDTO.getUsername(),userGuildRoleDTO.getRole(),userGuildRoleDTO.getGuild()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updateUserGuildRole(Connection connection,UserGuildRoleDTO userGuildRoleDTO, UserGuildRoleDTO newUserGuildRoleDTO ) {
        try {
            GuildService.updateUserInGuild(connection, userGuildRoleDTO, newUserGuildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update user %s role %s in guild %s successfully!",
                            userGuildRoleDTO.getUsername(),userGuildRoleDTO.getRole(),userGuildRoleDTO.getGuild()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> deleteUserGuildRole(Connection connection, UserGuildRoleDTO userGuildRoleDTO ) {
        try {
            GuildService.deleteUserInGuild(connection, userGuildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete user %s role %s in guild %s successfully!",
                            userGuildRoleDTO.getUsername(),userGuildRoleDTO.getRole(),userGuildRoleDTO.getGuild()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<UserGuildRoleDTO>> getAllUserGuildRolesByGuildID(Connection connection, String guild) {
        try {
            int guildId = Guild.getIdByName(connection,guild);
            List<UserGuildRoleDTO> data = UserGuildRole.getAllByGuildId(connection,guild,guildId);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild roles successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found Guild ID!", null);
        }
    }
    // TODO: CRUD Guild Permission
    public static ResponseDTO<Object> addGuildPermission(Connection connection,String data) {
        try {
            GuildService.addGuildPermission(connection, data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add guild permission %s to database successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deleteGuildPermission(Connection connection, String data ) {
        try {
            GuildService.deleteGuildPermission(connection, data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete guild permission %s from database successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateGuildPermission(Connection connection,String data, String newData ) {
        try {
            GuildService.updateGuildPermission(connection, data, newData);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update guild permission %s successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllGuildPermissions(Connection connection) {
        try {
            List<String> data = GuildPermission.getAllGuildPermission(connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guilds successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    // TODO: CRUD Permission In Guild Role
    public static ResponseDTO<Object> addPermissionToGuildRole(Connection connection,GuildRoleDTO guildRole, String permission) {
        try {
            GuildService.addPermissionToGuildRole(connection, guildRole,permission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add permission %s to guild role %s successfully!", permission,guildRole.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deletePermissionInGuildRole(Connection connection, GuildRoleDTO guildRole, String permission ) {
        try {
            GuildService.deletePermissionInGuildRole(connection, guildRole,permission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete permission %s in guild role %s successfully!", permission,guildRole.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updatePermissionInGuildRole(Connection connection, GuildRoleDTO guildRole, String permission, String newPermission ) {
        try {
            GuildService.updatePermissionInGuildRole(connection, guildRole, permission, newPermission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update permission %s in guild role %s successfully!", permission,guildRole.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

}
