package controllers;

import constants.ResponseStatus;
import dto.*;
import exceptions.DataEmptyException;
import exceptions.InvalidDataException;
import exceptions.NotFoundException;
import exceptions.TokenException;
import models.Crew;
import models.Generation;
import models.permissions.CrewPermission;
import models.roles.CrewRole;
import services.CrewService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CrewController {
    //TODO: CRUD Crew
    public static ResponseDTO<Object> addCrew(Connection con, CrewDTO crewDTO) {
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
        } catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        } catch (TokenException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResponseDTO<Object> deleteCrew(Connection connection, CrewDTO crewDTO)   {
        try {
            CrewService.delete(connection, crewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete %s from database successfully!", crewDTO.getName()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> updateCrew(Connection connection, CrewDTO crewDTO, CrewDTO newCrewDTO) {
        try {
            CrewService.update(connection, crewDTO,newCrewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update %s successfully!", crewDTO.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
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
    //TODO: CRUD Guild Role
    public static ResponseDTO<Object> addCrewRole(Connection connection, CrewRoleDTO crewRoleDTO ) {
        try {
            CrewService.insertCrewRole(connection, crewRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add crew role %s successfully!", crewRoleDTO.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updateCrewRole(Connection connection, CrewRoleDTO crewRoleDTO, CrewRoleDTO newCrewRoleDTO ) {
        try {
            CrewService.updateCrewRole(connection, crewRoleDTO, newCrewRoleDTO );
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update crew role %s successfully!", crewRoleDTO.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> deleteCrewRole(Connection connection, CrewRoleDTO crewRoleDTO ) {
        try {
            CrewService.deleteCrewRole(connection, crewRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete role %s from crew successfully!", crewRoleDTO.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<CrewRole>> getAllCrewRoles(Connection connection, String crew) {
        try {
            int crewId = Crew.getIdByName(connection,crew);
            List<CrewRole> data = CrewRole.getAllByCrewId(connection,crewId);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crew roles successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found Guild ID!", null);
        }
    }
    // TODO: CRUD User Guild Role
    public static ResponseDTO<Object> addUserCrewRole(Connection connection, UserCrewRoleDto userCrewRoleDto ) {
        try {
            CrewService.addUserToCrew(connection, userCrewRoleDto);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add user %s crew role %s to crew %s successfully!",
                            userCrewRoleDto.getUsername(),userCrewRoleDto.getRole(),userCrewRoleDto.getCrew()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updateUserCrewRole(Connection connection,UserCrewRoleDto userCrewRoleDto, UserCrewRoleDto newUserCrewRoleDto ) {
        try {
            CrewService.updateUserToCrew(connection, userCrewRoleDto, newUserCrewRoleDto);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update user %s role %s in crew %s successfully!",
                            userCrewRoleDto.getUsername(),userCrewRoleDto.getRole(),userCrewRoleDto.getCrew()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> deleteUserCrewRole(Connection connection, UserCrewRoleDto userCrewRoleDto ) {
        try {
            CrewService.deleteUserInCrew(connection, userCrewRoleDto);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete user %s role %s in crew %s successfully!",
                            userCrewRoleDto.getUsername(),userCrewRoleDto.getRole(),userCrewRoleDto.getCrew()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<UserCrewRoleDto>> getAllUserCrewRolesByCrewID(Connection connection, String crew) {
        try {
            List<UserCrewRoleDto> data = CrewService.getAllUserCrewRolesByCrewID(connection, crew);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild roles successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found Guild ID!", null);
        } catch (IndexOutOfBoundsException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
    // TODO: CRUD Crew Permission
    public static ResponseDTO<Object> addCrewPermission(Connection connection,String data) {
        try {
            CrewService.addCrewPermission(connection, data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add crew permission %s to database successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deleteCrewPermission(Connection connection, String data ) {
        try {
            CrewService.deleteGuildPermission(connection, data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete crew permission %s from database successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateCrewPermission(Connection connection,String data, String newData ) {
        try {
            CrewService.updateCrewPermission(connection, data, newData);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update crew permission %s successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllCrewPermissions(Connection connection) {
        try {
            List<String> data = CrewPermission.getAllCrewPermission(connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crews successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    // TODO: CRUD Permission In Guild Role
    public static ResponseDTO<Object> addPermissionToCrewRole(Connection connection,CrewRoleDTO crewRole, String permission) {
        try {
            CrewService.addPermissionToCrewRole(connection, crewRole,permission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add permission %s to crew role %s successfully!", permission,crewRole.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deletePermissionInCrewRole(Connection connection, CrewRoleDTO crewRole, String permission ) {
        try {
            CrewService.deletePermissionInCrewRole(connection, crewRole,permission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete permission %s in crew role %s successfully!", permission,crewRole.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updatePermissionInCrewRole(Connection connection, CrewRoleDTO crewRole, String permission, String newPermission ) {
        try {
            CrewService.updatePermissionInCrewRole(connection,crewRole,permission,newPermission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update permission %s in crew role %s successfully!", permission,crewRole.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<String>> getAllPermissionByCrewId(Connection connection, String crew, String role) {
        try {
            List<String> data = CrewService.getAllPermissionByCrewId(connection,crew,role);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crew permission successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NullPointerException e ){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<CrewPermission>> getAllPermissionByAccountId(Connection connection, String crew, String userName) {
        try {
            List<CrewPermission> data = CrewService.getAllPermissionByAccountId(connection,crew,userName);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crew permission successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found crew permission!", null);
        } catch (DataEmptyException | InvalidDataException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
    // TODO: Crew Event
    public static ResponseDTO<Object> addCrewEvent(Connection connection,CrewEventDto crewEventDto,String dateStart,String dateEnd) {
        try {
            CrewService.insertCrewEvent(connection, crewEventDto,dateStart,dateEnd);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add crew event %s successfully!", crewEventDto.getTitle()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deleteCrewEvent(Connection connection, int crewEventId, String crew ) {
        try {
            CrewService.deleteCrewEvent(connection, crewEventId, crew);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete crew event %s successfully!", crewEventId), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateCrewEvent(Connection connection, CrewEventDto crewEventDto, int crewEventId , String dateStart, String dateEnd) {
        try {
            CrewService.updateCrewEvent(connection,crewEventDto,crewEventId,  dateStart,  dateEnd);
            return new ResponseDTO<>(ResponseStatus.OK,
                    "Update crew event successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<CrewEventDto>> getAllCrewEvent(Connection connection) {
        try {
            List<CrewEventDto> data = CrewService.getAllEvent(connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crew event successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found crew event!", null);
        }catch (DataEmptyException | InvalidDataException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<String>> getAllGeneration(Connection connection){
        try {
            List<String> data = Generation.getAllGenerations(connection);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all generation successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found generation!", null);
        }
    }
}
