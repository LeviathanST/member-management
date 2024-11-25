package controllers;

import dto.*;
import exceptions.*;
import models.Generation;
import models.Guild;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import constants.ResponseStatus;
import models.permissions.GuildPermission;
import models.roles.GuildRole;
import services.GuildService;

public class GuildController {
    // TODO: CRUD Guild
    public static ResponseDTO<Object> addGuild(GuildDTO guildDTO) {
        try {
            GuildService.create(guildDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add guild %s to database successfully!", guildDTO.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deleteGuild(GuildDTO guildDTO ) {
        try {
            GuildService.delete(guildDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete %s from database successfully!", guildDTO.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateGuild(GuildDTO guildDTO, GuildDTO newGuildDTO ) {
        try {
            GuildService.update(guildDTO, newGuildDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update %s successfully!", guildDTO.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllGuilds() {
        try {
            List<String> data = Guild.getAllNameToList();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guilds successfully!", data);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        }
    }
    public static ResponseDTO<List<String>> getMemberInGuild(String guild) {
        try {
            List<String> data = GuildService.getMemberInGuild( guild);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all member in guild successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (DataEmptyException e) {
            throw new RuntimeException(e);
        } catch (TokenException | NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<UserProfileDTO> getUserProfile(String username) {
        try {
            UserProfileDTO data = GuildService.getUserProfile( username);
            return new ResponseDTO<>(ResponseStatus.OK, "Get user profile successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (DataEmptyException e) {
            throw new RuntimeException(e);
        } catch (TokenException | NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (InvalidDataException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    //TODO: CRUD Guild Role
    public static ResponseDTO<Object> addGuildRole(GuildRoleDTO guildRoleDTO ) {
        try {
            GuildService.insertGuildRole(guildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add guild role %s successfully!", guildRoleDTO.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updateGuildRole(GuildRoleDTO guildRoleDTO, GuildRoleDTO newGuildRoleDTO ) {
        try {
            GuildService.updateGuildRole(guildRoleDTO, newGuildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update guild role %s successfully!", guildRoleDTO.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> deleteGuildRole(GuildRoleDTO guildRoleDTO ) {
        try {
            GuildService.deleteGuildRole(guildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete role %s from guild successfully!", guildRoleDTO.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, cannot delete this role because related data still exists", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<GuildRole>> getAllGuildRoles(String guild) {
        try {
            int guildId = Guild.getIdByName(guild);
            List<GuildRole> data = GuildRole.getAllByGuildId(guildId);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild roles successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found Guild ID!", null);
        }
    }
    // TODO: CRUD User Guild Role
    public static ResponseDTO<Object> addUserGuildRole(UserGuildRoleDTO userGuildRoleDTO ) {
        try {
            GuildService.addUserToGuild(userGuildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add user %s guild role %s to guild %s successfully!",
                            userGuildRoleDTO.getUsername(),userGuildRoleDTO.getRole(),userGuildRoleDTO.getGuild()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updateUserGuildRole(UserGuildRoleDTO userGuildRoleDTO, UserGuildRoleDTO newUserGuildRoleDTO ) {
        try {
            GuildService.updateUserInGuild(userGuildRoleDTO, newUserGuildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update user %s role %s in guild %s successfully!",
                            userGuildRoleDTO.getUsername(),userGuildRoleDTO.getRole(),userGuildRoleDTO.getGuild()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> deleteUserGuildRole(UserGuildRoleDTO userGuildRoleDTO ) {
        try {
            GuildService.deleteUserInGuild(userGuildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete user %s role %s in guild %s successfully!",
                            userGuildRoleDTO.getUsername(),userGuildRoleDTO.getRole(),userGuildRoleDTO.getGuild()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<UserGuildRoleDTO>> getAllUserGuildRolesByGuildID(String guild) {
        try {

            List<UserGuildRoleDTO> data = GuildService.getAllUserGuildRolesByGuildID(guild);

            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild roles successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found Guild ID!", null);
        } catch (NullPointerException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found data!", null);
        } catch (TokenException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
    // TODO: CRUD Guild Permission
    public static ResponseDTO<Object> addGuildPermission(String data) {
        try {
            GuildService.addGuildPermission(data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add guild permission %s to database successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deleteGuildPermission(String data ) {
        try {
            GuildService.deleteGuildPermission( data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete guild permission %s from database successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateGuildPermission(String data, String newData ) {
        try {
            GuildService.updateGuildPermission(data, newData);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update guild permission %s successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllGuildPermissions() {
        try {
            List<String> data = GuildPermission.getAllGuildPermission();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guilds successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    // TODO: CRUD Permission In Guild Role
    public static ResponseDTO<Object> addPermissionToGuildRole(GuildRoleDTO guildRole, String permission) {
        try {
            GuildService.addPermissionToGuildRole( guildRole,permission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add permission %s to guild role %s successfully!", permission,guildRole.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deletePermissionInGuildRole(GuildRoleDTO guildRole, String permission ) {
        try {
            GuildService.deletePermissionInGuildRole( guildRole,permission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete permission %s in guild role %s successfully!", permission,guildRole.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updatePermissionInGuildRole(GuildRoleDTO guildRole, String permission, String newPermission ) {
        try {
            GuildService.updatePermissionInGuildRole(guildRole, permission, newPermission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update permission %s in guild role %s successfully!", permission,guildRole.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<String>> getAllPermissionByGuildId(String guild, String role) {
        try {
            int guildId = Guild.getIdByName(guild);
            int guildRoleId = GuildRole.getIdByName(guildId,role);
            List<String> listData = GuildPermission.getAllByGuildRoleId(guildRoleId);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild permission successfully!", listData);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found guild permission!", null);
        }
    }
    public static ResponseDTO<List<GuildPermission>> getAllPermissionByAccountId(String guild, String userName) {
        try {
            List<GuildPermission> data = GuildService.getAllPermissionByAccountId(guild,userName);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild permission successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found guild permission!", null);
        } catch (DataEmptyException | InvalidDataException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (TokenException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
    // TODO: Guild Event
    public static ResponseDTO<Object> addGuildEvent(GuildEventDto guildEventDto, String dateStart, String dateEnd) {
        try {
            GuildService.insertGuildEvent( guildEventDto,dateStart,dateEnd);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add guild event %s successfully!", guildEventDto.getTitle()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deleteGuildEvent(int guildEventId,String guild ) {
        try {
            GuildService.deleteGuildEvent(guildEventId, guild);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete guild event %s successfully!", guildEventId), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateGuildEvent(GuildEventDto guildEventDto, int guildEventId , String dateStart, String dateEnd) {
        try {
            GuildService.updateGuildEvent(guildEventDto,guildEventId,  dateStart,  dateEnd);
            return new ResponseDTO<>(ResponseStatus.OK,
                    "Update guild event successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<GuildEventDto>> getAllGuildEvent() {
        try {
            List<GuildEventDto> data = GuildService.getAllEvent();

            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild event successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found guild event!", null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<String>> getAllGeneration(){
        try {
            List<String> data = Generation.getAllGenerations();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all generation successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found generation!", null);
        }
    }
    public static ResponseDTO<List<UserProfileDTO>> findByUsername(String username) {
        try {
            List<UserProfileDTO> data = GuildService.findByUsername(username);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all user successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | DataEmptyException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found username!", null);
        } catch (TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"You don't have permission", null);
        } catch (InvalidDataException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST,"Not found username!", null);
        }
    }

}
